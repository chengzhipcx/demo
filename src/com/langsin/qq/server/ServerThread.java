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

	private Socket client; // �߳��д���Ŀͻ�����
	private DataOutputStream dous; // ���������
	private DataInputStream dins; // ����������
	private UserInfo owerUser; // ����̴߳�����������û�����
	private boolean loginOK = false;
	private static int baseID = 100000;// ����QQ����Ļ���

	// ����ʱ���봫��һ��Socket���󣬼���������ͻ������������
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
	 * ����ɹ���,������Ҫ��ȡ��һ����Ϣ,��ServerMain���� �����ǵ�½��Ϣ,Ҳ������ע������
	 * 
	 * @return:�Ƿ��ȡ�ɹ�
	 */
	public boolean readFirstMsg() {
		MsgHead msg = null;
		try {
			// ��ȡ��һ����Ϣ
			msg = reciveData();
			// �����ע������
			if (msg.getType() == IMsgConstance.command_reg) {
				reg(msg);
				return false;
			}
			// ����ǵ�½����
			if (msg.getType() == IMsgConstance.command_login) {
				return checkLogin(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * ע��
	 */

	private void reg(MsgHead msg) {
		MsgReg mr = (MsgReg) msg;
		/**
		 * ����ע��,����QQ��,�ظ�Ӧ����Ϣ
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
		 * ����һ��ע��Ӧ�����
		 */
		MsgRegResp mrs = new MsgRegResp();
		mrs.setTotalLen(4 + 1 + 4 + 4 + 1);
		mrs.setType(IMsgConstance.command_reg_resp);
		mrs.setDest(baseID);
		mrs.setSrc(IMsgConstance.Server_QQ_NUMBER);
		mrs.setState((byte) 0);
		// ����ע��Ӧ����Ϣ
		this.sendMsgToClient(mrs);
	}

	/**
	 * ����½�Ƿ�ɹ�, ���ҷ��غ����б���Ϣ��
	 * 
	 * @throws IOException
	 * */
	private boolean checkLogin(MsgHead msg) throws IOException {
		MsgLogin ml = (MsgLogin) msg;
		// 1�������ݿ���֤�Ƿ��½�ɳɹ�
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
		 * 2����½�ɹ�����Ӧ�������½Ӧ����Ϣ�ͺ��ѷ����б���Ϣ
		 */
		if (loginReg.getPwd() != null) {
			//һ�� ��½�ɹ�:1.��Ӧ�����2.����������̶߳���ӵ�������
			MsgLoginResp mlr = new MsgLoginResp();
			mlr.setTotalLen(4 + 1 + 4 + 4 + 1 + 16 + 24);
			mlr.setType(IMsgConstance.command_login_resp);
			mlr.setSrc(IMsgConstance.Server_QQ_NUMBER);
			mlr.setDest(loginReg.getID());// �����ߺ���
			mlr.setState((byte) 0);
			mlr.setNikeName(loginReg.getNikeName());
			mlr.setHeadImgSrc(loginReg.getHeadImgSrc());
			this.sendMsgToClient(mlr);// ����Ӧ����Ϣ
			owerUser = new UserInfo(loginReg.getID());
			
			//���������ݿ��� ���ѷ����б���󣬲��ҷ��͸��ͻ���
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
			
			//���� �����ݿ��� ��Ⱥ�б���󣬲��ҷ��͸��ͻ���
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
			mlr.setDest(loginReg.getID());// �����ߺ���
			mlr.setState((byte) -1);
			this.sendMsgToClient(mlr);// ����Ӧ����Ϣ
		}
		this.disConn();// ��½ʧ��,ֱ�ӶϿ�
		return false;
	}

	// �߳���ִ�н�����Ϣ�ķ���
	public void run() {
		try {
			loginOK = readFirstMsg();
			if (loginOK) {
				// �����½�ɹ�������������̶߳�����뵽������
				ChatTools.addClient(owerUser.getID(), this);
			}
			while (loginOK) {
				MsgHead msg = this.reciveData();// ѭ��������Ϣ
				ChatTools.sendMsgToOne(owerUser.getID(), msg);// �ַ�������Ϣ
			}
		} catch (Exception ef) {
			LogTools.ERROR(this.getClass(), "����������Ϣ����(�û�������):" + ef);
		}
		// �û�������,�Ӷ������Ƴ�����û���Ӧ�Ĵ����̶߳���
		ChatTools.removeClient(owerUser.getID());
	}

	/**
	 * ���������϶�ȡ���ݿ�,���Ϊ��Ϣ����
	 * 
	 * @return:�����������ݿ����Ϊ��Ϣ����
	 */
	private MsgHead reciveData() throws Exception {
		int len = dins.readInt(); // ��ȡ��Ϣ����
		LogTools.INFO(this.getClass(), "����������Ϣ����:" + len);
		byte[] data = new byte[len - 4];
		dins.readFully(data);
		MsgHead msg = MsgParseTools.parseMsg(data);// ����Ϊ��Ϣ����
		LogTools.INFO(this.getClass(), "������������Ϣ����:" + msg);
		return msg;
	}

	/**
	 * ����һ����Ϣ������������������Ŀͻ����û�
	 * 
	 * @param msg
	 *            :Ҫ���͵���Ϣ����
	 * @return:�Ƿ��ͳɹ�
	 */
	public boolean sendMsgToClient(MsgHead msg) {
		try {
			byte[] data = MsgCreateTools.packMsg(msg);// ����Ϣ������Ϊ�ֽ���
			this.dous.write(data);
			this.dous.flush();
			LogTools.INFO(this.getClass(), "������������Ϣ����:" + msg);
			return true;
		} catch (Exception ef) {
			LogTools.ERROR(this.getClass(), "������������Ϣ����:" + msg);
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

	// ȡ������̶߳��������û�����
	public UserInfo getOwerUser() {
		return this.owerUser;
	}

	/**
	 * �Ͽ�������������߳���ͻ���������, �����쳣,�����߳��˳�ʱ����
	 */
	public void disConn() {
		try {
			this.client.close();
		} catch (Exception ef) {
		}
	}
}
