package com.langsin.qq.msg;

public class MsgCreateGroupResp extends MsgHead {
	
	private int GID;
	
	@Override
	public String toString() {
		return "MsgCreateGroupResp [GID=" + GID + "]";
	}
	
	public int getGID() {
		return GID;
	}
	public void setGID(int gID) {
		GID = gID;
	}
}
