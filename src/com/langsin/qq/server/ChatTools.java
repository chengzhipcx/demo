package com.langsin.qq.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.langsin.qq.dataBase.util.JdbcTest;
import com.langsin.qq.msg.IMsgConstance;
import com.langsin.qq.msg.MsgAddFriend;
import com.langsin.qq.msg.MsgAddFriendResp;
import com.langsin.qq.msg.MsgChat;
import com.langsin.qq.msg.MsgCreateGroup;
import com.langsin.qq.msg.MsgCreateGroupResp;
import com.langsin.qq.msg.MsgFile;
import com.langsin.qq.msg.MsgFileResp;
import com.langsin.qq.msg.MsgFindGroupResp;
import com.langsin.qq.msg.MsgFindUsersResp;
import com.langsin.qq.msg.MsgHead;
import com.langsin.qq.msg.MsgReg;
import com.langsin.qq.msg.MsgRegResp;

public class ChatTools {
	private ChatTools() {
	}// ����Ҫ�����������,��������˽��

	// ���洦���̵߳Ķ��ж���
	private static Map<Integer, ServerThread> stMap = new HashMap<Integer, ServerThread>();

	/**
	 * ���û���½�ɹ��󽫶�Ӧ�Ĵ����̶߳�����뵽������ ��������ѷ���������Ϣ
	 * @param ct
	 *            :�����̶߳���
	 */
	public static void addClient(int id, ServerThread ct) {
		stMap.put(id, ct);
		// ���������ߵ���Ϣ
		// sendOnOffLineMsg(user,true);////////////////////////////////////////
	}

	/**
	 * �û��˳�ϵͳ 1.�Ƴ���������еĴ����̶߳��� 2.������ѷ���������Ϣ
	 * @param user
	 *            :�˳��û�����
	 */
	public static void removeClient(int id) {

		ServerThread ct = stMap.remove(id);
		ct.disConn();
		ct = null;// �Ƴ�,��������û��Ĵ����̶߳���
		// sendOnOffLineMsg(user,false);///////////////////////////������ѷ����伺���ߵ���Ϣ
	}

	/**
	 * �������е�ĳһ���û�������Ϣ
	 * 
	 * @param srcUser
	 *            ��������
	 * @param msg
	 *            :��Ϣ����
	 */
	public synchronized static void sendMsgToOne(int id, MsgHead msg) {
		// 1.���Һ���
		if (msg.getType() == IMsgConstance.command_findUsers) {
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT ID,NikeName,HeadImgSrc FROM users WHERE ID=");
			Iterator<Entry<Integer, ServerThread>> it = stMap.entrySet()
					.iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, ServerThread> entry = (Map.Entry<Integer, ServerThread>) it
						.next();
				int num = entry.getKey();
				if (num != msg.getSrc()) {
					sb.append(num);
					sb.append(" OR ID=");
				}
			}
			if (sb.toString().length() == 50) {
				sb.delete(sb.length() - 9, sb.length());
			} else {
				sb.delete(sb.length() - 7, sb.length());
			}
			String sql = sb.toString();
			MsgFindUsersResp mfur = JdbcTest.findUsers(sql);
			mfur.setTotalLen(4 + 1 + 4 + 4);
			mfur.setType(IMsgConstance.command_findUsers_resp);
			mfur.setSrc(IMsgConstance.Server_QQ_NUMBER);
			mfur.setDest(msg.getSrc());
			int len = stMap.get(id).getLengthOfMsg(mfur);
			mfur.setTotalLen(len);
			stMap.get(msg.getSrc()).sendMsgToClient(mfur);
		} else if (msg.getType() == IMsgConstance.command_addFriend) {
			// 2.��Ӻ���,ֻ���,���������ҲҪ������Ϣ
			MsgAddFriend maf = (MsgAddFriend) msg;
			int fID = maf.getFID();
			String fTName = maf.getFTeamName();
			String sql1 = "SELECT FTeamID,FTeamName FROM usersfriends WHERE ID = "
					+ maf.getSrc() + " GROUP BY FTeamID";
			int TID = JdbcTest.addFriends1(sql1, fTName);
			String sql2 = "INSERT INTO usersfriends  VALUE(" + maf.getSrc()
					+ "," + fID + "," + TID + ",'" + fTName + "')";
			JdbcTest.addFriends2(sql2);

			if (maf.getDest() == maf.getFID()) {
				maf.setDest(fID);
				maf.setFID(maf.getSrc());
				maf.setFTeamName("");
				stMap.get(fID).sendMsgToClient(maf);
			} else if (maf.getDest() == IMsgConstance.Server_QQ_NUMBER) {
				MsgAddFriendResp mafr = new MsgAddFriendResp();
				int len = 4 + 1 + 4 + 4 + 16 + 4 + 16 + 24;
				mafr.setTotalLen(len);
				mafr.setDest(maf.getFID());
				mafr.setSrc(IMsgConstance.Server_QQ_NUMBER);
				mafr.setType(IMsgConstance.command_addFriend_Resp);
				mafr.setTeanName("");
				mafr.setUi(maf.getUi());
				stMap.get(maf.getFID()).sendMsgToClient(mafr);
			}

		} else if (msg.getType() == IMsgConstance.command_chat) {
			MsgChat mc = (MsgChat) msg;
			if (stMap.get(mc.getDest()) != null) {
				stMap.get(mc.getDest()).sendMsgToClient(mc);
			}

		} else if (msg.getType() == IMsgConstance.command_file) {
			MsgFile mf = (MsgFile) msg;
			if (stMap.get(mf.getDest()) != null) {
				stMap.get(mf.getDest()).sendMsgToClient(mf);
			}
		} else if (msg.getType() == IMsgConstance.command_file_resp) {
			MsgFileResp mfr = (MsgFileResp) msg;
			if (stMap.get(mfr.getDest()) != null) {
				stMap.get(mfr.getDest()).sendMsgToClient(mfr);
			}
		} else if (msg.getType() == IMsgConstance.command_prtsc) {
			MsgFile jietu = (MsgFile) msg;
			if (stMap.get(msg.getDest()) != null) {
				stMap.get(msg.getDest()).sendMsgToClient(jietu);
			}
		} else if (msg.getType() == IMsgConstance.command_winshake) {
			if (stMap.get(msg.getDest()) != null) {
				stMap.get(msg.getDest()).sendMsgToClient(msg);
			}
		} else if (msg.getType() == IMsgConstance.command_findGroup) {
			String sql = "SELECT groups.GID,GName,GMaster FROM groups JOIN groupsusers "
					+ "ON groups.GID = groupsusers.GID WHERE groupsusers.GID <>"
					+ "(SELECT GID FROM groupsusers WHERE UID="
					+ msg.getSrc()
					+ ")";
			MsgFindGroupResp mfgr = JdbcTest.findGroup(sql);
			mfgr.setTotalLen(4 + 1 + 4 + 4);
			mfgr.setType(IMsgConstance.command_findGroup_resp);
			mfgr.setSrc(IMsgConstance.Server_QQ_NUMBER);
			mfgr.setDest(msg.getSrc());
			if (stMap.get(mfgr.getDest()) != null) {
				int len = stMap.get(mfgr.getDest()).getLengthOfMsg(mfgr);
				mfgr.setTotalLen(len);
				stMap.get(mfgr.getDest()).sendMsgToClient(mfgr);
			}
		} else if (msg.getType() == IMsgConstance.command_winshake) {
			if (stMap.get(msg.getDest()) != null) {
				stMap.get(msg.getDest()).sendMsgToClient(msg);
			}
		} else if (msg.getType() == IMsgConstance.command_createGroup) {
			int baseID = 1000;
			MsgCreateGroup mcg = (MsgCreateGroup) msg;
			String base = "SELECT ID FROM GROUPS ORDER BY ID DESC LIMIT 1";
			if (base != null) {
				baseID = JdbcTest.getJdbcTest().getBaseID(base);
			}
			baseID++;
			String sql = "insert into groups(GID,GName,GMaster) values('"
					+ String.valueOf(baseID) + "','" + mcg.getGName() + "','"
					+ String.valueOf(mcg.getSrc()) + "')";
			JdbcTest.getJdbcTest().reg(sql);

			String add = "insert into groupsusers(GID,UID) values('"
					+ String.valueOf(baseID) + "','"
					+ String.valueOf(mcg.getSrc()) + "')";
			JdbcTest.getJdbcTest().reg(add);
			/**
			 * ����һ�� ����Ⱥ��Ӧ�����
			 */
			MsgCreateGroupResp mcgr = new MsgCreateGroupResp();
			mcgr.setTotalLen(4 + 1 + 4 + 4 + 1);
			mcgr.setType(IMsgConstance.command_createGroup_resp);
			mcgr.setDest(mcg.getSrc());
			mcgr.setSrc(IMsgConstance.Server_QQ_NUMBER);
			mcgr.setGID(baseID);
			// ����ע��Ӧ����Ϣ
			if (stMap.get(mcgr.getDest()) != null) {
				stMap.get(mcgr.getDest()).sendMsgToClient(mcgr);
			}

		}
	}

	/**
	 * ȥ��String�����\0
	 */
	// private static String removeSpace(String str) {
	// char[] s = str.toCharArray();
	// StringBuilder sb = new StringBuilder();
	// for (int i = 0; i < s.length; i++) {
	// if (s[i] == '\0') {
	// break;
	// }
	// sb.append(s[i]);
	// }
	// return sb.toString();
	// }

}
