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
 * ��ʱͨ��ϵͳ �ͻ��˵�ͨ��ģ��,�ṩ�� 1.��½��ע��ӿڵ��ã� 2.�ڶ����߳��н��շ�������Ϣ 3.�����յ�����Ϣ�ַ�������������
 */
public class ClientConnection extends Thread {

	private static ClientConnection ins;// ���൥ʵ������
	private Socket client; // ����������������
	private DataOutputStream dous;// ���������
	private DataInputStream dins;// ����������

	private Map<Integer, MyClientMsgListener> listeners = new HashMap<Integer, MyClientMsgListener>();

	// ����Ҫ��������,���Թ�����˽��
	private ClientConnection() {
	}

	// ��ʵ��������ʷ���
	public static ClientConnection getIns() {
		if (null == ins) {
			ins = new ClientConnection();
		}
		return ins;
	}

	// �����Ϸ�����,�Ƿ�����ɹ�
	public boolean connToServer() {
		try {
			// 1.����һ�����������˵�Socket����
			client = new Socket(IMsgConstance.serverIP,
					IMsgConstance.serverPort);
			// 2.�õ��������������
			InputStream ins = client.getInputStream();
			// 3.��װΪ�ɶ�дԭʼ�������͵����������
			this.dous = new DataOutputStream(client.getOutputStream());
			this.dins = new DataInputStream(client.getInputStream());
			return true;
		} catch (Exception ef) {
			ef.printStackTrace();
		}
		return false;
	}

	/**
	 * ע��
	 * 
	 * @param nikeName
	 *            :�û��س�
	 * @param pwd
	 *            :����
	 * @return: ע���� -1:ʧ�� ����:ע�ᵽ��im��
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
			// ������ע������֮��,�������һ��Ӧ�����Ϣ
			MsgHead loginResp = readFromServer();
			MsgRegResp mr = (MsgRegResp) loginResp;
			if (mr.getState() == 0) {// ע��ɹ�!
				return mr.getDest();
			}
		} catch (Exception ef) {
			ef.printStackTrace();
		}
		return -1;
	}

	/**
	 * ��½������
	 * 
	 * @param imNum
	 *            :�û�imNum
	 * @param pwd
	 *            :����
	 * @return: �Ƿ��½�ɹ�
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
			// �����˵�½����֮��,�������һ��Ӧ�����Ϣ
			mlr = (MsgLoginResp) readFromServer();
			return mlr;

		} catch (Exception ef) {
			ef.printStackTrace();
		}
		return mlr;

	}

	/**
	 * ��ȡ���ѷ����б�
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
	 * ��ȡ��Ⱥ�б�
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
	 * ���Ͳ��Һ�����Ϣ
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
	 * ��Ӻ���
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
	 * ����������Ϣ
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
	 * �����ļ���Ϣ
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
	 * ���ͽ�ͼ
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
	 * ����Ⱥ��
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

	// �߳����ж�ȡ��������������Ϣ�����ַ���������
	public void run() {
		while (true) {
			try {
				// ����һ����Ϣ
				MsgHead msg = readFromServer();
				// ����Ϣ�ַ���������ȥ����
				listeners.get(msg.getDest()).dispatchMessage(msg);

			} catch (Exception ef) {
				ef.printStackTrace();
				break; // �����ȡ����,���˳�
			}
		}
		LogTools.INFO(this.getClass(), "�ͻ��˽����̼߳��˳�!");
	}

	/**
	 * ���������϶�ȡһ���������˷�������Ϣ ��������������������ڶ����߳���
	 * 
	 * @return:��ȡ������Ϣ����
	 */
	public MsgHead readFromServer() throws Exception {
		int totalLen = dins.readInt();
		LogTools.INFO(this.getClass(), "�ͻ��˶�����Ϣ�ܳ�Ϊ:" + totalLen);
		byte[] data = new byte[totalLen - 4];
		dins.readFully(data); // ��ȡ���ݿ�
		MsgHead msg = MsgParseTools.parseMsg(data);// ���Ϊ��Ϣ����
		LogTools.INFO(this.getClass(), "�ͻ����յ���Ϣ:" + msg);
		return msg;
	}

	/** ����һ����Ϣ���������ķ��� */
	public void sendMsg(MsgHead msg) throws Exception {
		LogTools.INFO(this.getClass(), "�ͻ��˷�����Ϣ:" + msg);
		byte[] data = MsgCreateTools.packMsg(msg);// �������Ϊ���ݿ�
		this.dous.write(data);// ����
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
	 * Ϊ����������һ����Ϣ�������������
	 * 
	 * @param my
	 *            :��Ϣ�������������
	 */
	public void addMsgListener(int qqNum, MyClientMsgListener my) {
		this.listeners.put(qqNum, my);
	}

	// �ر���һ���ͻ���������
	public void closeMe() {
		try {
			this.client.close();
		} catch (Exception ef) {
		}
	}
}
