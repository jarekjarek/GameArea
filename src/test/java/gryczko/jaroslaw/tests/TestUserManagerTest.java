package gryczko.jaroslaw.tests;


import static org.junit.Assert.*;

import gryczko.jaroslaw.domain.Mysql;
import gryczko.jaroslaw.domain.User;
import gryczko.jaroslaw.service.UserManagerImpl;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.mail.MessagingException;

import org.junit.After;
import org.junit.Test;

public class TestUserManagerTest {
	Mysql mysql = new Mysql();
	User user = new User();
	UserManagerImpl um = new UserManagerImpl();

	@Test
	
	public void aadminTest() {

		boolean check = um.aadmin("goon");
		assertTrue(check);
		try {
			ResultSet rs = mysql.stmt.executeQuery("SELECT admin from "
					+ mysql.getUserTableName() + " WHERE login='goon'");
			rs.next();
			assertEquals(1, rs.getInt("admin"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	
	public void auserTest() {

		boolean check = um.auser("goon");
		assertTrue(check);
		try {
			ResultSet rs = mysql.stmt.executeQuery("SELECT admin from "
					+ mysql.getUserTableName() + " WHERE login='goon'");
			rs.next();
			assertEquals(0, rs.getInt("admin"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	
	public void addUserTest() {
		user.setDob("2010-10-28");
		user.setEmail("tescik@tescik.pl");
		user.setFname("tescik");
		user.setLname("tescik");
		user.setLogin("tescik");
		user.setPassword("tescik1234");
		user.setTelephone(123456789);
		boolean check = um.addUser(user, false);
		assertTrue(check);
		try {
			ResultSet rs = mysql.stmt
					.executeQuery("SELECT fname, lname, login, email, password, dob, telephone from "
							+ mysql.getUserTableName()
							+ " WHERE login='"
							+ user.getLogin() + "'");
			rs.next();
			assertEquals(user.getDob(), rs.getString("dob"));
			assertEquals(user.getEmail(), rs.getString("email"));
			assertEquals(user.getFname(), rs.getString("fname"));
			assertEquals(user.getLname(), rs.getString("lname"));
			assertEquals(user.getLogin(), rs.getString("login"));
			assertEquals(user.getTelephone(), rs.getInt("telephone"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	
	public void addExistUserTest() {
		int iterator = 0;
		user.setDob("2010-10-28");
		user.setEmail("tescik@tescik.pl");
		user.setFname("tescik");
		user.setLname("tescik");
		user.setLogin("tescik");
		user.setPassword("tescik1234");
		user.setTelephone(123456789);
		um.addUser(user, true);
		boolean check = um.addUser(user, true);
		assertFalse(check);
		try {
			ResultSet rs = mysql.stmt.executeQuery("SELECT id from "
					+ mysql.getUserTableName() + " WHERE login='"
					+ user.getLogin() + "'");
			while (rs.next()) {
				++iterator;
			}
			assertTrue(iterator == 1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	
	public void adeleteTest() {
		int iterator = 0;
		user.setDob("2010-10-28");
		user.setEmail("tescik@tescik.pl");
		user.setFname("tescik");
		user.setLname("tescik");
		user.setLogin("tescik");
		user.setPassword("tescik1234");
		user.setTelephone(123456789);
		um.addUser(user, true);
		boolean check = um.adelete(user.getLogin());
		assertTrue(check);
		try {
			ResultSet rs = mysql.stmt.executeQuery("SELECT id from "
					+ mysql.getUserTableName() + " WHERE login='"
					+ user.getLogin() + "'");
			while (rs.next()) {
				++iterator;
			}
			assertTrue(iterator == 0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	
	public void updateTest() {
		user.setDob("2010-10-28");
		user.setEmail("tescik@tescik.pl");
		user.setFname("tescik");
		user.setLname("tescik");
		user.setLogin("tescik");
		user.setPassword("tescik1234");
		user.setTelephone(123456789);
		um.addUser(user, true);
		user.setFname("zmienionefname");
		boolean check = um.update(user);
		assertTrue(check);
		try {
			ResultSet rs = mysql.stmt.executeQuery("SELECT fname from "
					+ mysql.getUserTableName() + " WHERE login='"
					+ user.getLogin() + "'");
			rs.next();
			assertEquals(user.getFname(), rs.getString("fname"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test

	public void showgetUserTest() {
		user.setDob("2010-10-28");
		user.setEmail("tescik@tescik.pl");
		user.setFname("tescik");
		user.setLname("tescik");
		user.setLogin("tescik");
		user.setPassword("tescik1234");
		user.setTelephone(123456789);
		um.addUser(user, true);
		User check = um.showgetuser(user.getLogin());
		assertEquals(user.getDob(), check.getDob());
		assertEquals(user.getFname(), check.getFname());
		assertEquals(user.getLname(), check.getLname());
		assertEquals(user.getLogin(), check.getLogin());
	}

	@After
	public void clean() { 
		try {
			mysql.stmt.executeUpdate("DELETE FROM votes WHERE id_users > 15");
			mysql.stmt.executeUpdate("DELETE FROM "
					+ mysql.getSessionTableName() + " WHERE users_id > 15");
			mysql.stmt.executeUpdate("DELETE FROM "
					+ mysql.getNoticesTableName() + " WHERE users_id > 15");
			mysql.stmt.executeUpdate("DELETE FROM " + mysql.getUserTableName()
					+ " WHERE id > 15");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
