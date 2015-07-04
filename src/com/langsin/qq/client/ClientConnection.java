package com.langsin.qq.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.langsin.qq.model.MsgCreateTools;
import com.langsin.qq.model.MsgParseTools;
import com.langsin.qq.model.UserInfo;
import com.langsin.qq.msg.IMsgConstance;
import com.langsin.qq.msg.MsgAddFriend;
import com.langsin.qq.msg.MsgChat;
import com.langsin.qq.msg.MsgCreateGroup;
import com.langsin.qq.msg.MsgFile;
import com.langsin.qq.msg.MsgFileResp;
import com.langsin.qq.msg.MsgGroupList;
import com.langsin.qq.msg.MsgHead;
import com.langsin.qq.msg.MsgLogin;
import com.langsin.qq.msg.MsgLoginResp;
import com.langsin.qq.msg.MsgReg;
import com.langsin.qq.msg.MsgRegResp;
import com.langsin.qq.msg.MsgTeamList;
import com.langsin.qq.util.LogTools;

/**
 * 即时通信系统 客户端的通信模块,提供： 1.登陆，注册接口调用； 2.在独立线程中接收服务器消息 3.将接收到的消息分发给监听器对象
 */
public class ClientConnection extends Thread {

	private static ClientConnection ins;// 本类单实例对象
	private Socket client; // 与服务器的连结对象
	private DataOutputStream dous;// 输出流对象
	private DataInputStream dins;// 输入流对象

	private Map<Integer, MyClientMsgListener> listeners = new HashMap<Integer, MyClientMsgListener>();

	// 不需要创建对象,所以构造器私有
	private ClientConnection() {
	}

	// 单实例对象访问方法
	public static ClientConnection getIns() {
		if (null == ins) {
			ins = new ClientConnection();
		}
		return ins;
	}

	// 连结上服务器,是否连结成功
	public boolean connToServer() {
		try {
			// 1.创建一个到服务器端的Socket对象
			client = new Socket(IMsgConstance.serverIP,
					IMsgConstance.serverPort);
			// 2.得到输入输出流对象
			InputStream ins = client.getInputStream();
			// 3.包装为可读写原始数据类型的输入输出流
			this.dous = new DataOutputStream(client.getOutputStream());
			this.dins = new DataInputStream(client.getInputStream());
			return true;
		} catch (Exception ef) {
			ef.printStackTrace();
		}
		return false;
	}

	/**
	 * 注册
	 * 
	 * @param nikeName
	 *            :用户呢称
	 * @param pwd
	 *            :密码
	 * @return: 注册结果 -1:失败 其它:注册到的im号
	 */
	public int regServer(String nikeName, String pwd, String headImgSrc) {
		try {
			MsgReg mrg = new MsgReg();
			mrg.setTotalLen(4 + 1 + 4 + 4 + 12 + 16 + 24);
			mrg.setType(IMsgConstance.command_reg);
			mrg.setDest(IMsgConstance.Server_QQ_NUMBER);
			mrg.setSrc(0);
			mrg.setPwd(pwd);
			mrg.setNikeName(nikeName);
			mrg.setHeadImg(headImgSrc);
			this.sendMsg(mrg);
			// 发送了注册请求之后,必须读到一条应答的消息
			MsgHead loginResp = readFromServer();
			MsgRegResp mr = (MsgRegResp) loginResp;
			if (mr.getState() == 0) {// 注册成功!
				return mr.getDest();
			}
		} catch (Exception ef) {
			ef.printStackTrace();
		}
		return -1;
	}

	/**
	 * 登陆服务器
	 * 
	 * @param imNum
	 *            :用户imNum
	 * @param pwd
	 *            :密码
	 * @return: 是否登陆成功
	 */
	public MsgLoginResp loginServer(int imNum, String pwd) {
		MsgLoginResp mlr = null;
		try {
			MsgLogin ml = new MsgLogin();
			ml.setTotalLen(4 + 1 + 4 + 4 + 12);
			ml.setType(IMsgConstance.command_login);
			ml.setDest(IMsgConstance.Server_QQ_NUMBER);
			ml.setSrc(imNum);
			ml.setPwd(pwd);
			this.sendMsg(ml);
			// 发送了登陆请求之后,必须读到一条应答的消息
			mlr = (MsgLoginResp) readFromServer();
			return mlr;

		} catch (Exception ef) {
			ef.printStackTrace();
		}
		return mlr;

	}

	/**
	 * 获取好友分组列表
	 */
	public MsgTeamList getTeamList() {
		MsgTeamList mtl = null;
		try {
			mtl = (MsgTeamList) readFromServer();
			return mtl;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mtl;
	}

	/**
	 * 获取组群列表
	 */
	public MsgGroupList getGroupList() {
		MsgGroupList mgl = null;
		try {
			mgl = (MsgGroupList) readFromServer();
			return mgl;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mgl;
	}

	/**
	 * 发送查找好友消息
	 */
	public void findUser(int src) {
		try {
			MsgHead mh = new MsgHead();
			mh.setTotalLen(4 + 1 + 4 + 4);
			mh.setType(IMsgConstance.command_findUsers);
			mh.setSrc(src);
			mh.setDest(IMsgConstance.Server_QQ_NUMBER);
			this.sendMsg(mh);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 添加好友
	 */
	public void addUser(int src, int fid, UserInfo iUi, String fTeamName, int a) {
		try {
			MsgAddFriend maf = new MsgAddFriend();
			maf.setTotalLen(4 + 1 + 4 + 4 + 4 + 16 + 4 + 16 + 24);
			maf.setType(IMsgConstance.command_addFriend);
			maf.setSrc(src);
			if (a == 0) {
				maf.setDest(fid);
			} else if (a == 1) {
				maf.setDest(IMsgConstance.Server_QQ_NUMBER);
			}
			maf.setFID(fid);
			maf.setFTeamName(fTeamName);
			maf.setUi(iUi);
			this.sendMsg(maf);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 发送聊天消息
	 */
	public void sendTest(int src, int dest, String text) {
		MsgChat mc = null;
		try {
			mc = new MsgChat();
			mc.setTotalLen(4 + 1 + 4 + 4);
			mc.setType(IMsgConstance.command_chat);
			mc.setDest(dest);
			mc.setSrc(src);
			mc.setMsgContent(text);
			int totalLen = getLengthOfMsg(mc);
			mc.setTotalLen(totalLen);
			sendMsg(mc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送文件消息
	 */
	public void sendFile(int src, int dest, String fileName, byte[] fileData) {
		MsgFile mf = null;
		try {
			mf = new MsgFile();
			mf.setTotalLen(4 + 1 + 4 + 4);
			mf.setType(IMsgConstance.command_file);
			mf.setDest(dest);
			mf.setSrc(src);
			mf.setFileName(fileName);
			mf.setFileData(fileData);
			int totalLen = getLengthOfMsg(mf);
			mf.setTotalLen(totalLen);
			sendMsg(mf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送截图
	 */
	public void sendjietu(int src, int dest, String fileName) {
		MsgFile mf = null;
		try {
			mf = new MsgFile();
			mf.setTotalLen(4 + 1 + 4 + 4 + 50);
			mf.setType(IMsgConstance.command_prtsc);
			mf.setDest(dest);
			mf.setSrc(src);
			mf.setFileName(fileName);
			sendMsg(mf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建群组
	 */
	public void createGroup(int src, String gName) {

		MsgCreateGroup mcg = null;
		try {
			mcg = new MsgCreateGroup();
			mcg.setTotalLen(4 + 1 + 4 + 4);
			mcg.setType(IMsgConstance.command_createGroup);
			mcg.setSrc(src);
			mcg.setDest(IMsgConstance.Server_QQ_NUMBER);
			mcg.setGName(gName);
			int totalLen = getLengthOfMsg(mcg);
			mcg.setTotalLen(totalLen);
			sendMsg(mcg);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 线程中中读取服务器发来的消息，并分发给监听器
	public void run() {
		while (true) {
			try {
				// 接收一条消息
				MsgHead msg = readFromServer();
				// 将消息分发给监听器去处理
				listeners.get(msg.getDest()).dispatchMessage(msg);

			} catch (Exception ef) {
				ef.printStackTrace();
				break; // 如果读取出错,则退出
			}
		}
		LogTools.INFO(this.getClass(), "客户端接收线程己退出!");
	}

	/**
	 * 从输入流上读取一条服务器端发来的消息 这个方法会阻塞，必须在独立线程中
	 * 
	 * @return:读取到的消息对象
	 */
	public MsgHead readFromServer() throws Exception {
		int totalLen = dins.readInt();
		LogTools.INFO(this.getClass(), "客户端读到消息总长为:" + totalLen);
		byte[] data = new byte[totalLen - 4];
		dins.readFully(data); // 读取数据块
		MsgHead msg = MsgParseTools.parseMsg(data);// 解包为消息对象
		LogTools.INFO(this.getClass(), "客户端收到消息:" + msg);
		return msg;
	}

	/** 发送一条消息到服务器的方法 */
	public void sendMsg(MsgHead msg) throws Exception {
		LogTools.INFO(this.getClass(), "客户端发出消息:" + msg);
		byte[] data = MsgCreateTools.packMsg(msg);// 打包对象为数据块
		this.dous.write(data);// 发送
		this.dous.flush();
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

	/**
	 * 为连结对象加入一个消息处理监听器对象
	 * 
	 * @param my
	 *            :消息处理监听器对象
	 */
	public void addMsgListener(int qqNum, MyClientMsgListener my) {
		this.listeners.put(qqNum, my);
	}

	// 关闭与一个客户机的连结
	public void closeMe() {
		try {
			this.client.close();
		} catch (Exception ef) {
		}
	}
}
