package com.langsin.qq.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import com.langsin.qq.dataBase.util.JdbcTest;
import com.langsin.qq.model.GroupInfo;
import com.langsin.qq.model.MsgCreateTools;
import com.langsin.qq.model.MsgParseTools;
import com.langsin.qq.model.UserInfo;
import com.langsin.qq.msg.IMsgConstance;
import com.langsin.qq.msg.MsgGroupList;
import com.langsin.qq.msg.MsgHead;
import com.langsin.qq.msg.MsgLogin;
import com.langsin.qq.msg.MsgLoginResp;
import com.langsin.qq.msg.MsgReg;
import com.langsin.qq.msg.MsgRegResp;
import com.langsin.qq.msg.MsgTeamList;
import com.langsin.qq.util.LogTools;

public class ServerThread extends Thread {

	private Socket client; // 线程中处理的客户对象
	private DataOutputStream dous; // 输出流对象
	private DataInputStream dins; // 输入流对象
	private UserInfo owerUser; // 这个线程处理对像代表的用户对象
	private boolean loginOK = false;
	private static int baseID = 100000;// 申请QQ号码的基数

	// 创建时必须传入一个Socket对象，即服务器与客户机的连结对象
	public ServerThread(Socket client) {
		this.client = client;
		try {
			this.dous = new DataOutputStream(client.getOutputStream());
			this.dins = new DataInputStream(client.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 连结成功后,服务器要读取第一条消息,被ServerMain调用 可能是登陆消息,也可能是注册请求
	 * 
	 * @return:是否读取成功
	 */
	public boolean readFirstMsg() {
		MsgHead msg = null;
		try {
			// 读取第一条消息
			msg = reciveData();
			// 如果是注册请求
			if (msg.getType() == IMsgConstance.command_reg) {
				reg(msg);
				return false;
			}
			// 如果是登陆请求
			if (msg.getType() == IMsgConstance.command_login) {
				return checkLogin(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 注册
	 */

	private void reg(MsgHead msg) {
		MsgReg mr = (MsgReg) msg;
		/**
		 * 保存注册,生成QQ号,回复应答消息
		 */
		String base = "SELECT ID FROM USERS ORDER BY ID DESC LIMIT 1";
		if (base != null) {
			baseID = JdbcTest.getJdbcTest().getBaseID(base);
		}
		baseID++;
		String sql = "insert into users(ID,Pwd,NikeName,HeadImgSrc) values('"
				+ String.valueOf(baseID) + "','" + mr.getPwd() + "','"
				+ mr.getNikeName() + "','" + mr.getHeadImg() + "')";
		JdbcTest.getJdbcTest().reg(sql);

		/**
		 * 创建一个注册应答对象
		 */
		MsgRegResp mrs = new MsgRegResp();
		mrs.setTotalLen(4 + 1 + 4 + 4 + 1);
		mrs.setType(IMsgConstance.command_reg_resp);
		mrs.setDest(baseID);
		mrs.setSrc(IMsgConstance.Server_QQ_NUMBER);
		mrs.setState((byte) 0);
		// 发送注册应答消息
		this.sendMsgToClient(mrs);
	}

	/**
	 * 检查登陆是否成功, 并且返回好友列表消息。
	 * 
	 * @throws IOException
	 * */
	private boolean checkLogin(MsgHead msg) throws IOException {
		MsgLogin ml = (MsgLogin) msg;
		// 1、到数据库验证是否登陆可成功
		UserInfo loginReg = null;
		String sql_login = "select * from users where ID="
				+ String.valueOf(ml.getSrc());
		UserInfo ui = JdbcTest.getJdbcTest().login(sql_login);
		if (ml.getPwd().equals(ui.getPwd())) { // endsWith(getPwd)
			loginReg = ui;
		} else {
			loginReg = new UserInfo(ml.getSrc());
		}

		/**
		 * 2、登陆成功返回应答包：登陆应答消息和好友分组列表消息
		 */
		if (loginReg.getPwd() != null) {
			//一、 登陆成功:1.回应答包，2.将这个处理线程对象加到队列中
			MsgLoginResp mlr = new MsgLoginResp();
			mlr.setTotalLen(4 + 1 + 4 + 4 + 1 + 16 + 24);
			mlr.setType(IMsgConstance.command_login_resp);
			mlr.setSrc(IMsgConstance.Server_QQ_NUMBER);
			mlr.setDest(loginReg.getID());// 接收者号码
			mlr.setState((byte) 0);
			mlr.setNikeName(loginReg.getNikeName());
			mlr.setHeadImgSrc(loginReg.getHeadImgSrc());
			this.sendMsgToClient(mlr);// 发送应答消息
			owerUser = new UserInfo(loginReg.getID());
			
			//二、从数据库获得 好友分组列表对象，并且发送给客户端
			String sql_team = "SELECT FID,NikeName,HeadImgSrc,FTeamID,FTeamName "
					+ "FROM usersfriends INNER JOIN users ON usersfriends.FID = users.ID "
					+ "WHERE usersfriends.ID  = " + loginReg.getID();
			MsgTeamList mtl = JdbcTest.teamList(sql_team);
			int len = 4 + 1 + 4 + 4;
			mtl.setTotalLen(len);
			mtl.setDest(loginReg.getID());
			mtl.setSrc(IMsgConstance.Server_QQ_NUMBER);
			mtl.setType(IMsgConstance.command_teamList);
			len = getLengthOfMsg(mtl);
			mtl.setTotalLen(len);
			this.sendMsgToClient(mtl);
			
			//三、 从数据库获得 组群列表对象，并且发送给客户端
			String sql_groups = "SELECT groupsusers.GID,GName,GMaster "
					+ "FROM groupsusers JOIN groups ON "
					+ "groupsusers.GID = groups.GID "
					+ "WHERE groupsusers.UID = " + loginReg.getID();
			ArrayList<GroupInfo> groups = JdbcTest.getGroups(sql_groups);
			for (int i = 0; i < groups.size(); i++) {
				String sql_groupList = "SELECT ID,NikeName,HeadImgSrc "
						+ "FROM users JOIN groupsusers ON "
						+ "groupsusers.UID = users.ID "
						+ "WHERE groupsusers.GID =" + groups.get(i).getGID();
				ArrayList<UserInfo> groupsUsers = JdbcTest
						.getGroupsUsers(sql_groupList);
				groups.get(i).setGroupsUsers(groupsUsers);
			}
			MsgGroupList mgl = new MsgGroupList();
			int len2 = 4 + 1 + 4 + 4;
			mgl.setGroupList(groups);
			mgl.setTotalLen(len2);
			mgl.setDest(loginReg.getID());
			mgl.setSrc(IMsgConstance.Server_QQ_NUMBER);
			mgl.setType(IMsgConstance.command_groupList);
			len2 = getLengthOfMsg(mgl);
			mgl.setTotalLen(len2);
			this.sendMsgToClient(mgl);
			loginOK = true;
			return true;

		} else if (loginReg.getPwd() == null) {
			MsgLoginResp mlr = new MsgLoginResp();
			mlr.setTotalLen(4 + 1 + 4 + 4 + 1);
			mlr.setType(IMsgConstance.command_login_resp);
			mlr.setSrc(IMsgConstance.Server_QQ_NUMBER);
			mlr.setDest(loginReg.getID());// 接收者号码
			mlr.setState((byte) -1);
			this.sendMsgToClient(mlr);// 发送应答消息
		}
		this.disConn();// 登陆失败,直接断开
		return false;
	}

	// 线程中执行接收消息的方法
	public void run() {
		try {
			loginOK = readFirstMsg();
			if (loginOK) {
				// 如果登陆成功，将这个处理线程对象加入到队列中
				ChatTools.addClient(owerUser.getID(), this);
			}
			while (loginOK) {
				MsgHead msg = this.reciveData();// 循环接收消息
				ChatTools.sendMsgToOne(owerUser.getID(), msg);// 分发这条消息
			}
		} catch (Exception ef) {
			LogTools.ERROR(this.getClass(), "服务器读消息出错(用户已离线):" + ef);
		}
		// 用户离线了,从队列中移除这个用户对应的处理线程对象
		ChatTools.removeClient(owerUser.getID());
	}

	/**
	 * 从输入流上读取数据块,解包为消息对象
	 * 
	 * @return:将读到的数据块解析为消息对象
	 */
	private MsgHead reciveData() throws Exception {
		int len = dins.readInt(); // 读取消息长度
		LogTools.INFO(this.getClass(), "服务器读消息长度:" + len);
		byte[] data = new byte[len - 4];
		dins.readFully(data);
		MsgHead msg = MsgParseTools.parseMsg(data);// 解析为消息对象
		LogTools.INFO(this.getClass(), "服务器读到消息对象:" + msg);
		return msg;
	}

	/**
	 * 发送一条消息对象给这个对象所代表的客户端用户
	 * 
	 * @param msg
	 *            :要发送的消息对象
	 * @return:是否发送成功
	 */
	public boolean sendMsgToClient(MsgHead msg) {
		try {
			byte[] data = MsgCreateTools.packMsg(msg);// 将消息对象打包为字节组
			this.dous.write(data);
			this.dous.flush();
			LogTools.INFO(this.getClass(), "服务器发出消息对象:" + msg);
			return true;
		} catch (Exception ef) {
			LogTools.ERROR(this.getClass(), "服务器发出消息出错:" + msg);
		}
		return false;
	}

	public int getLengthOfMsg(MsgHead msg) {
		int totalLen = -1;
		try {
			byte[] data = MsgCreateTools.packMsg(msg);
			totalLen = data.length;
			return totalLen;
		} catch (Exception ef) {
		}
		return totalLen;
	}

	// 取得这个线程对象代表的用户对象；
	public UserInfo getOwerUser() {
		return this.owerUser;
	}

	/**
	 * 断开连结这个处理线程与客户机的连结, 发生异常,或处理线程退出时调用
	 */
	public void disConn() {
		try {
			this.client.close();
		} catch (Exception ef) {
		}
	}
}
