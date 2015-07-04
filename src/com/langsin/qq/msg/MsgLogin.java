package com.langsin.qq.msg;

public class MsgLogin extends MsgHead {
	
	private String pwd;		//登录密码，账号在消息头中。
	
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
