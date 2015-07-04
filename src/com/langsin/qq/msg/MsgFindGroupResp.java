package com.langsin.qq.msg;

import java.util.ArrayList;
import java.util.List;

import com.langsin.qq.model.GroupInfo;

public class MsgFindGroupResp extends MsgHead {
	
	private List<GroupInfo> groups = new ArrayList<GroupInfo>();
	
	

	@Override
	public String toString() {
		return "MsgFindGroupResp [groups=" + groups + "]";
	}

	public List<GroupInfo> getGroups() {
		return groups;
	}

	public void setGroups(List<GroupInfo> groups) {
		this.groups = groups;
	}
	
	

}
