package com.langsin.qq.msg;

public class MsgRegResp extends MsgHead {
	private byte state;	
	/**
	 * ���������ص�ע����Ӧ��
	 * 0Ϊ�ɹ�����0Ϊע��ʧ�ܡ�
	 * 
	 * ��Ϣͷ�е�destֵΪע��ɹ���QQ���룻
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
