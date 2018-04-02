package com.revature.service;

import java.sql.SQLException;
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
		if (employee.getId() != 0){
			int employeeID = employee.getId();
			Employee emp = repository.select(employeeID);
			logger.info("Employee " + employeeID + " acquired.");
			return emp;
		}
		logger.error("Error acquiring employee information. EmployeeServiceAlpha.getEmployeeInformation.");
		return null;
	}

	@Override
	public Set<Employee> getAllEmployeesInformation(int num) {
		Set<Employee> allEmployees = repository.selectAll(num);
		if (allEmployees.size() > 0){
			logger.info("All employees Acquired.");
			logger.trace(allEmployees);
			return allEmployees;
		}
		logger.error("Error acquiring all Employees Information. EmployeeServiceAlpha.getAllEmployeesInformation.");
		return null;
	}

	@Override
	public boolean createEmployee(Employee employee) {
		return repository.insert(employee);
	}

	@Override
	public boolean updateEmployeeInformation(Employee employee) {
		if (repository.update(employee)){
			logger.info("Employee Updated EmployeeServiceAlpha.updateEmployee!");
			return true;
		} else {
			logger.error("Employee update failed. =(");
			return false;
		}
		
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
