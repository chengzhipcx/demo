package com.langsin.qq.msg;

import java.util.ArrayList;
import java.util.List;

import com.langsin.qq.model.GroupInfo;

public class MsgGroupList extends MsgHead {

	private List<GroupInfo> groupList = new ArrayList<GroupInfo>();

	@Override
	public String toString() {
		return super.toString() + " [groupList=" + groupList + "]";
	}

	public List<GroupInfo> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<GroupInfo> groupList) {
		this.groupList = groupList;
	}

}
