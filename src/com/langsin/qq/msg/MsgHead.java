package com.langsin.qq.msg;

public class MsgHead {

	private int totalLen;	// ��Ϣ�ܳ���
	private byte type;		// ��Ϣ����
	private int dest;		// Ŀ���û�QQ��
	private int src;		// �����û���QQ��

	public String toString() {
		return "totalLen:" + totalLen + " type:" + type + " dest:" + dest
				+ " src:" + src;
	}

	public int getTotalLen() {
		return totalLen;
	}

	public void setTotalLen(int totalLen) {
		this.totalLen = totalLen;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public int getDest() {
		return dest;
	}

	public void setDest(int dest) {
		this.dest = dest;
	}

	public int getSrc() {
		return src;
	}

	public void setSrc(int src) {
		this.src = src;
	}
	
	
}