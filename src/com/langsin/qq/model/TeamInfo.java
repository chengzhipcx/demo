package com.langsin.qq.model;

import java.util.ArrayList;
import java.util.List;

public class TeamInfo {
	private int TID;
	private String TName;			//����
	private UserInfo owerUser;		//����������û�
	
	private List<UserInfo> users = new ArrayList<UserInfo>();
	
	/**
	  * ����һ���������
	  * @param GID:�����ΨһID
	  * @param GName:������
	  * @param owerUser:�����������û�����
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
	 * Ϊ�˷������һ�����Ѷ���
	 * @param user
	 */
	public void addUsers(UserInfo  user ) {
		this.users.add(user);
	}
	/**
	 * ����Ϊset/get����
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
