package com.langsin.qq.msg;

import com.langsin.qq.model.UserInfo;

public class MsgAddFriendResp extends MsgHead {

	private String TeanName;
	private UserInfo ui;

	public UserInfo getUi() {
		return ui;
	}

	public void setUi(UserInfo ui) {
		this.ui = ui;
	}

	public String getTeanName() {
		return TeanName;
	}

	public void setTeanName(String teanName) {
		TeanName = teanName;
	}

	@Override
	public String toString() {
		return super.toString() + "MsgAddFriendResp [TeanName=" + TeanName
				+ ", ui=" + ui + "]";
	}

}
