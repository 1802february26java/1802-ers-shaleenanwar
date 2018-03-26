package com.revature.service;

import java.time.LocalDateTime;
import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.model.Employee;
import com.revature.model.EmployeeRole;
import com.revature.model.EmployeeToken;
import com.revature.repository.EmployeeRepository;
import com.revature.repository.EmployeeRepositoryJdbc;

public class EmployeeServiceAlpha implements EmployeeService {
	
	private static EmployeeService service = new EmployeeServiceAlpha();
	private static Logger logger = Logger.getLogger(EmployeeServiceAlpha.class);
	private EmployeeRepository repository = EmployeeRepositoryJdbc.getInstance();
	
	private EmployeeServiceAlpha() {}
	
	public static EmployeeService getInstance() {
		return service;
	}

	@Override
	public Employee authenticate(Employee employee) {
		Employee loggedEmployee = repository.select(employee.getUsername());
		//logger.trace(loggedEmployee.getPassword());
		
		
		
		if (loggedEmployee != null && loggedEmployee.getPassword().equals(repository.getPasswordHash(employee))) {
			return loggedEmployee;
		}
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
