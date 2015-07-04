package com.langsin.qq.msg;

import java.util.ArrayList;
import java.util.List;

import com.langsin.qq.model.UserInfo;



public class MsgFindUsersResp extends MsgHead {
	
	// 存放好友分组的队列
	private List<UserInfo> findusers = new ArrayList<UserInfo>();
	
	// 加一个用户对象
	public void addUserInfo(UserInfo bu) {
		this.findusers.add(bu);
	}

	// 取得所有好友列表
	public List<UserInfo> getUsers() {
		return this.findusers;
	}

	public String toString() {
		return super.toString() + this.findusers.toString();
	}


}
