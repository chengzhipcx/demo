package com.langsin.qq.msg;

import java.util.ArrayList;
import java.util.List;

import com.langsin.qq.model.UserInfo;



public class MsgFindUsersResp extends MsgHead {
	
	// ��ź��ѷ���Ķ���
	private List<UserInfo> findusers = new ArrayList<UserInfo>();
	
	// ��һ���û�����
	public void addUserInfo(UserInfo bu) {
		this.findusers.add(bu);
	}

	// ȡ�����к����б�
	public List<UserInfo> getUsers() {
		return this.findusers;
	}

	public String toString() {
		return super.toString() + this.findusers.toString();
	}


}
