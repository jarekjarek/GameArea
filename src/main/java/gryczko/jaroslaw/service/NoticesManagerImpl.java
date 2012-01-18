package gryczko.jaroslaw.service;

import gryczko.jaroslaw.domain.Mysql;
import gryczko.jaroslaw.domain.Notices;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


public class NoticesManagerImpl implements NoticesManager {
	Mysql mysql = new Mysql(); 
	SessionManagerImpl sm = new SessionManagerImpl(); 

	@Override
	public boolean addAnn(Notices no) { 

		String username = null;
		int id;
		// get cookies
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
			} else {    
				return false;
			}
			PreparedStatement psaa = mysql.con
					.prepareStatement("INSERT INTO "
							+ mysql.getNoticesTableName()
							+ " (date, title, content, contact, telephone, users_id) values (CURRENT_DATE(), ?, ?, ?, ?, ? )");
			psaa.setString(1, no.getTitle());
			psaa.setString(2, no.getContent());
			psaa.setString(3, no.getContact());
			psaa.setInt(4, no.getTelephone());  
			psaa.setInt(5, id);
			psaa.executeUpdate(); 
			
			
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext ec = context.getExternalContext();
			HttpServletRequest request = (HttpServletRequest) ec.getRequest();
			request.getSession(false).invalidate();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<Notices> getsearch(String ssearch) {  
		List<Notices> baza = new ArrayList<Notices>(); 
		try {
			ResultSet rs = mysql.stmt
					.executeQuery("SELECT accept, notices.id as id, date, title,  content, contact, vote, notices.telephone as telephone, login from "
							+ mysql.getNoticesTableName()   
							+ " INNER JOIN "
							+ mysql.getUserTableName()
							+ " ON notices.users_id = users.id WHERE title LIKE '%"
							+ ssearch
							+ "%' ORDER BY notices.id DESC");

			while (rs.next()) {  
				Notices notices = new Notices();
				notices.setId(rs.getInt("id"));
				notices.setDate(rs.getString("date"));
				notices.setTitle(rs.getString("title"));
				notices.setContent(rs.getString("content"));
				notices.setContact(rs.getString("contact"));
				notices.setTelephone(rs.getInt("telephone"));
				notices.setUname(rs.getString("login"));
				notices.setVote(rs.getInt("vote"));

				baza.add(notices);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return baza; 

	}

	@Override
	public boolean deleteAnn(Notices no) {
		PreparedStatement psaa;
		try {
			mysql.stmt.executeUpdate("DELETE from votes where id_notices="+no.getId());   
			psaa = mysql.con.prepareStatement("DELETE FROM "
					+ mysql.getNoticesTableName() + " where id=?");
			psaa.setInt(1, no.getId());
			psaa.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;

	}

	@Override
	public List<Notices> getbrowse() {  
		List<Notices> baza = new ArrayList<Notices>();
		try {
			ResultSet rs = mysql.stmt
					.executeQuery("SELECT notices.id as id, date, title,  content, contact, vote, notices.telephone as telephone, login from "
							+ mysql.getNoticesTableName()
							+ " INNER JOIN "
							+ mysql.getUserTableName()
							+ " ON notices.users_id = users.id WHERE accept=1 ORDER BY notices.date DESC");   

			while (rs.next()) {
				Notices notices = new Notices();
				notices.setId(rs.getInt("id"));
				notices.setDate(rs.getString("date"));
				notices.setTitle(rs.getString("title"));
				notices.setContent(rs.getString("content"));
				notices.setContact(rs.getString("contact"));
				notices.setTelephone(rs.getInt("telephone"));
				notices.setUname(rs.getString("login"));
				notices.setVote(rs.getInt("vote"));

				baza.add(notices);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return baza;

	}

	public boolean accept(int id) {  
		try {
			mysql.stmt.executeUpdate("UPDATE notices set accept=1 WHERE id="
					+ id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public boolean drop(int id) {   
		try {
			mysql.stmt.executeUpdate("DELETE from notices WHERE id=" + id);   
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public List<Notices> getpending() {    
		List<Notices> baza = new ArrayList<Notices>();
		try {
			ResultSet rs = mysql.stmt
					.executeQuery("SELECT notices.id as id, date, title,  content, contact, vote, notices.telephone as telephone, login from "
							+ mysql.getNoticesTableName()
							+ " INNER JOIN "
							+ mysql.getUserTableName()
							+ " ON notices.users_id = users.id WHERE accept=0 ORDER BY notices.date DESC");

			while (rs.next()) {
				Notices notices = new Notices();
				notices.setId(rs.getInt("id"));
				notices.setDate(rs.getString("date"));
				notices.setTitle(rs.getString("title"));
				notices.setContent(rs.getString("content"));
				notices.setContact(rs.getString("contact"));
				notices.setTelephone(rs.getInt("telephone"));
				notices.setUname(rs.getString("login"));
				notices.setVote(rs.getInt("vote"));

				baza.add(notices);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return baza;

	}

	public List<Notices> getbrowse(int id) {  
		List<Notices> baza = new ArrayList<Notices>();
		try {
			ResultSet rs = mysql.stmt   
					.executeQuery("SELECT notices.id as id, date, title,  content, contact, vote, notices.telephone as telephone, login from "
							+ mysql.getNoticesTableName()
							+ " INNER JOIN "
							+ mysql.getUserTableName()
							+ " ON notices.users_id = users.id WHERE users.id="
							+ id + " ORDER BY notices.date DESC");

			while (rs.next()) {
				Notices notices = new Notices();
				notices.setId(rs.getInt("id"));
				notices.setDate(rs.getString("date"));
				notices.setTitle(rs.getString("title"));
				notices.setContent(rs.getString("content"));
				notices.setContact(rs.getString("contact"));
				notices.setTelephone(rs.getInt("telephone"));
				notices.setUname(rs.getString("login"));
				notices.setVote(rs.getInt("vote"));

				baza.add(notices);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return baza;

	}

	@Override
	public List<Notices> getmybrowse() {     
		List<Notices> baza = new ArrayList<Notices>();
		try {

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
				return baza;
			}

			ResultSet rs1 = mysql.stmt.executeQuery("SELECT admin from "
					+ mysql.getUserTableName() + " where login='" + username
					+ "'");
			rs1.next();
			if (rs1.getInt("admin") == 1) {    
				ResultSet rs = mysql.stmt
						.executeQuery("SELECT notices.id as id, date, title, vote, content, contact, notices.telephone as telephone, login from "
								+ mysql.getNoticesTableName()
								+ " INNER JOIN "
								+ mysql.getUserTableName()
								+ " ON notices.users_id = users.id ORDER BY notices.id DESC");

				while (rs.next()) {
					Notices notices = new Notices();
					notices.setId(rs.getInt("id"));
					notices.setDate(rs.getString("date"));
					notices.setTitle(rs.getString("title"));
					notices.setContent(rs.getString("content"));
					notices.setContact(rs.getString("contact"));
					notices.setTelephone(rs.getInt("telephone"));
					notices.setUname(rs.getString("login"));
					notices.setVote(rs.getInt("vote"));

					baza.add(notices);
				}

			} else {   

				ResultSet rs = mysql.stmt
						.executeQuery("SELECT notices.id as id, date, title,  content, vote, contact, notices.telephone as telephone, login from "
								+ mysql.getNoticesTableName()
								+ " INNER JOIN "
								+ mysql.getUserTableName()
								+ " ON notices.users_id = users.id where login='"
								+ username + "'");

				while (rs.next()) {
					Notices notices = new Notices();
					notices.setId(rs.getInt("id"));
					notices.setDate(rs.getString("date"));
					notices.setTitle(rs.getString("title"));
					notices.setContent(rs.getString("content"));
					notices.setContact(rs.getString("contact"));
					notices.setTelephone(rs.getInt("telephone"));
					notices.setUname(rs.getString("login"));
					notices.setVote(rs.getInt("vote"));

					baza.add(notices);

				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return baza;

	}

	@Override
	public boolean update(Notices no) {   
		PreparedStatement psaa;
		try {
			psaa = mysql.con
					.prepareStatement("UPDATE "
							+ mysql.getNoticesTableName()
							+ " set title=?, content=?, contact=?, telephone=? where id="
							+ no.getId());
			psaa.setString(1, no.getTitle());
			psaa.setString(2, no.getContent());
			psaa.setString(3, no.getContact());
			psaa.setInt(4, no.getTelephone());
			psaa.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;

	}

	@Override
	public boolean rox(int id) {  

		int iduser = sm.getCurrentUserId();
		try {
			mysql.stmt.executeUpdate("UPDATE " + mysql.getNoticesTableName()
					+ " set vote=vote+1 WHERE id=" + id);
			PreparedStatement ps = mysql.con.prepareStatement("INSERT INTO "
					+ mysql.getVotesTableName()
					+ "(id_users, id_notices) VALUES (?,?)");
			ps.setInt(1, iduser);
			ps.setInt(2, id);
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;

	}

	@Override
	public boolean sux(int id) {  
		int iduser = sm.getCurrentUserId();
		try {
			mysql.stmt.executeUpdate("UPDATE " + mysql.getNoticesTableName()
					+ " set vote=vote-1 WHERE id=" + id);
			PreparedStatement ps = mysql.con.prepareStatement("INSERT INTO "
					+ mysql.getVotesTableName()
					+ "(id_users, id_notices) VALUES (?,?)");
			ps.setInt(1, iduser);
			ps.setInt(2, id);
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;

	}

	@Override
	public Notices showgetann(Long id) {   
		Notices not = new Notices();
		try {
			ResultSet rs = mysql.stmt
					.executeQuery("SELECT notices.id as id, date, title,  content, contact, vote, notices.telephone as telephone, login from "
							+ mysql.getNoticesTableName()
							+ " INNER JOIN "
							+ mysql.getUserTableName()
							+ " ON notices.users_id = users.id where notices.id="
							+ id);
			rs.next();
			not.setContact(rs.getString("contact"));
			not.setContent(rs.getString("content"));
			not.setDate(rs.getString("date"));
			not.setId(rs.getInt("id"));
			not.setTelephone(rs.getInt("telephone"));
			not.setTitle(rs.getString("title"));
			not.setUname(rs.getString("login"));
			not.setVote(rs.getInt("vote"));
			return not;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return not;

	}

	public boolean checkVotes(int id) {   
		int userid = sm.getCurrentUserId();
		try {
			ResultSet rs = mysql.stmt.executeQuery("SELECT id from "
					+ mysql.getVotesTableName() + " WHERE id_users=" + userid
					+ " AND id_notices=" + id);
			if (rs.next()) {
				return true;  
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

}
