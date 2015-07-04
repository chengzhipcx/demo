package com.langsin.qq.msg;

public class MsgCreateGroup extends MsgHead {
	
	private String GName;

	@Override
	public String toString() {
		return "MsgCreateGroup [GName=" + GName + "]";
	}

	public String getGName() {
		return GName;
	}

	public void setGName(String gName) {
		GName = gName;
	}
	
	

}
