package gryczko.jaroslaw.tests;

import static org.junit.Assert.*;
import gryczko.jaroslaw.domain.Mysql;

import org.junit.Test;


public class TestMysqlTest {

	@Test
	public void connectionTest() {
		Mysql mysql = new Mysql();
		assertNotNull(mysql.con);
		assertNotNull(mysql.stmt);
		mysql.closeconnection();
	}
}
