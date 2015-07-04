package com.langsin.qq.msg;

public class MsgRegResp extends MsgHead {
	private byte state;	
	/**
	 * 服务器返回的注册结果应答，
	 * 0为成功，非0为注册失败。
	 * 
	 * 消息头中的dest值为注册成功的QQ号码；
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
