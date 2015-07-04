package com.langsin.qq.model;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import com.langsin.qq.msg.*;

/**
 * 消息构建工具类
 * 
 * 
 */
public class MsgCreateTools {

	/**
	 * 将消息对象打包为字节数组返回 将消息写入内存IO中
	 * 
	 * @param msg
	 *            :要打包的消息对象
	 */

	public static byte[] packMsg(MsgHead msg) throws IOException {
		// 创建内存输出流
		ByteArrayOutputStream bous = new ByteArrayOutputStream();
		DataOutputStream dous = new DataOutputStream(bous);

		writeHead(dous, msg); // 先写入消息头,所有消息头结构相同
		int msgType = msg.getType(); // 取得消息类型标识

		/**
		 * 0x01:注册请求
		 */
		if (msgType == IMsgConstance.command_reg) {
			MsgReg mr = (MsgReg) msg;
			writeString(dous, 16, mr.getNikeName());
			writeString(dous, 12, mr.getPwd());
			writeString(dous, 24, mr.getHeadImg());
		}

		/**
		 * 0x02:注册应答
		 */
		else if (msgType == IMsgConstance.command_reg_resp) {
			MsgRegResp mrr = (MsgRegResp) msg;
			dous.writeByte(mrr.getState());
		}

		/**
		 * 0x03:登陆请求
		 */
		else if (msgType == IMsgConstance.command_login) {
			MsgLogin ml = (MsgLogin) msg;
			writeString(dous, 12, ml.getPwd());
		}

		/**
		 * 0x04:登陆应答
		 */
		else if (msgType == IMsgConstance.command_login_resp) {
			MsgLoginResp mlr = (MsgLoginResp) msg;
			dous.writeByte(mlr.getState());
			writeString(dous, 16, mlr.getNikeName());
			writeString(dous, 24, mlr.getHeadImgSrc());
		}

		/**
		 * 0x05:好友列表
		 */
		else if (msgType == IMsgConstance.command_teamList) {
			MsgTeamList mtl = (MsgTeamList) msg;
			List<TeamInfo> teams = mtl.getTeamLists();
			dous.writeInt(teams.size());// 写入有多少个分组
			for (TeamInfo team : teams) {
				writeString(dous, 16, team.getTName());// 写入一个分组的名字
				List<UserInfo> users = team.getTeamUsers();
				dous.writeByte(users.size());// 写入组内有多少个好友对象
				for (UserInfo user : users) {
					// 写入每一个好友的qq号
					dous.writeInt(user.getID());
					// 写入每一个好友的呢称
					writeString(dous, 16, user.getNikeName());
					// 写入每一个好友的headimgsrc
					writeString(dous, 24, user.getHeadImgSrc());
				}
			}
		}

		/**
		 * 0x06,0x07:好友上下线（无消息体）
		 */
		else if (msgType == IMsgConstance.command_offLine
				|| msgType == IMsgConstance.command_onLine) {
		}

		/**
		 * 0x08:聊天消息发送
		 */
		else if (msgType == IMsgConstance.command_chat) {
			MsgChat mc = (MsgChat) msg;
			dous.write(mc.getMsgContent().getBytes());
		}

		/**
		 * 0x09:传送文件请求
		 */
		else if (msgType == IMsgConstance.command_file) {
			MsgFile mf = (MsgFile) msg;
			writeString(dous, 256, mf.getFileName());
			dous.write(mf.getFileData());
		}

		/**
		 * 0x11:文件传送应答
		 */
		else if (msgType == IMsgConstance.command_file_resp) {
			MsgFileResp mfr = (MsgFileResp) msg;
			dous.writeByte(mfr.getState());
		}
		/**
		 * 0x02:截图
		 */
		else if (msgType == IMsgConstance.command_prtsc) {
			MsgFile mf = (MsgFile) msg;
			writeString(dous, 50, mf.getFileName());
		}
		/**
		 * 0x13:窗口抖动消息（无消息体）
		 */
		else if (msgType == IMsgConstance.command_winshake) {
		}

		/**
		 * 0x14创建组群请求
		 */
		else if (msgType == IMsgConstance.command_createGroup) {
			MsgCreateGroup mcg = (MsgCreateGroup) msg;
			writeString(dous, 16, mcg.getGName());
		}
		/**
		 * 0x15创建组群请求应答
		 */
		else if (msgType == IMsgConstance.command_createGroup_resp) {
			MsgCreateGroupResp mcgr = (MsgCreateGroupResp) msg;
			dous.writeInt(mcgr.getGID());
		}

		/**
		 * 0x16:查找好友请求（无消息体）
		 */
		else if (msgType == IMsgConstance.command_findUsers) {
		}

		/**
		 * 0x17:查找好友应答
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
		 * 0x18:添加好友请求
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
		 * 0x19:添加好友的应答
		 */
		else if (msgType == IMsgConstance.command_addFriend_Resp) {
			MsgAddFriendResp mafr = (MsgAddFriendResp) msg;
			writeString(dous, 16, mafr.getTeanName());
			dous.writeInt(mafr.getUi().getID());
			writeString(dous, 16, mafr.getUi().getNikeName());
			writeString(dous, 24, mafr.getUi().getHeadImgSrc());
		}
		/**
		 * 0x21:服务器发送群组列表
		 */
		else if (msgType == IMsgConstance.command_groupList) {
			MsgGroupList mgl = (MsgGroupList) msg;
			List<GroupInfo> groups = mgl.getGroupList();
			dous.writeInt(groups.size());// 写入有多少个群
			for (GroupInfo group : groups) {
				dous.writeInt(group.getGID());
				writeString(dous, 16, group.getGName());// 写入一个分组的名字
				dous.writeInt(group.getGMaster());
				List<UserInfo> users = group.getGroupsUsers();
				dous.writeByte(users.size());// 写入组内有多少个好友对象
				for (UserInfo user : users) {
					// 写入每一个好友的qq号
					dous.writeInt(user.getID());
					// 写入每一个好友的呢称
					writeString(dous, 16, user.getNikeName());
					// 写入每一个好友的headimgsrc
					writeString(dous, 24, user.getHeadImgSrc());
				}
			}
		}

		/**
		 * 0x22:查找组群请求（无消息体）
		 */
		else if (msgType == IMsgConstance.command_findGroup) {
		}

		/**
		 * 0x23:查找组群请求应答
		 */
		else if (msgType == IMsgConstance.command_findGroup_resp) {
			MsgFindGroupResp mfgr = (MsgFindGroupResp) msg;
			List<GroupInfo> groups = mfgr.getGroups();
			dous.writeInt(groups.size());// 写入有多少个群
			for (GroupInfo group : groups) {
				dous.writeInt(group.getGID());
				writeString(dous, 16, group.getGName());// 写入一个分组的名字
				dous.writeInt(group.getGMaster());
			}
		}

		else {
			// String logMsg="创建未知消息类型，无法打包:type:"+msgType;
			// LogTools.ERROR(ToolsCreateMsg.class, logMsg);//记录日志
			System.out.println("创建未知消息类型，无法打包");
		}

		dous.flush();
		byte[] data = bous.toByteArray();
		return data;// 返回打包后的数据,以方便发送

	}

	/**
	 * 向流中写入消息头数据
	 * 
	 * @param dous
	 *            :要写入的流对象
	 * @param m
	 *            :消息头对象
	 */
	private static void writeHead(DataOutputStream dous, MsgHead head)
			throws IOException {
		dous.writeInt(head.getTotalLen());
		dous.writeByte(head.getType());
		dous.writeInt(head.getDest());
		dous.writeInt(head.getSrc());
	}

	/**
	 * 向流中写入len长度的字符串 如果s的字节长度不足len个，补'\0'
	 * 
	 * @param dous
	 *            :输出流对象
	 * @param len
	 *            :要写入的长度
	 * @param s
	 *            :要写入到流中的字符串
	 */
	private static void writeString(DataOutputStream dous, int len, String s)
			throws IOException {
		byte[] data = s.getBytes();
		if (data.length > len) {
			throw new IOException("写入长度为" + data.length + ",超长!");
		}
		dous.write(data);
		while (len > data.length) {// 如果短，需要补0
			dous.writeByte('\0');// 补二进制0
			len--;
		}
	}
}
