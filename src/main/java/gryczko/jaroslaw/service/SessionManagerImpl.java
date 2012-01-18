package gryczko.jaroslaw.service;

import gryczko.jaroslaw.domain.Mysql;
import gryczko.jaroslaw.domain.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;


import javax.servlet.http.*;
import javax.faces.context.*;

public class SessionManagerImpl implements SessionManager {   
	Mysql mysql = new Mysql();

	@Override
	public boolean loginUser(User user) {  

		int id;
		try {

			Calendar calendar = Calendar.getInstance();
			java.util.Date now = calendar.getTime();
			java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(
					now.getTime());   
			long timestamp = currentTimestamp.getTime();
			String loginform = user.getLogin();
			String passform = user.getPassword();

			ResultSet hash1 = mysql.stmt.executeQuery("SELECT MD5(" + timestamp   
					+ ") as hash, MD5('" + passform + "') as pass");
			hash1.next();
			String hash = hash1.getString("hash");
			String pass = hash1.getString("pass");

			ResultSet auth = mysql.stmt.executeQuery("SELECT id from "
					+ mysql.getUserTableName() + " where login='" + loginform  
					+ "' AND password='" + pass + "'");

			if (auth.next()) { 
				FacesContext facesContext = FacesContext.getCurrentInstance();
				Cookie btuser = new Cookie("user", loginform);
				Cookie btpasswd = new Cookie("hash", hash);
				btuser.setMaxAge(3600);
				btpasswd.setMaxAge(3600);

				((HttpServletResponse) facesContext.getExternalContext()
						.getResponse()).addCookie(btuser);
				((HttpServletResponse) facesContext.getExternalContext()
						.getResponse()).addCookie(btpasswd);

				ResultSet iduser = mysql.stmt.executeQuery("SELECT id from "
						+ mysql.getUserTableName() + " where login='"
						+ user.getLogin() + "'");
				if (iduser.next()) {
					id = iduser.getInt("id");
				} else {
					return false;
				}
				PreparedStatement psau = mysql.con    
						.prepareStatement("INSERT INTO "
								+ mysql.getSessionTableName()
								+ "(ses_id, login, users_id) VALUES (?,?,?)");
				psau.setString(1, hash);
				psau.setString(2, user.getLogin());
				psau.setInt(3, id);
				psau.executeUpdate();
				return true;

			} else {
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean removeCookie() {   
		String username = null;
		
		HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		Cookie[] cookies = httpServletRequest.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equalsIgnoreCase("user")) {
					username = cookies[i].getValue();
				}
			}
		}

		try {
			PreparedStatement ps = mysql.con.prepareStatement("DELETE FROM "
					+ mysql.getSessionTableName() + " WHERE login=?");
			ps.setString(1, username);
			ps.executeUpdate();
			FacesContext facesContext = FacesContext.getCurrentInstance();
			Cookie btuser = new Cookie("user", "0");
			Cookie btpasswd = new Cookie("hash", "0"); 
			
			btuser.setMaxAge(3600);
			btpasswd.setMaxAge(3600);

			((HttpServletResponse) facesContext.getExternalContext()
					.getResponse()).addCookie(btuser);
			((HttpServletResponse) facesContext.getExternalContext()
					.getResponse()).addCookie(btpasswd);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean checkCookie() {  
		String username = null;
		String hash = null;
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String cookieName = null;
		Cookie cookie[] = ((HttpServletRequest) facesContext
				.getExternalContext().getRequest()).getCookies();
		if (cookie != null && cookie.length > 0) {
			for (int i = 0; i < cookie.length; i++) {
				cookieName = cookie[i].getName();

				if (cookieName.equals("user")) {
					username = cookie[i].getValue();
				} else if (cookieName.equals("hash")) {
					hash = cookie[i].getValue();
				}
			}
		} else {
			return false;
		}
		try {
			ResultSet check = mysql.stmt.executeQuery("SELECT id from "
					+ mysql.getSessionTableName() + " WHERE ses_id='" + hash
					+ "' AND login='" + username + "'");
			if (check.next()) {
				return true;

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean checkAdmin() { 
		String username = null;
		boolean check = checkCookie();
		if (check == true) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext
					.getCurrentInstance().getExternalContext().getRequest();
			Cookie[] cookies = httpServletRequest.getCookies();
			if (cookies != null) {
				for (int i = 0; i < cookies.length; i++) {
					if (cookies[i].getName().equalsIgnoreCase("user")) {   
						username = cookies[i].getValue();
					}
				}
			}
			try {
				ResultSet rs = mysql.stmt.executeQuery("SELECT admin from "
						+ mysql.getUserTableName() + " WHERE login='"
						+ username + "'");
				rs.next();
				if (rs.getInt("admin") == 1) {
					return true;
				} else {
					return false;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return check;

	}

	public int getCurrentUserId() {  
		String username = null;
		int id;
		
		HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		Cookie[] cookies = httpServletRequest.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equalsIgnoreCase("user")) {
					username = cookies[i].getValue();
				}
			}
		}

		try {
			ResultSet iduser = mysql.stmt.executeQuery("SELECT id from "
					+ mysql.getUserTableName() + " where login='" + username
					+ "'");
			if (iduser.next()) {
				id = iduser.getInt("id");
				return id;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}
}
