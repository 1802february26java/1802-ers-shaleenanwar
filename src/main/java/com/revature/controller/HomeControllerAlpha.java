package com.revature.controller;

import javax.servlet.http.HttpServletRequest;

import com.revature.model.Employee;

public class HomeControllerAlpha implements HomeController {
	
	private static HomeController homeController = new HomeControllerAlpha();
    
	private HomeControllerAlpha() {}
    
    public static HomeController getInstance() {
        return homeController;
    }
	

	@Override
	public String showEmployeeHome(HttpServletRequest request) {
		Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");
		
		/* If customer is not logged in */
		if(loggedEmployee == null) {
			return "login.html";
		}
		
		return "home.html";
	}
}

