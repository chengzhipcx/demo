package com.langsin.qq.model;

import java.util.ArrayList;
import java.util.List;

public class GroupInfo {
	private int GID;
	private String GName;
	private int GMaster;
	private ArrayList<UserInfo> groupsUsers = new ArrayList<UserInfo>();

	@Override
	public String toString() {
		return "GroupInfo [GID=" + GID + ", GName=" + GName + ", GMaster="
				+ GMaster + "]";
	}

	public void setGroupsUsers(ArrayList<UserInfo> groupsUsers) {
		this.groupsUsers = groupsUsers;
	}

	public void addUsers(UserInfo ui) {
		groupsUsers.add(ui);
	}

	public ArrayList<UserInfo> getGroupsUsers() {
		return groupsUsers;
	}

	public int getGID() {
		return GID;
	}

	public void setGID(int gID) {
		GID = gID;
	}

	public String getGName() {
		return GName;
	}

	public void setGName(String gName) {
		GName = gName;
	}

	public int getGMaster() {
		return GMaster;
	}

	public void setGMaster(int gMaster) {
		GMaster = gMaster;
	}

}
