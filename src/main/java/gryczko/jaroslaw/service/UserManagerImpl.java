package gryczko.jaroslaw.service;

import gryczko.jaroslaw.domain.Mysql;
import gryczko.jaroslaw.domain.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


public class UserManagerImpl implements UserManager {
	Mysql mysql = new Mysql();

	@Override
	public boolean addUser(User user, boolean newsletter) {
		int news = 0;
		if (newsletter) {  
			news = 1;                                                                                  //dodanie nowego usera
		}
		try {
			ResultSet rs1 = mysql.stmt
					.executeQuery("SELECT id from " + mysql.getUserTableName()
							+ " where login='" + user.getLogin()
							+ "' OR  email='" + user.getEmail() + "'");
			if (rs1.next()) {

				return false;
			}
			PreparedStatement psau = mysql.con
					.prepareStatement("INSERT INTO "
							+ mysql.getUserTableName()
							+ "(fname, lname, telephone, dob, email, login, password, dof, newsletter) VALUES (?, ?, ?, ?, ?, ?, MD5(?), CURRENT_DATE(), ?)");
			psau.setString(1, user.getFname());
			psau.setString(2, user.getLname());
			psau.setInt(3, user.getTelephone());
			psau.setString(4, user.getDob());
			psau.setString(5, user.getEmail());
			psau.setString(6, user.getLogin());
			psau.setString(7, user.getPassword());
			psau.setInt(8, news);
			psau.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override                                 
	public User editUser() {
		User u = new User();
		String username = null;

		HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		Cookie[] cookies = httpServletRequest.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equalsIgnoreCase("user")) {                                //setting ..wyrzuca dane uzytkownika
					username = cookies[i].getValue();
				}
			}
		} else {
			return u;
		}
		try {
			ResultSet rs = mysql.stmt.executeQuery("SELECT * FROM "
					+ mysql.getUserTableName() + " where login='" + username
					+ "'");
			rs.next();
			u.setFname(rs.getString("fname"));
			u.setLname(rs.getString("lname"));
			u.setEmail(rs.getString("email"));
			u.setTelephone(rs.getInt("telephone"));
			u.setDob(rs.getString("dob"));
			u.setLogin(rs.getString("login"));
			u.setPassword(null); 
			
			return u;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return u;
	}

	@Override
	public boolean deleteUser() {   
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
		} else {
			return false;
		}
		try {
			ResultSet rs = mysql.stmt.executeQuery("SELECT id from "
					+ mysql.getUserTableName() + " where login='" + username       
					+ "'");
			rs.next();

			PreparedStatement psdc = mysql.con.prepareStatement("DELETE FROM "     
					+ mysql.getVotesTableName() + " WHERE users_id=?");
			psdc.setInt(1, rs.getInt("id"));
			psdc.executeUpdate();

			PreparedStatement psdn = mysql.con.prepareStatement("DELETE FROM "
					+ mysql.getNoticesTableName() + " WHERE users_id=?");              
			psdn.setInt(1, rs.getInt("id"));
			psdn.executeUpdate();

			PreparedStatement psds = mysql.con.prepareStatement("DELETE FROM "     
					+ mysql.getSessionTableName() + " WHERE users_id=?");
			psds.setInt(1, rs.getInt("id"));
			psds.executeUpdate();
			PreparedStatement psdu = mysql.con.prepareStatement("DELETE FROM "     
					+ mysql.getUserTableName() + " WHERE id=?");
			psdu.setInt(1, rs.getInt("id"));
			psdu.executeUpdate();

			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public boolean update(User user) {        
		PreparedStatement psaa;
		try {
			psaa = mysql.con
					.prepareStatement("UPDATE users set fname=?, lname=?, telephone=?, dob=?, email=?, password=MD5(?) where login=?");
			psaa.setString(1, user.getFname());
			psaa.setString(2, user.getLname());      
			psaa.setInt(3, user.getTelephone());
			psaa.setString(4, user.getDob());
			psaa.setString(5, user.getEmail());
			psaa.setString(6, user.getPassword());
			psaa.setString(7, user.getLogin());

			psaa.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;

	}


	@Override
	public List<User> getbrowse() {                    
		List<User> baza = new ArrayList<User>();
		try {
			ResultSet rs = mysql.stmt.executeQuery("SELECT * from "
					+ mysql.getUserTableName());

			while (rs.next()) {
				User user = new User();
				user.setFname(rs.getString("fname"));
				user.setLname(rs.getString("lname"));
				user.setTelephone(rs.getInt("telephone"));
				user.setDob(rs.getString("fname"));
				user.setEmail(rs.getString("email"));
				user.setLogin(rs.getString("login"));
				user.setPassword(null);
				baza.add(user);
			}
			return baza;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return baza;

	}

	@Override
	public boolean adelete(String login) {    
		try {
			ResultSet rs = mysql.stmt
					.executeQuery("SELECT id from " + mysql.getUserTableName()
							+ " where login='" + login + "'");
			rs.next();
			PreparedStatement psdn = mysql.con.prepareStatement("DELETE FROM "
					+ mysql.getNoticesTableName() + " WHERE users_id=?");
			psdn.setInt(1, rs.getInt("id"));
			psdn.executeUpdate();
			PreparedStatement psds = mysql.con.prepareStatement("DELETE FROM "
					+ mysql.getSessionTableName() + " WHERE users_id=?");
			psds.setInt(1, rs.getInt("id"));
			psds.executeUpdate();
			PreparedStatement psdu = mysql.con.prepareStatement("DELETE FROM "
					+ mysql.getUserTableName() + " WHERE id=?");
			psdu.setInt(1, rs.getInt("id"));
			psdu.executeUpdate();

			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	@Override
	public boolean aadmin(String login) {    
		try {
			PreparedStatement ps = mysql.con.prepareStatement("UPDATE "
					+ mysql.getUserTableName() + " set admin=1 where login=?");
			ps.setString(1, login);
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	@Override
	public boolean auser(String login) {    
		try {
			PreparedStatement ps = mysql.con.prepareStatement("UPDATE "
					+ mysql.getUserTableName() + " set admin=0 where login=?");
			ps.setString(1, login);
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	public User showgetuser(String login) {  
		User not = new User();
		try {
			ResultSet rs = mysql.stmt
					.executeQuery("SELECT id, fname, lname, dob, login from "
							+ mysql.getUserTableName() + " WHERE login='"
							+ login + "'");
			rs.next();
			not.setId(rs.getInt("id"));
			not.setDob(rs.getString("dob"));
			not.setEmail(null);
			not.setFname(rs.getString("fname"));
			not.setLname(rs.getString("lname"));
			not.setLogin(rs.getString("login"));
			not.setPassword(null);
			not.setTelephone(0);
			return not;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return not;

	}

}
