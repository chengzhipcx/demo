package com.langsin.qq.dataBase.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcUtil {
	
	private static String username = "root";
	private static String password = "root";
	private static String url = "jdbc:mysql://localhost:3306/myqq";
	private static String driver = "com.mysql.jdbc.Driver";
	
	/**
	 * 连接到数据库 获取一个链接
	 * @return
	 */
	public static Connection getConnection(){
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url,username,password);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	
}
