package com.langsin.qq.msg;

public class MsgReg extends MsgHead {
	
	private String nikeName;	//ע���û���
	private String pwd;			//����
	private String headImg;
	
	public String toString() {
		String head = super.toString();
		return head + "nikeName:" + nikeName + " pwd:" + pwd;
	}

	public String getNikeName() {
		return nikeName;
	}

	public void setNikeName(String nikeName) {
		this.nikeName = nikeName;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	
}
