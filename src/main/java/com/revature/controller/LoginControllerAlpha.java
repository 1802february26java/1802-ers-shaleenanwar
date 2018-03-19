package com.revature.controller;

import javax.servlet.http.HttpServletRequest;

import com.revature.model.Employee;
import com.revature.service.EmployeeService;

public class LoginControllerAlpha implements LoginController {

	//Singleton Logic
	
	private static LoginController loginController = new LoginControllerAlpha();
	
	public LoginControllerAlpha() {}
	
	public static LoginController getInstance() {
		return loginController;
	}
	
	@Override
	public String login(HttpServletRequest request) {
//		if(request.getMethod().equals("GET")) {
			return "login.html";
		}
		
//		Employee loggedEmployee = EmployeeService.getInstance().authenticate(
//				new Employee(request.getParameter("username"),
//							 request.getParameter("password"))
//				);
//		
//		/* If authentication failed */
//		if(loggedCustomer == null) {
//			return new ClientMessage("AUTHENTICATION FAILED");
//		}
//		
//		/* Store the customer information on the session */
//		request.getSession().setAttribute("loggedEmployee", loggedEmployee);
//		return loggedEmployee;
//	}


	@Override
	public String logout(HttpServletRequest request) {
		return "login.html";
	}

}
