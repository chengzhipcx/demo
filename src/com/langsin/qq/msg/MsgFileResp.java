package com.langsin.qq.msg;

public class MsgFileResp extends MsgHead {
	
	private byte state;
	/**
	 * 被发送文件的客户端返回给服务器
	 * 或服务器返回给发送文件的客户端的文件传送结果应答，
	 * 0为接收，
	 * 前者消息头中的dest值为服务器QQ号码，
	 * 后者消息头中的dest值为发送文件的客户端的QQ号码；
	 * 1为拒绝接收；2为发送成功；-1为发送失败。
	 */
	
	public String toString() {
		String head = super.toString();
		return head + "state:" + state;
	}
	public byte getState() {
		return state;
	}
	public void setState(byte state) {
		this.state = state;
	}

	
}
