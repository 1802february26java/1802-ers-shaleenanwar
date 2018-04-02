package com.revature.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.model.Employee;
import com.revature.model.EmployeeRole;
import com.revature.model.EmployeeToken;
import com.revature.util.ConnectionUtil;

public class EmployeeRepositoryJdbc implements EmployeeRepository {

	private static Logger logger = Logger.getLogger(EmployeeRepositoryJdbc.class);
	
	/*Singleton transformation of JDBC implementation object */
	private static EmployeeRepository employeeRepository;
	
	private EmployeeRepositoryJdbc() {
		
	}
	
	public static EmployeeRepository getInstance() {
		if(employeeRepository == null) {
			employeeRepository = new EmployeeRepositoryJdbc();
		}
		
		return employeeRepository;
	}
	
	@Override
	public boolean insert(Employee employee) {
		try(Connection connection = ConnectionUtil.getConnection()) {
			int statementIndex = 0;
			String command = "INSERT INTO USER_T VALUES(NULL,?,?,?,?,?,?)";

			PreparedStatement statement = connection.prepareStatement(command);

			//Set attributes to be inserted
			statement.setString(++statementIndex, employee.getFirstName().toUpperCase());
			statement.setString(++statementIndex, employee.getLastName().toUpperCase());
			statement.setString(++statementIndex, employee.getUsername().toLowerCase());
			statement.setString(++statementIndex, employee.getPassword());
			statement.setString(++statementIndex, employee.getEmail().toLowerCase());
			statement.setInt(++statementIndex, employee.getEmployeeRole().getId());
			
			if(statement.executeUpdate() > 0) {
				
				logger.trace("Successfully inserted new employee");
				return true;
			}
		} catch (SQLException e) {
			logger.warn("Exception inserting a new employee", e);
		}
		return false;
	}

	@Override
	public boolean update(Employee employee) {
		try (Connection connection = ConnectionUtil.getConnection())
		{
			final String command = "UPDATE USER_T SET U_FIRSTNAME = ?, U_LASTNAME = ? , U_EMAIL = ?, UR_ID = ? WHERE U_ID =  ?";
			PreparedStatement statement = connection.prepareStatement(command);
			int statementIndex = 0;

			statement.setString(++statementIndex, employee.getFirstName().toUpperCase());
			statement.setString(++statementIndex, employee.getLastName().toUpperCase());
			statement.setString(++statementIndex, employee.getEmail().toLowerCase());
			statement.setInt(++statementIndex, employee.getEmployeeRole().getId());
			statement.setInt(++statementIndex, employee.getId());
			
			if ( statement.executeUpdate() != 0 )
			{
				logger.trace("Successfully updated employee");
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e)
		{
			logger.warn("Exception updating employee", e);
		}
		return false;
	}

	@Override
	public Employee select(int employeeId) {
		
		try (Connection connection = ConnectionUtil.getConnection()){
			int statementIndex = 0;
			String sql = "SELECT * FROM USER_T INNER JOIN USER_ROLE ON USER_T.UR_ID = USER_ROLE.UR_ID WHERE U_ID = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(++statementIndex, employeeId);
			ResultSet result = statement.executeQuery();

			while(result.next()){
				return new Employee(

				result.getInt("U_ID"),
				result.getString("U_FIRSTNAME"),
				result.getString("U_LASTNAME"),
				result.getString("U_USERNAME"),
				result.getString("U_PASSWORD"),
				result.getString("U_EMAIL"),
				new EmployeeRole(
						result.getInt("UR_ID"),
						result.getString("UR_TYPE")
						)
				);
			}
			logger.trace("Successful Employee selection by employeee ID");
		} catch (SQLException e) {
			logger.warn("Exception selecting Employee by ID", e);
		}
		return null;
	}
	@Override
	public Employee select(String username) {
		try(Connection connection = ConnectionUtil.getConnection()) {
			int statementIndex = 0;
			String command = "SELECT * FROM USER_T INNER JOIN USER_ROLE ON USER_T.UR_ID = USER_ROLE.UR_ID WHERE USER_T.U_USERNAME = ?";
			PreparedStatement statement = connection.prepareStatement(command);
			statement.setString(++statementIndex, username);
			ResultSet result = statement.executeQuery();

			while(result.next()) {
				return new Employee(
						result.getInt("U_ID"),
						result.getString("U_FIRSTNAME"),
						result.getString("U_LASTNAME"),
						result.getString("U_USERNAME"),
						result.getString("U_PASSWORD"),
						result.getString("U_EMAIL"),
						new EmployeeRole(
								result.getInt("UR_ID"),
								result.getString("UR_TYPE")
								)
						);
			}
			logger.trace("Successful Employee selection by employeee username");
		} catch (SQLException e) {
			logger.warn("Exception selecting Employee by ID", e);
		}
		return null;
	}

	@Override
	public Set<Employee> selectAll(int num) {
		try(Connection connection = ConnectionUtil.getConnection()) {
			String command = "SELECT * FROM USER_T INNER JOIN USER_ROLE ON USER_T.UR_ID = USER_ROLE.UR_ID";
			PreparedStatement statement = connection.prepareStatement(command);
			
			ResultSet result = statement.executeQuery();
			Set<Employee> employeeSet = new HashSet<>();
			while(result.next()) {
				employeeSet.add(new Employee(
						result.getInt("U_ID"),
						result.getString("U_FIRSTNAME"),
						result.getString("U_LASTNAME"),
						result.getString("U_USERNAME"),
						result.getString("U_PASSWORD"),
						result.getString("U_EMAIL"),
						new EmployeeRole(
								result.getInt("UR_ID"),
								result.getString("UR_TYPE")
								)
						));
			}
			logger.trace("Successfully selected all employees");
			return employeeSet;
		} catch (SQLException e) {
			logger.warn("Exception selecting all employees", e);
		} 
		return null;
	}


	@Override
	public String getPasswordHash(Employee employee) {
		try(Connection connection = ConnectionUtil.getConnection()) {
			int statementIndex = 0;
			String command = "SELECT GET_HASH(?) AS HASH FROM DUAL";
			PreparedStatement statement = connection.prepareStatement(command);
			statement.setString(++statementIndex, employee.getPassword());
			ResultSet result = statement.executeQuery();

			if(result.next()) {
				return result.getString("HASH");
			}
			logger.trace("Successfully hashed employee password");
		} catch (SQLException e) {
			logger.warn("Exception getting employee password hash", e);
		} 
		return new String();
	}

	@Override
	public boolean insertEmployeeToken(EmployeeToken employeeToken) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteEmployeeToken(EmployeeToken employeeToken) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public EmployeeToken selectEmployeeToken(EmployeeToken employeeToken) {
		// TODO Auto-generated method stub
		return null;
	}

	

//	public static void main(String[] args) {
//		EmployeeRepository repository = EmployeeRepositoryJdbc.getInstance();
//		EmployeeRole employeeRole = new EmployeeRole(1, "EMPLOYEE");
//
//		Employee employee = new Employee(101,"Miley","Cyrus","mileycyrus","p4ssw0rd", "miley@gmail.com",employeeRole);
//
//		
//			logger.trace(repository.insert(employee));
//			employee.setEmail("new.example@gmail.com");
//			logger.trace(repository.update(employee));
//			logger.trace(repository.select(employee.getId()).toString());
//			logger.trace(repository.select("mileycyrus").toString());
//			logger.trace(repository.selectAll());
//			logger.trace(repository.getPasswordHash(employee));
//
//}

}