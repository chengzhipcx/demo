package com.langsin.qq.msg;

public class MsgLoginResp extends MsgHead{

	private byte state;// ��½״̬ 0:�ɹ� ����:ʧ�ܴ���
	private String nikeName;
	private String HeadImgSrc;
	
	
	
	public String toString() {
		String head = super.toString();
		return head + " state:" + state+ " nikeName:" + nikeName+ " HeadImgSrc:" + HeadImgSrc;
	}

	// ����Ϊgetter/seter����
	
	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	public String getNikeName() {
		return nikeName;
	}

	public void setNikeName(String nikeName) {
		this.nikeName = nikeName;
	}

	public String getHeadImgSrc() {
		return HeadImgSrc;
	}

	public void setHeadImgSrc(String headImgSrc) {
		HeadImgSrc = headImgSrc;
	}
	
}
