package com.sec.hong.samecardgame.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sec.hong.samecardgame.Logger;

public class DBConnection {
	
	public static DBConnection dbConn;

	public static DBConnection getInstance() {
		if(dbConn == null) {
			dbConn = new DBConnection(); 
		}
		return dbConn;
	}
	
	public static Connection getDbConn() {
		Connection dbConn = null;
		try {
			String user = "hong";
			String pw = "1234";
			String url = "jdbc:oracle:thin:@localhost:1521:oracle";
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			dbConn = DriverManager.getConnection(url, user, pw);
			System.out.println("DataBase�� ����Ǿ����ϴ�.");
		} catch (ClassNotFoundException e) {
			System.out.println("����̹� �ε� ���� : " + e.toString());
		} catch (SQLException e) {
			System.out.println("DB ���ӽ��� " + e.toString());
		} catch (Exception e) {
			System.out.println("Unkown Error");
		}
		return dbConn;
	}
	
	
	
}
