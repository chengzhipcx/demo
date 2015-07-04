package com.langsin.qq.msg;

import com.langsin.qq.model.UserInfo;

public class MsgAddFriend extends MsgHead {
	private int FID;
	private String FTeamName;
	private UserInfo ui;

	@Override
	public String toString() {
		return "MsgAddFriend [FID=" + FID + ", FTeamName=" + FTeamName
				+ ", ui=" + ui + "]";
	}

	public int getFID() {
		return FID;
	}

	public void setFID(int fID) {
		FID = fID;
	}

	public String getFTeamName() {
		return FTeamName;
	}

	public void setFTeamName(String fTeamName) {
		FTeamName = fTeamName;
	}

	public UserInfo getUi() {
		return ui;
	}

	public void setUi(UserInfo ui) {
		this.ui = ui;
	}

}
