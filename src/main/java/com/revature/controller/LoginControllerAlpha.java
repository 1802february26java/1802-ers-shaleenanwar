package com.revature.controller;

import javax.servlet.http.HttpServletRequest;

import com.revature.ajax.ClientMessage;
import com.revature.model.Employee;
import com.revature.service.EmployeeServiceAlpha;

public class LoginControllerAlpha implements LoginController {

	//Singleton Logic
	
	private static LoginController loginController = new LoginControllerAlpha();
	
	private LoginControllerAlpha() {}
	
	public static LoginController getInstance() {
		return loginController;
	}
	
	@Override
	public Object login(HttpServletRequest request) {
		
		if (request.getMethod().equals("GET")) {
			return "login.html";
		}
		
		Employee loggedEmployee = new Employee();
		loggedEmployee.setUsername(request.getParameter("username"));
		loggedEmployee.setPassword(request.getParameter("password"));
		loggedEmployee = EmployeeServiceAlpha.getInstance().authenticate(loggedEmployee);
		
		if (loggedEmployee == null) {
			return new ClientMessage("Authentication Failed");
		} 
		
		/* Store the customer information on the session */
		request.getSession().setAttribute("loggedEmployee", loggedEmployee);
		return loggedEmployee;
	}



	@Override
	public String logout(HttpServletRequest request) {
		request.getSession().invalidate();
		return "login.html";
	}

}
