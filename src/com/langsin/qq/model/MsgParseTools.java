package com.langsin.qq.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import com.langsin.qq.msg.MsgGroupList;
import com.langsin.qq.msg.MsgHead;
import com.langsin.qq.msg.MsgLogin;
import com.langsin.qq.msg.MsgLoginResp;
import com.langsin.qq.msg.MsgReg;
import com.langsin.qq.msg.MsgRegResp;
import com.langsin.qq.msg.MsgTeamList;

/**
 * ��Ϣ����������
 * 
 * 
 */
public class MsgParseTools {
	/**
	 * �������϶��������ݿ飬���Ϊ��Ϣ����
	 * 
	 * @param data
	 *            :���������ݿ�
	 * @return:��������Ϣ����
	 */
	public static MsgHead parseMsg(byte[] data) throws Exception {

		/**
		 * ת��Ϊ�ڴ���
		 */
		ByteArrayInputStream bins = new java.io.ByteArrayInputStream(data);
		DataInputStream dins = new java.io.DataInputStream(bins);

		int totalLen = data.length + 4; // �����Ϣ�ܳ�
		byte msgType = dins.readByte(); // ��ȡ��Ϣ����
		int dest = dins.readInt(); // ��ȡĿ��QQ��
		int src = dins.readInt(); // ��ȡԴQQ��

		/**
		 * ���½���Ϣͷ���ݸ�ֵ
		 */
		MsgHead msgHead = new MsgHead();
		msgHead.setTotalLen(totalLen);
		msgHead.setType(msgType);
		msgHead.setDest(dest);
		msgHead.setSrc(src);

		/**
		 * ���´�������
		 * 
		 * 0x01:ע������
		 */
		if (msgType == IMsgConstance.command_reg) {
			MsgReg mr = new MsgReg();
			copyHead(msgHead, mr);
			String nikeName = readString(dins, 16);
			mr.setNikeName(nikeName);
			String pwd = readString(dins, 12);
			mr.setPwd(pwd);
			String headImgSrc = readString(dins, 24);
			mr.setHeadImg(headImgSrc);
			return mr;
		}

		/**
		 * 0x02:ע��Ӧ��
		 */
		else if (msgType == IMsgConstance.command_reg_resp) {
			MsgRegResp mrr = new MsgRegResp();
			copyHead(msgHead, mrr);
			mrr.setState(dins.readByte());
			return mrr;
		}

		/**
		 * 0x03:��½����
		 */
		else if (msgType == IMsgConstance.command_login) {
			String pwd = readString(dins, 12);
			MsgLogin ml = new MsgLogin();
			copyHead(msgHead, ml); // ������Ϣͷ����
			ml.setPwd(pwd);
			return ml;
		}

		/**
		 * 0x04:��½Ӧ��
		 */
		else if (msgType == IMsgConstance.command_login_resp) {
			byte state = dins.readByte(); // ��ȡ״̬�ֶΣ�һ���ֽ�
			String nikeName = readString(dins, 16);
			String headImgSrc = readString(dins, 24);
			MsgLoginResp ml = new MsgLoginResp();
			copyHead(msgHead, ml); // ������Ϣͷ����
			ml.setState(state);
			ml.setNikeName(nikeName);
			ml.setHeadImgSrc(headImgSrc);
			return ml;
		}

		/**
		 * 0x05:�����б����ݰ�
		 */
		else if (msgType == IMsgConstance.command_teamList) {
			MsgTeamList mtl = new MsgTeamList();
			copyHead(msgHead, mtl);
			int listCount = dins.readInt(); // ��ʶ�м������������
			List<TeamInfo> teamLists = new ArrayList<TeamInfo>();
			while (listCount > 0) {
				listCount--;
				String teamName = readString(dins, 16); // ��ȡһ����������
				TeamInfo team = new TeamInfo(teamName); // ����һ���������
				byte UserCount = dins.readByte(); // �����м����û�
				while (UserCount > 0) {
					UserCount--;
					int FID = dins.readInt(); // ��ȡһ���û���QQ��
					String nikeName = readString(dins, 16); // ��ȡ����û����س�
					String headImgSrc = readString(dins, 24); // ��ȡ����û���headImgSrc
					UserInfo ui = new UserInfo(nikeName, FID);
					ui.setHeadImgSrc(headImgSrc);
					team.addUsers(ui);
				}
				teamLists.add(team);
			}
			mtl.setTeamLists(teamLists);
			return mtl;
		}

		/**
		 * 0x06,0x07:����������(����Ϣ��)
		 */
		else if (msgType == IMsgConstance.command_offLine
				|| msgType == IMsgConstance.command_onLine) {
			return msgHead;
		}

		/**
		 * 0x08:������Ϣ��
		 */
		else if (msgType == IMsgConstance.command_chat) {
			int len = totalLen - 4 - 1 - 4 - 4; // �������������ֽڵĳ���
			String content = readString(dins, len);// ��ȡ���������ַ���
			MsgChat mc = new MsgChat();
			copyHead(msgHead, mc);
			mc.setMsgContent(content);
			return mc;
		}

		/**
		 * 0x09:�ļ����ݰ�
		 */
		else if (msgType == IMsgConstance.command_file) {
			String fileName = readString(dins, 256); // ��ȡ�ļ����ݰ�
			int fileLen = totalLen - 4 - 1 - 4 - 4 - 256; // �����ļ������ֽڵĳ���
			MsgFile mf = new MsgFile();
			copyHead(msgHead, mf);
			mf.setFileName(fileName);
			byte[] fileData = new byte[fileLen];
			dins.readFully(fileData);
			mf.setFileData(fileData);
			return mf;
		}

		/**
		 * 0x11:�ļ�����Ӧ��
		 */
		else if (msgType == IMsgConstance.command_file_resp) {
			byte state = dins.readByte(); // ��ȡ״̬�ֶΣ�һ���ֽ�
			MsgFileResp mfr = new MsgFileResp();
			copyHead(msgHead, mfr); // ������Ϣͷ����
			mfr.setState(state);
			return mfr;
		}
		/**
		 * 0x12:��ͼ
		 */
		else if (msgType == IMsgConstance.command_prtsc) {
			String fileName = readString(dins, 50); // ��ȡ�ļ����ݰ�
			MsgFile mf = new MsgFile();
			copyHead(msgHead, mf);
			mf.setFileName(fileName);
			return mf;
		}

		/**
		 * 0x13:���ڶ���(����Ϣ��)
		 */
		else if (msgType == IMsgConstance.command_winshake) {
			return msgHead;
		}
		
		/**
		 * 0x14������Ⱥ����
		 */
		else if (msgType == IMsgConstance.command_createGroup) {
			int len = totalLen - 4 - 1 - 4 - 4;
			String gName = readString(dins, len);
			MsgCreateGroup mcg = new MsgCreateGroup();
			copyHead(msgHead, mcg);
			mcg.setGName(gName);
			return mcg;
		}
		
		/**
		 * 0x15������ȺӦ��
		 */
		else if (msgType == IMsgConstance.command_createGroup_resp) {
			int gid = dins.readInt();
			MsgCreateGroupResp mcgr = new MsgCreateGroupResp();
			copyHead(msgHead, mcgr); // ������Ϣͷ����
			mcgr.setGID(gid);
			return mcgr;
		}
		// /**
		// * 0x14,0x15:Զ��������Ϣ����,Զ������Ӧ��(����Ϣ��)
		// */
		// else if(msgType==IMsgConstance.command_remoteControl||
		// msgType==IMsgConstance.command_remoteControl_resp){
		// return msgHead;
		// }

		/**
		 * 0x16:���Һ���(����Ϣ��)
		 */
		else if (msgType == IMsgConstance.command_findUsers) {
			return msgHead;
		}

		/**
		 * 0x17:���Һ���Ӧ��
		 */
		else if (msgType == IMsgConstance.command_findUsers_resp) {
			MsgFindUsersResp mfur = new MsgFindUsersResp();
			copyHead(msgHead, mfur);
			int userCount = dins.readInt();
			for (int i = 0; i < userCount; i++) {
				String nikeName = readString(dins, 16);
				String headImgSrc = readString(dins, 24);
				int FID = dins.readInt();
				UserInfo user = new UserInfo(nikeName, FID);
				user.setHeadImgSrc(headImgSrc);
				mfur.addUserInfo(user);
			}
			return mfur;
		}

		/**
		 * 0x18:��Ӻ���
		 */
		else if (msgType == IMsgConstance.command_addFriend) {
			MsgAddFriend maf = new MsgAddFriend();
			copyHead(msgHead, maf);

			int FID = dins.readInt(); // Ҫ����ĺ��ѵ�QQ����
			String fTeamName = readString(dins, 16);
			int getId = dins.readInt();
			String nikeName = readString(dins, 16);
			String headImgSrc = readString(dins, 24);

			UserInfo ui = new UserInfo(nikeName, getId);
			ui.setHeadImgSrc(headImgSrc);

			maf.setFID(FID);
			maf.setFTeamName(fTeamName);
			maf.setUi(ui);
			return maf;
		}

		/**
		 * 0x19:��Ӻ��ѵ�Ӧ��
		 */
		else if (msgType == IMsgConstance.command_addFriend_Resp) {
			MsgAddFriendResp mafr = new MsgAddFriendResp();
			copyHead(msgHead, mafr);
			String fTeamName = readString(dins, 16);
			int getId = dins.readInt();
			String nikeName = readString(dins, 16);
			String headImgSrc = readString(dins, 24);
			UserInfo ui = new UserInfo(nikeName, getId);
			ui.setHeadImgSrc(headImgSrc);
			mafr.setTeanName(fTeamName);
			mafr.setUi(ui);
			return mafr;
		}
		
		/**
		 * 0x21:����������Ⱥ���б�
		 */
		else if (msgType == IMsgConstance.command_groupList) {

			MsgGroupList mgl = new MsgGroupList();
			copyHead(msgHead, mgl);
			int listCount = dins.readInt(); // ��ʶ�м���Ⱥ��
			List<GroupInfo> groupLists = new ArrayList<GroupInfo>();
			while (listCount > 0) {
				listCount--;
				int groupID = dins.readInt();
				String groupName = readString(dins, 16); // ��ȡһ����Ⱥ����
				int gMaster = dins.readInt();
				GroupInfo group = new GroupInfo(); // ����һ���������
				group.setGID(groupID);
				group.setGName(groupName);
				group.setGMaster(gMaster);
				byte UserCount = dins.readByte(); // �����м����û�
				while (UserCount > 0) {
					UserCount--;
					int FID = dins.readInt(); // ��ȡһ���û���QQ��
					String nikeName = readString(dins, 16); // ��ȡ����û����س�
					String headImgSrc = readString(dins, 24); // ��ȡ����û���headImgSrc
					UserInfo ui = new UserInfo(nikeName, FID);
					ui.setHeadImgSrc(headImgSrc);
					group.addUsers(ui);
				}
				groupLists.add(group);
			}
			mgl.setGroupList(groupLists);
			return mgl;
		} 
		/**
		 * 0x22:������Ⱥ
		 */
		else if (msgType == IMsgConstance.command_findGroup) {
			return msgHead;
		}
		/**
		 * 0x23:������ȺӦ��
		 */
		else if (msgType == IMsgConstance.command_findGroup_resp) {
			MsgFindGroupResp mfgr = new MsgFindGroupResp();
			copyHead(msgHead, mfgr);
			int listCount = dins.readInt(); // ��ʶ�м���Ⱥ��
			List<GroupInfo> groupLists = new ArrayList<GroupInfo>();
			while (listCount > 0) {
				listCount--;
				int groupID = dins.readInt();
				String groupName = readString(dins, 16); // ��ȡһ����Ⱥ����
				int gMaster = dins.readInt();
				GroupInfo group = new GroupInfo(); // ����һ���������
				group.setGID(groupID);
				group.setGName(groupName);
				group.setGMaster(gMaster);
				groupLists.add(group);
			}
			mfgr.setGroups(groupLists);
			return mfgr;
		}
		
		else {
			// String logMsg="���δ֪��Ϣ���ͣ��޷����:type:"+msgType;
			// LogTools.ERROR(ToolsParseMsg.class, logMsg);//��¼��־
			System.out.println("���δ֪��Ϣ���ͣ��޷����:type:" + msgType);
		}
		return null;
	}

	/**
	 * ������Ϣͷ������:
	 * 
	 * @param head
	 *            :��Ϣͷ
	 * @param dest
	 *            :Ҫ���Ƶ���Ŀ����Ϣ����
	 */
	private static void copyHead(MsgHead head, MsgHead dest) {
		dest.setTotalLen(head.getTotalLen());
		dest.setType(head.getType());
		dest.setDest(head.getDest());
		dest.setSrc(head.getSrc());

	}

	/**
	 * �����ж�ȡlen���ȸ��ֽڣ�����Ϊ�ַ�������
	 * 
	 * @param dins
	 *            :Ҫ��ȡ��������
	 * @param len
	 *            :��ȡ���ֽڳ���
	 * @return:�������ַ���
	 */
	private static String readString(DataInputStream dins, int len) {
		byte[] data = new byte[len];
		try {
			dins.readFully(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String(data);// ʹ��ϵͳĬ���ַ�������
	}

	/**
	 * ȥ��String�����\0
	 */
	private static String removeSpace(String str) {
		char[] s = str.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length; i++) {
			if (s[i] == '\0') {
				break;
			}
			sb.append(s[i]);
		}
		return sb.toString();
	}

}
