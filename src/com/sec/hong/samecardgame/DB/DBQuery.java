package com.sec.hong.samecardgame.DB;

import java.awt.peer.TrayIconPeer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBQuery {

	private static DBQuery instance;
	
	Connection conn = null;
	PreparedStatement pstm = null;
	ResultSet res = null;
	
	
	public static DBQuery getInstance() {
		if(instance == null) {
			instance = new DBQuery(); 
		}
		return instance;
	}
	
	public ArrayList<HashMap<String, String>> select() {
		// 카드 점수에 따라 순위가 다르게 나와야 한다.
		HashMap<String, String> map	;
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
		
		
		try {
			String selectSql = "select * from CARDGAME order by score desc";
			conn = DBConnection.getDbConn();
			pstm = conn.prepareStatement(selectSql);
			res = pstm.executeQuery();
			
			//System.out.println("==============================================");
			//System.out.println("               CARDGAME TABLE");
			//System.out.println("==============================================");
			//System.out.println("USERNAME | LEVELNAME | RANK | SCORE | GAMETIME");
			
			while (res.next()) {
				map	= new HashMap<>();
				String username = res.getString(2);
				String levelname = res.getString(3);
				String rank = res.getString(4);
				int score = res.getInt(5);
				int time = res.getInt(6);
				
				map.put("username", username);
				map.put("levelname", levelname);
				map.put("rank", rank);
				map.put("score",Integer.toString(score));
				map.put("time",Integer.toString(time));
				list.add(map);
				//System.out.println(String.format("%s      %s       %s 	    %s      %s",
				//								  username,levelname,rank,score,time));
			}
			return list;

		}catch (SQLException e) {
			System.out.println("SQLExeption : "+ e.toString());
		}finally {
			try {
				if(res!=null) {res.close();}
				if(pstm != null) {pstm.close();}
				if(conn != null) {conn.close();}
			} catch (Exception e2) {
				throw new RuntimeException(e2.getMessage());
			}
		}
		return list;
		
	}
	public void Insert(String username, String levelname, String rank, int Score , int gameTime) {
		try {
			String insertSql = "INSERT INTO CARDGAME VALUES (cardgame_seq.NEXTVAL,?,?,?,?,?)";
			conn = DBConnection.getDbConn();
			pstm = conn.prepareStatement(insertSql);
			
			pstm.setString(1, username);
			pstm.setString(2, levelname);
			pstm.setString(3, rank);
			pstm.setInt(4, Score);
			pstm.setInt(5,gameTime);
			
			pstm.executeUpdate();
			
		}catch (SQLException e) {
			System.out.println("SQLExeption : "+ e.toString());
		}finally {
			try {
				if(res!=null) {res.close();}
				if(pstm != null) {pstm.close();}
				if(conn != null) {conn.close();}
			} catch (Exception e2) {
				throw new RuntimeException(e2.getMessage());
			}
		}
	}
	
}	
