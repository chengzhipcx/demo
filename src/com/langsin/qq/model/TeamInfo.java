package com.langsin.qq.model;

import java.util.ArrayList;
import java.util.List;

public class TeamInfo {
	private int TID;
	private String TName;			//名称
	private UserInfo owerUser;		//本组包含的用户
	
	private List<UserInfo> users = new ArrayList<UserInfo>();
	
	/**
	  * 创建一个分组对象
	  * @param GID:此组的唯一ID
	  * @param GName:组名字
	  * @param owerUser:此组所属的用户对象
	  */
	 public TeamInfo(String TName){
		 this.TName=TName;
	 }
	 public TeamInfo (int TID,String TName,UserInfo owerUser){
		 this.TID = TID;
		 this.TName=TName;
		 this.owerUser=owerUser;
	 }
	 
	@Override
	public String toString() {
		return "TeamInfo [TID=" + TID + ", TName=" + TName + "]";
	}
	/**
	 * 为此分组加入一个好友对象
	 * @param user
	 */
	public void addUsers(UserInfo  user ) {
		this.users.add(user);
	}
	/**
	 * 以下为set/get方法
	 * @return
	 */
	public int getTID() {
		return TID;
	}
	public void setTID(int tID) {
		TID = tID;
	}
	public List<UserInfo> getTeamUsers() {
		return users;
	}

	public String getTName() {
		return TName;
	}

	public void setTName(String tName) {
		TName = tName;
	}

	public UserInfo getOwerUser() {
		return owerUser;
	}

	public void setOwerUser(UserInfo owerUser) {
		this.owerUser = owerUser;
	}

}
