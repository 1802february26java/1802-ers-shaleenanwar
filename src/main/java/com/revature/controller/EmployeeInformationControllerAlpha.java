package com.revature.controller;

import javax.servlet.http.HttpServletRequest;

import com.revature.ajax.ClientMessage;
import com.revature.model.Employee;
import com.revature.model.EmployeeRole;
import com.revature.service.EmployeeServiceAlpha;
import com.revature.util.FinalUtil;

public class EmployeeInformationControllerAlpha implements EmployeeInformationController {

	private static EmployeeInformationController employeeInformationController = new EmployeeInformationControllerAlpha();

    private EmployeeInformationControllerAlpha() {}

    public static EmployeeInformationController getInstance(){
        return employeeInformationController;
    }
    
	@Override
	public Object registerEmployee(HttpServletRequest request) {
		if(request.getMethod().equals("GET")) {
			return "register.html";
		}
		
		//Logic for POST
		
		Employee registeredEmployee =new Employee(0,
				request.getParameter("firstName"),
				request.getParameter("lastName"),
				request.getParameter("username"),
				request.getParameter("password"),
				request.getParameter("email"),
				new EmployeeRole(1, "Employee")
				); 
				
		
		if (EmployeeServiceAlpha.getInstance().createEmployee(registeredEmployee)){
			return new ClientMessage(FinalUtil.CLIENT_MESSAGE_REGISTRATION_SUCCESSFUL);
		} else {
			return new ClientMessage(FinalUtil.CLIENT_MESSAGE_SOMETHING_WRONG);
		}
	}

	@Override
	public Object updateEmployee(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object viewEmployeeInformation(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object viewAllEmployees(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object usernameExists(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
