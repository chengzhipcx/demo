package com.langsin.qq.msg;

public class MsgFileResp extends MsgHead {
	
	private byte state;
	/**
	 * �������ļ��Ŀͻ��˷��ظ�������
	 * ����������ظ������ļ��Ŀͻ��˵��ļ����ͽ��Ӧ��
	 * 0Ϊ���գ�
	 * ǰ����Ϣͷ�е�destֵΪ������QQ���룬
	 * ������Ϣͷ�е�destֵΪ�����ļ��Ŀͻ��˵�QQ���룻
	 * 1Ϊ�ܾ����գ�2Ϊ���ͳɹ���-1Ϊ����ʧ�ܡ�
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
