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
 * 消息解析工具类
 * 
 * 
 */
public class MsgParseTools {
	/**
	 * 将从流上读到的数据块，解包为消息对象
	 * 
	 * @param data
	 *            :读到的数据块
	 * @return:解包后的消息对象
	 */
	public static MsgHead parseMsg(byte[] data) throws Exception {

		/**
		 * 转换为内存流
		 */
		ByteArrayInputStream bins = new java.io.ByteArrayInputStream(data);
		DataInputStream dins = new java.io.DataInputStream(bins);

		int totalLen = data.length + 4; // 算出消息总长
		byte msgType = dins.readByte(); // 读取消息类型
		int dest = dins.readInt(); // 读取目标QQ号
		int src = dins.readInt(); // 读取源QQ号

		/**
		 * 以下将消息头数据赋值
		 */
		MsgHead msgHead = new MsgHead();
		msgHead.setTotalLen(totalLen);
		msgHead.setType(msgType);
		msgHead.setDest(dest);
		msgHead.setSrc(src);

		/**
		 * 以下处理请求。
		 * 
		 * 0x01:注册请求
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
		 * 0x02:注册应答
		 */
		else if (msgType == IMsgConstance.command_reg_resp) {
			MsgRegResp mrr = new MsgRegResp();
			copyHead(msgHead, mrr);
			mrr.setState(dins.readByte());
			return mrr;
		}

		/**
		 * 0x03:登陆请求
		 */
		else if (msgType == IMsgConstance.command_login) {
			String pwd = readString(dins, 12);
			MsgLogin ml = new MsgLogin();
			copyHead(msgHead, ml); // 复制消息头数据
			ml.setPwd(pwd);
			return ml;
		}

		/**
		 * 0x04:登陆应答
		 */
		else if (msgType == IMsgConstance.command_login_resp) {
			byte state = dins.readByte(); // 读取状态字段，一个字节
			String nikeName = readString(dins, 16);
			String headImgSrc = readString(dins, 24);
			MsgLoginResp ml = new MsgLoginResp();
			copyHead(msgHead, ml); // 复制消息头数据
			ml.setState(state);
			ml.setNikeName(nikeName);
			ml.setHeadImgSrc(headImgSrc);
			return ml;
		}

		/**
		 * 0x05:好友列表数据包
		 */
		else if (msgType == IMsgConstance.command_teamList) {
			MsgTeamList mtl = new MsgTeamList();
			copyHead(msgHead, mtl);
			int listCount = dins.readInt(); // 标识有几个分组的数据
			List<TeamInfo> teamLists = new ArrayList<TeamInfo>();
			while (listCount > 0) {
				listCount--;
				String teamName = readString(dins, 16); // 读取一个分组名字
				TeamInfo team = new TeamInfo(teamName); // 创建一个分组对象
				byte UserCount = dins.readByte(); // 组内有几个用户
				while (UserCount > 0) {
					UserCount--;
					int FID = dins.readInt(); // 读取一个用户的QQ号
					String nikeName = readString(dins, 16); // 读取这个用户的呢称
					String headImgSrc = readString(dins, 24); // 读取这个用户的headImgSrc
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
		 * 0x06,0x07:好友上下线(无消息体)
		 */
		else if (msgType == IMsgConstance.command_offLine
				|| msgType == IMsgConstance.command_onLine) {
			return msgHead;
		}

		/**
		 * 0x08:聊天消息体
		 */
		else if (msgType == IMsgConstance.command_chat) {
			int len = totalLen - 4 - 1 - 4 - 4; // 计算聊天内容字节的长度
			String content = readString(dins, len);// 读取聊天内容字符串
			MsgChat mc = new MsgChat();
			copyHead(msgHead, mc);
			mc.setMsgContent(content);
			return mc;
		}

		/**
		 * 0x09:文件数据包
		 */
		else if (msgType == IMsgConstance.command_file) {
			String fileName = readString(dins, 256); // 读取文件数据包
			int fileLen = totalLen - 4 - 1 - 4 - 4 - 256; // 计算文件数据字节的长度
			MsgFile mf = new MsgFile();
			copyHead(msgHead, mf);
			mf.setFileName(fileName);
			byte[] fileData = new byte[fileLen];
			dins.readFully(fileData);
			mf.setFileData(fileData);
			return mf;
		}

		/**
		 * 0x11:文件传送应答
		 */
		else if (msgType == IMsgConstance.command_file_resp) {
			byte state = dins.readByte(); // 读取状态字段，一个字节
			MsgFileResp mfr = new MsgFileResp();
			copyHead(msgHead, mfr); // 复制消息头数据
			mfr.setState(state);
			return mfr;
		}
		/**
		 * 0x12:截图
		 */
		else if (msgType == IMsgConstance.command_prtsc) {
			String fileName = readString(dins, 50); // 读取文件数据包
			MsgFile mf = new MsgFile();
			copyHead(msgHead, mf);
			mf.setFileName(fileName);
			return mf;
		}

		/**
		 * 0x13:窗口抖动(无消息体)
		 */
		else if (msgType == IMsgConstance.command_winshake) {
			return msgHead;
		}
		
		/**
		 * 0x14创建组群请求
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
		 * 0x15创建组群应答
		 */
		else if (msgType == IMsgConstance.command_createGroup_resp) {
			int gid = dins.readInt();
			MsgCreateGroupResp mcgr = new MsgCreateGroupResp();
			copyHead(msgHead, mcgr); // 复制消息头数据
			mcgr.setGID(gid);
			return mcgr;
		}
		// /**
		// * 0x14,0x15:远程桌面消息请求,远程桌面应答(无消息体)
		// */
		// else if(msgType==IMsgConstance.command_remoteControl||
		// msgType==IMsgConstance.command_remoteControl_resp){
		// return msgHead;
		// }

		/**
		 * 0x16:查找好友(无消息体)
		 */
		else if (msgType == IMsgConstance.command_findUsers) {
			return msgHead;
		}

		/**
		 * 0x17:查找好友应答
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
		 * 0x18:添加好友
		 */
		else if (msgType == IMsgConstance.command_addFriend) {
			MsgAddFriend maf = new MsgAddFriend();
			copyHead(msgHead, maf);

			int FID = dins.readInt(); // 要加入的好友的QQ号码
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
		 * 0x19:添加好友的应答
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
		 * 0x21:服务器发送群组列表
		 */
		else if (msgType == IMsgConstance.command_groupList) {

			MsgGroupList mgl = new MsgGroupList();
			copyHead(msgHead, mgl);
			int listCount = dins.readInt(); // 标识有几个群组
			List<GroupInfo> groupLists = new ArrayList<GroupInfo>();
			while (listCount > 0) {
				listCount--;
				int groupID = dins.readInt();
				String groupName = readString(dins, 16); // 读取一个组群名字
				int gMaster = dins.readInt();
				GroupInfo group = new GroupInfo(); // 创建一个分组对象
				group.setGID(groupID);
				group.setGName(groupName);
				group.setGMaster(gMaster);
				byte UserCount = dins.readByte(); // 组内有几个用户
				while (UserCount > 0) {
					UserCount--;
					int FID = dins.readInt(); // 读取一个用户的QQ号
					String nikeName = readString(dins, 16); // 读取这个用户的呢称
					String headImgSrc = readString(dins, 24); // 读取这个用户的headImgSrc
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
		 * 0x22:查找组群
		 */
		else if (msgType == IMsgConstance.command_findGroup) {
			return msgHead;
		}
		/**
		 * 0x23:查找组群应答
		 */
		else if (msgType == IMsgConstance.command_findGroup_resp) {
			MsgFindGroupResp mfgr = new MsgFindGroupResp();
			copyHead(msgHead, mfgr);
			int listCount = dins.readInt(); // 标识有几个群组
			List<GroupInfo> groupLists = new ArrayList<GroupInfo>();
			while (listCount > 0) {
				listCount--;
				int groupID = dins.readInt();
				String groupName = readString(dins, 16); // 读取一个组群名字
				int gMaster = dins.readInt();
				GroupInfo group = new GroupInfo(); // 创建一个分组对象
				group.setGID(groupID);
				group.setGName(groupName);
				group.setGMaster(gMaster);
				groupLists.add(group);
			}
			mfgr.setGroups(groupLists);
			return mfgr;
		}
		
		else {
			// String logMsg="解包未知消息类型，无法解包:type:"+msgType;
			// LogTools.ERROR(ToolsParseMsg.class, logMsg);//记录日志
			System.out.println("解包未知消息类型，无法解包:type:" + msgType);
		}
		return null;
	}

	/**
	 * 复制消息头的数据:
	 * 
	 * @param head
	 *            :消息头
	 * @param dest
	 *            :要复制到的目标消息对象
	 */
	private static void copyHead(MsgHead head, MsgHead dest) {
		dest.setTotalLen(head.getTotalLen());
		dest.setType(head.getType());
		dest.setDest(head.getDest());
		dest.setSrc(head.getSrc());

	}

	/**
	 * 从流中读取len长度个字节，编码为字符串返回
	 * 
	 * @param dins
	 *            :要读取的流对象
	 * @param len
	 *            :读取的字节长度
	 * @return:编码后的字符串
	 */
	private static String readString(DataInputStream dins, int len) {
		byte[] data = new byte[len];
		try {
			dins.readFully(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String(data);// 使用系统默认字符集编码
	}

	/**
	 * 去除String后面的\0
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
