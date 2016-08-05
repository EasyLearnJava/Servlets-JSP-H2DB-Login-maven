package com.easylearnjava.util;

import java.sql.Connection;
import java.sql.DriverManager;

import org.h2.tools.Server;

public class DBConnectionUtil {
	
	private static final String DB_DRIVER = "org.h2.Driver";
	private static final String DB_URL = "jdbc:h2:mem:ATHENA;DB_CLOSE_DELAY=-1";
	private static final String DB_USER = "";
	private static final String DB_PASSWORD = "";
	static Server server = null;

	public static Connection getH2DBConnectionObj() {
		Connection conn = null;
		try {
			server = Server.createTcpServer().start();
			System.out.println("Server DB URL for Reference : " + server.getURL());
			Class.forName(DB_DRIVER);
			conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}
