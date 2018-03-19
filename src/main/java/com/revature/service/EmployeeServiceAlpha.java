package com.revature.service;

import java.util.Set;

import com.revature.model.Employee;
import com.revature.model.EmployeeToken;
import com.revature.repository.EmployeeRepositoryJdbc;

public class EmployeeServiceAlpha implements EmployeeService {

	/*Singleton*/
	private static EmployeeService employeeService = new EmployeeServiceAlpha();
	
	private EmployeeServiceAlpha() { }
	
	public static EmployeeService getInstance() {
		return employeeService;
	}
		
	@Override
	public Employee authenticate(Employee employee) {
//		//Information on the database
//		Employee loggedEmployee = EmployeeRepositoryJdbc.getInstance().select(employee);
//		/*
//		 * What we have stored in the database is the Username + Password hash
//		 * We can't compare the blank password provided by the user, against the hash.
//		 * So we have to obtain the hash of the user input.
//		 * 
//		 * If the hashes are the same, user is authenticated.
//		 */
//		
//		if(loggedEmployee.getPassword().equals(EmployeeRepositoryJdbc.getInstance().getCustomerHash(customer))){
//			return loggedEmployee;
//		}
		return null;
	}

	@Override
	public Employee getEmployeeInformation(Employee employee) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Employee> getAllEmployeesInformation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean createEmployee(Employee employee) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateEmployeeInformation(Employee employee) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updatePassword(Employee employee) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUsernameTaken(Employee employee) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createPasswordToken(Employee employee) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deletePasswordToken(EmployeeToken employeeToken) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTokenExpired(EmployeeToken employeeToken) {
		// TODO Auto-generated method stub
		return false;
	}

}
