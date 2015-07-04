package com.langsin.qq.msg;

public class MsgLoginResp extends MsgHead{

	private byte state;// 登陆状态 0:成功 其它:失败代码
	private String nikeName;
	private String HeadImgSrc;
	
	
	
	public String toString() {
		String head = super.toString();
		return head + " state:" + state+ " nikeName:" + nikeName+ " HeadImgSrc:" + HeadImgSrc;
	}

	// 以下为getter/seter方法
	
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
