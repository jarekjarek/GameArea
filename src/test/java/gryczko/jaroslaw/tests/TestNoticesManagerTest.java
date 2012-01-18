package gryczko.jaroslaw.tests;

import static org.junit.Assert.*;

import gryczko.jaroslaw.domain.Mysql;
import gryczko.jaroslaw.domain.Notices;
import gryczko.jaroslaw.domain.User;
import gryczko.jaroslaw.service.NoticesManagerImpl;
import gryczko.jaroslaw.service.UserManagerImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.mail.MessagingException;

import org.junit.After;
import org.junit.Test;

public class TestNoticesManagerTest {
	Mysql mysql = new Mysql();
	User user = new User();
	NoticesManagerImpl nm = new NoticesManagerImpl();
	UserManagerImpl um = new UserManagerImpl();

	public void createTestUser() {   
		Notices n = new Notices();
		n.setContact("goon@g00n.pl");
		n.setContent("Test");
		n.setDate("2011-01-12");
		n.setId(20);
		n.setTelephone(12345678);
		n.setTitle("TYTUL");
		n.setUname("goon");
		n.setVote(0);
		User u = um.showgetuser("goon");
		PreparedStatement ps;

		try {
			ps = mysql.con
					.prepareStatement("INSERT INTO "
							+ mysql.getNoticesTableName()
							+ "(id, date, title, content, contact, telephone, vote, users_id) VALUES(?,?,?,?,?,?,?,?)");
			ps.setInt(1, n.getId());
			ps.setString(2, n.getDate());
			ps.setString(3, n.getTitle());
			ps.setString(4, n.getContent());
			ps.setString(5, n.getContact());
			ps.setInt(6, n.getTelephone());
			ps.setInt(7, n.getVote());
			ps.setInt(8, u.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Notices getObject() {
		Notices n = new Notices();
		n.setContact("goon@g00n.pl");
		n.setContent("Test");
		n.setDate("2011-01-12");
		n.setId(20);
		n.setTelephone(12345678);
		n.setTitle("TYTUL");
		n.setUname("goon");
		n.setVote(0);
		return n;

	}

	@Test
	public void getsearchTest() {    
		List<Notices> check = nm.getsearch("VAT");   
		assertNotNull(check);
	}

	

	@Test      
	public void getBrowseTest() {
		List<Notices> check = nm.getbrowse();
		assertNotNull(check);
	}

	@Test
	public void getBrowseTest1() {   
		List<Notices> check = nm.getbrowse(16);
		assertNotNull(check);
	}

	@Test
	public void updateTest() {      
		this.createTestUser();
		Notices n = this.getObject();
		User u = um.showgetuser("goon");   
		try {

			n.setTitle("TYTUL ZMIENIONY");
			boolean check = nm.update(n);
			assertTrue(check);
			ResultSet rs = mysql.stmt.executeQuery("SELECT * from "
					+ mysql.getNoticesTableName() + " WHERE id=20");
			rs.next();
			assertEquals(n.getId(), rs.getInt("id"));
			assertEquals(n.getDate(), rs.getString("date"));
			assertEquals(n.getTitle(), rs.getString("title"));
			assertEquals(n.getContent(), rs.getString("content"));
			assertEquals(n.getContact(), rs.getString("contact"));
			assertEquals(n.getTelephone(), rs.getInt("telephone"));
			assertEquals(n.getVote(), rs.getInt("vote"));
			assertEquals(u.getId(), rs.getInt("users_id"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test    
	public void showgetann() {
		this.createTestUser();
		Notices n = this.getObject();
		Notices n1 = nm.showgetann((long) n.getId());
		assertEquals(n.getId(), n1.getId());
		assertEquals(n.getDate(), n1.getDate());
		assertEquals(n.getTitle(), n1.getTitle());
		assertEquals(n.getContent(), n1.getContent());
		assertEquals(n.getContact(), n1.getContact());
		assertEquals(n.getTelephone(), n1.getTelephone());
		assertEquals(n.getVote(), n1.getVote());
	}

	@After
	public void clean() {  
		try {
			mysql.stmt.executeUpdate("DELETE FROM " + mysql.getVotesTableName()
					+ " WHERE id_users=1");
			mysql.stmt.executeUpdate("DELETE FROM "
					+ mysql.getNoticesTableName() + " WHERE id=20");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
