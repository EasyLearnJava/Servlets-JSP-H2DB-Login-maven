package com.easylearnjava.listener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.easylearnjava.util.DBConnectionUtil;

//Create tables and required data during application start up 
public class AppContextListener implements ServletContextListener{

	private final String CREATE_TABLE_SQL = "CREATE TABLE EMPLOYEE(EMP_ID INT PRIMARY KEY, EMP_NAME VARCHAR(255), EMP_PASSWORD VARCHAR(255));";
	private final String INSERT_RECORD1_SQL = "INSERT INTO EMPLOYEE (EMP_ID, EMP_NAME, EMP_PASSWORD) VALUES (?,?,?)";
	private final String INSERT_RECORD2_SQL = "INSERT INTO EMPLOYEE (EMP_ID, EMP_NAME, EMP_PASSWORD) VALUES (2,'naveen','topsecret')";

	public void contextInitialized(ServletContextEvent scEvent) {
		Connection conn = null;
		PreparedStatement pStmt = null;
		
		try {			
			//Get a DB connection
			conn = DBConnectionUtil.getH2DBConnectionObj();

			//Place the connection object in ServletContext so that other classes can get it from the servlet context when required
			ServletContext context = scEvent.getServletContext();
			context.setAttribute("DBConnectionKey", conn);
			
			pStmt = conn.prepareStatement(CREATE_TABLE_SQL);
			pStmt.execute();

			pStmt = conn.prepareStatement(INSERT_RECORD1_SQL);
			pStmt.setInt(1, 1);
			pStmt.setString(2, "raghu");
			pStmt.setString(3, "secret");
			pStmt.executeUpdate();
			
			pStmt = conn.prepareStatement(INSERT_RECORD2_SQL);
			pStmt.executeUpdate();

			pStmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//Closing the db connection when the context is destroyed
	public void contextDestroyed(ServletContextEvent scEvent) {
		Connection con = (Connection) scEvent.getServletContext().getAttribute("DBConnectionKey");
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
