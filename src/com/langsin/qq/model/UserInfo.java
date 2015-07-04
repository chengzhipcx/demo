package com.langsin.qq.model;

import java.util.ArrayList;
import java.util.List;

public class UserInfo implements java.io.Serializable {

	private int ID; // �û���QQ����
	private String pwd; // �û�������
	private String nikeName; // �û����ǳ�
	private String headImgSrc; //

	private List<TeamInfo> usersteams = new ArrayList<TeamInfo>();

	public UserInfo(int ID) {
		this.ID = ID;
	}

	public UserInfo(int ID, String pwd) {
		this.ID = ID;
		this.pwd = pwd;
	}

	public UserInfo(String nikeName, int ID) {
		this.ID = ID;
		this.nikeName = nikeName;
	}

	public UserInfo(int ID, String pwd, String nikeName) {
		this.ID = ID;
		this.pwd = pwd;
		this.nikeName = nikeName;
	}

	@Override
	public String toString() {
		return "UserInfo [ID=" + ID + ", nikeName=" + nikeName + "]";
	}

	/**
	 * �����û�����һ���������
	 * 
	 * @param team
	 *            :һ�����������
	 */
	public void addTeams(TeamInfo team) {
		this.usersteams.add(team);
	}

	/**
	 * ����Ϊset/get����
	 * 
	 * @return
	 */

	public List<TeamInfo> getUsersteams() {
		return usersteams;
	}

	public String getHeadImgSrc() {
		return headImgSrc;
	}

	public void setHeadImgSrc(String headImgSrc) {
		this.headImgSrc = headImgSrc;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getNikeName() {
		return nikeName;
	}

	public void setNikeName(String nikeName) {
		this.nikeName = nikeName;
	}

}
