package com.langsin.qq.model;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import com.langsin.qq.msg.*;

/**
 * ��Ϣ����������
 * 
 * 
 */
public class MsgCreateTools {

	/**
	 * ����Ϣ������Ϊ�ֽ����鷵�� ����Ϣд���ڴ�IO��
	 * 
	 * @param msg
	 *            :Ҫ�������Ϣ����
	 */

	public static byte[] packMsg(MsgHead msg) throws IOException {
		// �����ڴ������
		ByteArrayOutputStream bous = new ByteArrayOutputStream();
		DataOutputStream dous = new DataOutputStream(bous);

		writeHead(dous, msg); // ��д����Ϣͷ,������Ϣͷ�ṹ��ͬ
		int msgType = msg.getType(); // ȡ����Ϣ���ͱ�ʶ

		/**
		 * 0x01:ע������
		 */
		if (msgType == IMsgConstance.command_reg) {
			MsgReg mr = (MsgReg) msg;
			writeString(dous, 16, mr.getNikeName());
			writeString(dous, 12, mr.getPwd());
			writeString(dous, 24, mr.getHeadImg());
		}

		/**
		 * 0x02:ע��Ӧ��
		 */
		else if (msgType == IMsgConstance.command_reg_resp) {
			MsgRegResp mrr = (MsgRegResp) msg;
			dous.writeByte(mrr.getState());
		}

		/**
		 * 0x03:��½����
		 */
		else if (msgType == IMsgConstance.command_login) {
			MsgLogin ml = (MsgLogin) msg;
			writeString(dous, 12, ml.getPwd());
		}

		/**
		 * 0x04:��½Ӧ��
		 */
		else if (msgType == IMsgConstance.command_login_resp) {
			MsgLoginResp mlr = (MsgLoginResp) msg;
			dous.writeByte(mlr.getState());
			writeString(dous, 16, mlr.getNikeName());
			writeString(dous, 24, mlr.getHeadImgSrc());
		}

		/**
		 * 0x05:�����б�
		 */
		else if (msgType == IMsgConstance.command_teamList) {
			MsgTeamList mtl = (MsgTeamList) msg;
			List<TeamInfo> teams = mtl.getTeamLists();
			dous.writeInt(teams.size());// д���ж��ٸ�����
			for (TeamInfo team : teams) {
				writeString(dous, 16, team.getTName());// д��һ�����������
				List<UserInfo> users = team.getTeamUsers();
				dous.writeByte(users.size());// д�������ж��ٸ����Ѷ���
				for (UserInfo user : users) {
					// д��ÿһ�����ѵ�qq��
					dous.writeInt(user.getID());
					// д��ÿһ�����ѵ��س�
					writeString(dous, 16, user.getNikeName());
					// д��ÿһ�����ѵ�headimgsrc
					writeString(dous, 24, user.getHeadImgSrc());
				}
			}
		}

		/**
		 * 0x06,0x07:���������ߣ�����Ϣ�壩
		 */
		else if (msgType == IMsgConstance.command_offLine
				|| msgType == IMsgConstance.command_onLine) {
		}

		/**
		 * 0x08:������Ϣ����
		 */
		else if (msgType == IMsgConstance.command_chat) {
			MsgChat mc = (MsgChat) msg;
			dous.write(mc.getMsgContent().getBytes());
		}

		/**
		 * 0x09:�����ļ�����
		 */
		else if (msgType == IMsgConstance.command_file) {
			MsgFile mf = (MsgFile) msg;
			writeString(dous, 256, mf.getFileName());
			dous.write(mf.getFileData());
		}

		/**
		 * 0x11:�ļ�����Ӧ��
		 */
		else if (msgType == IMsgConstance.command_file_resp) {
			MsgFileResp mfr = (MsgFileResp) msg;
			dous.writeByte(mfr.getState());
		}
		/**
		 * 0x02:��ͼ
		 */
		else if (msgType == IMsgConstance.command_prtsc) {
			MsgFile mf = (MsgFile) msg;
			writeString(dous, 50, mf.getFileName());
		}
		/**
		 * 0x13:���ڶ�����Ϣ������Ϣ�壩
		 */
		else if (msgType == IMsgConstance.command_winshake) {
		}

		/**
		 * 0x14������Ⱥ����
		 */
		else if (msgType == IMsgConstance.command_createGroup) {
			MsgCreateGroup mcg = (MsgCreateGroup) msg;
			writeString(dous, 16, mcg.getGName());
		}
		/**
		 * 0x15������Ⱥ����Ӧ��
		 */
		else if (msgType == IMsgConstance.command_createGroup_resp) {
			MsgCreateGroupResp mcgr = (MsgCreateGroupResp) msg;
			dous.writeInt(mcgr.getGID());
		}

		/**
		 * 0x16:���Һ�����������Ϣ�壩
		 */
		else if (msgType == IMsgConstance.command_findUsers) {
		}

		/**
		 * 0x17:���Һ���Ӧ��
		 */
		else if (msgType == IMsgConstance.command_findUsers_resp) {
			MsgFindUsersResp mfur = (MsgFindUsersResp) msg;
			List<UserInfo> users = mfur.getUsers();
			dous.writeInt(users.size());
			for (UserInfo user : users) {
				writeString(dous, 16, user.getNikeName());
				writeString(dous, 24, user.getHeadImgSrc());
				dous.writeInt(user.getID());
			}
		}

		/**
		 * 0x18:��Ӻ�������
		 */
		else if (msgType == IMsgConstance.command_addFriend) {
			MsgAddFriend maf = (MsgAddFriend) msg;
			dous.writeInt(maf.getFID());
			writeString(dous, 16, maf.getFTeamName());
			dous.writeInt(maf.getUi().getID());
			writeString(dous, 16, maf.getUi().getNikeName());
			writeString(dous, 24, maf.getUi().getHeadImgSrc());
		}

		/**
		 * 0x19:��Ӻ��ѵ�Ӧ��
		 */
		else if (msgType == IMsgConstance.command_addFriend_Resp) {
			MsgAddFriendResp mafr = (MsgAddFriendResp) msg;
			writeString(dous, 16, mafr.getTeanName());
			dous.writeInt(mafr.getUi().getID());
			writeString(dous, 16, mafr.getUi().getNikeName());
			writeString(dous, 24, mafr.getUi().getHeadImgSrc());
		}
		/**
		 * 0x21:����������Ⱥ���б�
		 */
		else if (msgType == IMsgConstance.command_groupList) {
			MsgGroupList mgl = (MsgGroupList) msg;
			List<GroupInfo> groups = mgl.getGroupList();
			dous.writeInt(groups.size());// д���ж��ٸ�Ⱥ
			for (GroupInfo group : groups) {
				dous.writeInt(group.getGID());
				writeString(dous, 16, group.getGName());// д��һ�����������
				dous.writeInt(group.getGMaster());
				List<UserInfo> users = group.getGroupsUsers();
				dous.writeByte(users.size());// д�������ж��ٸ����Ѷ���
				for (UserInfo user : users) {
					// д��ÿһ�����ѵ�qq��
					dous.writeInt(user.getID());
					// д��ÿһ�����ѵ��س�
					writeString(dous, 16, user.getNikeName());
					// д��ÿһ�����ѵ�headimgsrc
					writeString(dous, 24, user.getHeadImgSrc());
				}
			}
		}

		/**
		 * 0x22:������Ⱥ��������Ϣ�壩
		 */
		else if (msgType == IMsgConstance.command_findGroup) {
		}

		/**
		 * 0x23:������Ⱥ����Ӧ��
		 */
		else if (msgType == IMsgConstance.command_findGroup_resp) {
			MsgFindGroupResp mfgr = (MsgFindGroupResp) msg;
			List<GroupInfo> groups = mfgr.getGroups();
			dous.writeInt(groups.size());// д���ж��ٸ�Ⱥ
			for (GroupInfo group : groups) {
				dous.writeInt(group.getGID());
				writeString(dous, 16, group.getGName());// д��һ�����������
				dous.writeInt(group.getGMaster());
			}
		}

		else {
			// String logMsg="����δ֪��Ϣ���ͣ��޷����:type:"+msgType;
			// LogTools.ERROR(ToolsCreateMsg.class, logMsg);//��¼��־
			System.out.println("����δ֪��Ϣ���ͣ��޷����");
		}

		dous.flush();
		byte[] data = bous.toByteArray();
		return data;// ���ش���������,�Է��㷢��

	}

	/**
	 * ������д����Ϣͷ����
	 * 
	 * @param dous
	 *            :Ҫд���������
	 * @param m
	 *            :��Ϣͷ����
	 */
	private static void writeHead(DataOutputStream dous, MsgHead head)
			throws IOException {
		dous.writeInt(head.getTotalLen());
		dous.writeByte(head.getType());
		dous.writeInt(head.getDest());
		dous.writeInt(head.getSrc());
	}

	/**
	 * ������д��len���ȵ��ַ��� ���s���ֽڳ��Ȳ���len������'\0'
	 * 
	 * @param dous
	 *            :���������
	 * @param len
	 *            :Ҫд��ĳ���
	 * @param s
	 *            :Ҫд�뵽���е��ַ���
	 */
	private static void writeString(DataOutputStream dous, int len, String s)
			throws IOException {
		byte[] data = s.getBytes();
		if (data.length > len) {
			throw new IOException("д�볤��Ϊ" + data.length + ",����!");
		}
		dous.write(data);
		while (len > data.length) {// ����̣���Ҫ��0
			dous.writeByte('\0');// ��������0
			len--;
		}
	}
}
