package com.langsin.qq.dataBase.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.langsin.qq.model.GroupInfo;
import com.langsin.qq.model.TeamInfo;
import com.langsin.qq.model.UserInfo;
import com.langsin.qq.msg.MsgFindGroupResp;
import com.langsin.qq.msg.MsgFindUsersResp;
import com.langsin.qq.msg.MsgTeamList;

public class JdbcTest {

	private static JdbcTest jdbcTest = new JdbcTest();
	private static Connection conn = null;
	private static Statement st = null;

	public static JdbcTest getJdbcTest() {
		return jdbcTest;
	}

	/**
	 * 查询baseID
	 */
	public int getBaseID(String sql) {

		ResultSet result = null;
		int baseID = 100000;
		try {
			conn = JdbcUtil.getConnection();
			st = conn.createStatement();
			result = st.executeQuery(sql);
			while (result.next()) {
				baseID = result.getInt(1);
			}
			return baseID;
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				st.close();
				conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return baseID;
	}

	/**
	 * 注册用户
	 * 
	 * @param sql
	 */
	public void reg(String sql) {
		try {
			conn = JdbcUtil.getConnection();
			st = conn.createStatement();
			st.execute(sql);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				st.close();
				conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}

	/**
	 * 登陆
	 * 
	 * @param String
	 *            sql
	 * @return String pwd
	 */
	public UserInfo login(String sql) {

		ResultSet result = null;
		UserInfo ui = null;
		int id;
		String pwd = null;
		String nikeName = null;
		String headImgSrc = null;
		try {
			conn = JdbcUtil.getConnection();
			st = conn.createStatement();
			result = st.executeQuery(sql);
			while (result.next()) {
				id = result.getInt(1);
				pwd = result.getString(2);
				nikeName = result.getString(3);
				headImgSrc = result.getString(4);
				ui = new UserInfo(id, pwd, nikeName);
				ui.setHeadImgSrc(headImgSrc);
			}
			return ui;

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				st.close();
				conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return ui;
	}

	/**
	 * 获取好友列表
	 * 
	 * @param sql
	 * @return
	 */
	public static MsgTeamList teamList(String sql) {
		ResultSet result = null;
		try {
			conn = JdbcUtil.getConnection();
			st = conn.createStatement();
			result = st.executeQuery(sql);
			MsgTeamList mtl = new MsgTeamList();
			List<TeamInfo> teamList = new ArrayList<TeamInfo>();
			while (result.next()) {
				int id = result.getInt(1);
				String nakeName = result.getString(2);
				String headImgSrc = result.getString(3);
				int tid = result.getInt(4);
				String tname = result.getString(5);
				boolean add = false;
				UserInfo user = new UserInfo(nakeName, id);
				user.setHeadImgSrc(headImgSrc);
				for (int i = 0; i <= teamList.size(); i++) {
					if (teamList.size() > i) {
						if (teamList.get(i).getTID() == tid) {
							teamList.get(i).addUsers(user);
							add = true;
						}
					}

				}
				if (!add) {
					TeamInfo team = new TeamInfo(tid, tname, null);
					team.addUsers(user);
					teamList.add(team);
				}
			}
			mtl.setTeamLists(teamList);
			return mtl;

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				st.close();
				conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return null;
	}

	/**
	 * 查询群组
	 * 
	 * @param sql
	 * @return
	 */
	public static ArrayList<GroupInfo> getGroups(String sql) {
		ResultSet result = null;
		ArrayList<GroupInfo> groups = new ArrayList<GroupInfo>();
		try {
			conn = JdbcUtil.getConnection();
			st = conn.createStatement();
			result = st.executeQuery(sql);
			while (result.next()) {
				int gID = result.getInt(1);
				String gName = result.getString(2);
				int getGMaster = result.getInt(3);
				GroupInfo gi = new GroupInfo();
				gi.setGID(gID);
				gi.setGName(gName);
				gi.setGMaster(getGMaster);
				groups.add(gi);
			}
			return groups;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				st.close();
				conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return groups;
	}

	/**
	 * 查询群组成员
	 * 
	 * @param sql_groupList
	 * @return
	 */
	public static ArrayList<UserInfo> getGroupsUsers(String sql_groupList) {

		ResultSet result = null;
		ArrayList<UserInfo> users = new ArrayList<UserInfo>();
		try {
			conn = JdbcUtil.getConnection();
			st = conn.createStatement();
			result = st.executeQuery(sql_groupList);
			while (result.next()) {
				int ID = result.getInt(1);
				String nikeName = result.getString(2);
				String headMsgSrc = result.getString(3);
				UserInfo ui = new UserInfo(nikeName, ID);
				ui.setHeadImgSrc(headMsgSrc);
				users.add(ui);
			}
			return users;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				st.close();
				conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return users;
	}

	/**
	 * 查询好友
	 * 
	 * @param sql
	 * @return
	 */
	public static MsgFindUsersResp findUsers(String sql) {
		ResultSet result = null;
		try {
			conn = JdbcUtil.getConnection();
			st = conn.createStatement();
			result = st.executeQuery(sql);
			MsgFindUsersResp mfur = new MsgFindUsersResp();
			while (result.next()) {
				int id = result.getInt(1);
				String nakeName = result.getString(2);
				String headImgSrc = result.getString(3);
				UserInfo ui = new UserInfo(nakeName, id);
				ui = new UserInfo(nakeName, id);
				ui.setHeadImgSrc(headImgSrc);
				mfur.addUserInfo(ui);
			}
			return mfur;
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				st.close();
				conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return null;
	}

	/**
	 * 添加好友
	 * 
	 * @param sql
	 * @return
	 */
	public static int addFriends1(String sql1, String fTName) {
		ResultSet result = null;
		try {
			conn = JdbcUtil.getConnection();
			st = conn.createStatement();
			result = st.executeQuery(sql1);
			int max = 0;
			while (result.next()) {
				int fTID = result.getInt(1);
				String fTeamName = removeSpace(result.getString(2));
				if (removeSpace(fTName).equals(fTeamName)) {
					return fTID;
				}
				max = fTID;
			}
			return max + 1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				st.close();
				conn.close();
			} catch (Exception e3) {
				// TODO: handle exception
			}
		}
		return 1;
	}

	public static void addFriends2(String sql2) {
		try {
			conn = JdbcUtil.getConnection();
			st = conn.createStatement();
			st.execute(sql2);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				st.close();
				conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}
	
	public static MsgFindGroupResp findGroup(String sql){
		ResultSet result = null;
		MsgFindGroupResp mfgr = null;
		List<GroupInfo> groupList  = new ArrayList<GroupInfo>();
		try {
			mfgr = new MsgFindGroupResp();
			conn = JdbcUtil.getConnection();
			st = conn.createStatement();
			result = st.executeQuery(sql);
			while(result.next()){
				int gid = result.getInt(1);
				String gName = result.getString(2);
				int gMaster = result.getInt(3);
				GroupInfo gi = new GroupInfo();
				gi.setGID(gid);
				gi.setGName(gName);
				gi.setGMaster(gMaster);
				groupList.add(gi);
			}
			mfgr.setGroups(groupList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mfgr;
	}
	
	/**
	 * 查询groupBaseID
	 */
	public int getGroupBaseID(String sql) {

		ResultSet result = null;
		int baseGroupID = 1000;
		try {
			conn = JdbcUtil.getConnection();
			st = conn.createStatement();
			result = st.executeQuery(sql);
			while (result.next()) {
				baseGroupID = result.getInt(1);
			}
			return baseGroupID;
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				st.close();
				conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return baseGroupID;
	}
	
	/**
	 * 去除String后面的\0
	 */
	private static String removeSpace(String str) {
		char[] s = str.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length; i++) {
			if (s[i] == '\0') {
				break;
			}
			sb.append(s[i]);
		}
		return sb.toString();
	}

}
