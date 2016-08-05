package com.easylearnjava.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.easylearnjava.util.Constants;

/**
 * 
 * @author rnr
 *
 */
public class LoginServlet extends HttpServlet {

	/**
	 * This is auto generated
	 */
	private static final long serialVersionUID = -8145663309844069243L;


	/**
	 * The request comes to this method when the login button is clicked
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html"); 
		
		try {
			// get the user entered input values from the "HttpServletRequest" object, i.e request
			String userNameStr = request.getParameter("usernameTB");
			String passwordStr = request.getParameter("passwordTB");

			
			
			// inputdata validation
			boolean isDataValid = isValidData(userNameStr, passwordStr);
			if (!isDataValid) {
				request.setAttribute("errMsg", Constants.LOGIN_DATA_VALIDATION_ERROR);
				request.getRequestDispatcher("/login.jsp").forward(request, response);
				return;
			}

			boolean isValid = isValidPassword(userNameStr, passwordStr);

			if (isValid) {
				request.setAttribute("reqName", userNameStr);
				request.getRequestDispatcher("/loginSuccess.jsp").forward(request, response);
				return;
			} else {
				request.setAttribute("errMsg", Constants.LOGIN_INVALID_CREDENTIALS);
				request.getRequestDispatcher("/login.jsp").forward(request, response);
				return;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(Constants.GLOBAL_EXCEPTION_MESSAGE);
			RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
			rd.forward(request, response);
			return;
		}
	}
	

	/**
	 * Method for validating the input values
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	public boolean isValidData(String userName, String password) {

		if ((null != userName) && (userName != "") && (userName.length() >= 5)) {
			if ((null != password) && (password != "") && (password.length() >= 5)) {
				return true;
			}
		}
		return false;
	}
	

	/**
	 * Method for implementing the business logic, like comparing the passwords
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	public boolean isValidPassword(String userName, String password) {

		String pwdFromDB = getUserPassword(userName);
		if (null != pwdFromDB) {
			if (pwdFromDB.equals(password)) {
				return true;
			}
		}
		return false;
	}
	

	/**
	 * Method which fetches password for the user name from the hashmap
	 * 
	 * @param userName
	 * @return
	 */
	public String getUserPassword(String userName) {
		String passwordFromDB = null;
		try{
				Connection conn = (Connection) getServletContext().getAttribute("DBConnectionKey");
				PreparedStatement pStmt = null;
				String sql = "SELECT * FROM  employee where emp_name = ? ";
				pStmt = conn.prepareStatement(sql);
				pStmt.setString(1, userName);
		
				ResultSet res = pStmt.executeQuery();
				while (res.next()) {
					passwordFromDB = res.getString("emp_password");
				}
		}catch(Exception e){
			e.printStackTrace();
		}
		return passwordFromDB;
	}

}
