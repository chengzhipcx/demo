package com.langsin.qq.msg;

public interface IMsgConstance {
	
	String serverIP = "localhost";// 服务器IP
	int serverPort = 9090; // 服务器端口
	int Server_QQ_NUMBER = 100000;// 服务器的QQ号
	
	// 系统用到的消息类型定义
	byte command_reg = 0x01;// 注册请求消息
	byte command_reg_resp = 0x02;// 注册应答消息
	byte command_login = 0x03;// 登陆请求消息
	byte command_login_resp = 0x04;// 登陆应答消息
	byte command_teamList = 0x05;// 服务器发送在线好友列表
	byte command_onLine = 0x06;// 服务器发送好友上线消息*无消息体
	byte command_offLine = 0x07;// 服务器发送好友下线消息*无消息体
	byte command_chat = 0x08;// 聊天消息发送与接收	
	byte command_file = 0x09;// 文件传送发送
	byte command_file_resp = 0x11;//文件传送应答
	byte command_prtsc = 0x12;//发送截屏图片请求消息
	byte command_winshake = 0x13;//窗口抖动消息请求*无消息体
	byte command_createGroup = 0x14;//创建群组请求
	byte command_createGroup_resp = 0x15;//创建群组请求应答*无消息体
	byte command_findUsers = 0x16;// 查找用户请求 *无消息体
	byte command_findUsers_resp = 0x17;// 查找好友请求应答
	byte command_addFriend = 0x18;// 添加好友请求
	byte command_addFriend_Resp = 0x19;// 添加好友应答
	byte command_groupList = 0x21;//服务器发送群组列表
	byte command_findGroup = 0x22;//查找组群
	byte command_findGroup_resp = 0x23;//查找组群应答
	byte command_addGroup = 0x24;//添加组群
	byte command_addGroup_resp = 0x25;//添加组群应答
	byte command_groupChat = 0x26;//群聊消息发送与接收
//	byte command_remoteControl = 0x27;//远程桌面消息请求*无消息体
//	byte command_remoteControl_resp = 0x28;//远程桌面应答*无消息体
	//共26种消息，2种选做。
}
