package com.langsin.qq.msg;

public class MsgLogin extends MsgHead {
	
	private String pwd;		//��¼���룬�˺�����Ϣͷ�С�
	
	public String toString() {
		String head = super.toString();
		return head + "PassWordd:" + pwd;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	
}
