package com.langsin.qq.msg;

public class MsgChat extends MsgHead {
	private String msgContent;
	
	public String toString() {
		String head = super.toString();
		return head + "��Ϣ����:" + msgContent;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	
	

}
