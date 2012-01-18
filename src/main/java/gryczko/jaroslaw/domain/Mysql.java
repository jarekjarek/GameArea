package gryczko.jaroslaw.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Mysql {
	public Connection con;
	public Statement stmt;
	private String noticesTableName = "notices";
	private String userTableName = "users";
	private String sessionTableName = "session";
	private String votesTableName = "votes";
	private String host = "localhost";

	public void makeconnection() {

		try {
			con = DriverManager.getConnection("jdbc:mysql://" + host
					+ "/sas?useUnicode=true&characterEncoding=utf8", "root",
					"");
			

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Polaczenie nie udalo sie.");
		}
	}
	
	
	public void closeconnection() {
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Mysql() {
		makeconnection();
		try {
			stmt = con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getNoticesTableName() {
		return noticesTableName;
	}

	public void setNoticesTableName(String noticesTableName) {
		this.noticesTableName = noticesTableName;
	}

	public String getUserTableName() {
		return userTableName;
	}

	public void setUserTableName(String userTableName) {
		this.userTableName = userTableName;
	}

	public String getSessionTableName() {
		return sessionTableName;
	}

	public void setSessionTableName(String sessionTableName) {
		this.sessionTableName = sessionTableName;
	}

	public String getVotesTableName() {
		return votesTableName;
	}

	public void setVotesTableName(String votesTableName) {
		this.votesTableName = votesTableName;
	}
}
