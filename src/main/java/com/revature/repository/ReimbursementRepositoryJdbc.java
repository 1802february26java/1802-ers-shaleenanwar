package com.revature.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.model.Reimbursement;
import com.revature.model.ReimbursementStatus;
import com.revature.model.ReimbursementType;
import com.revature.util.ConnectionUtil;

public class ReimbursementRepositoryJdbc implements ReimbursementRepository {
	
	private static Logger logger = Logger.getLogger(ReimbursementRepositoryJdbc.class);
	
	/*Singleton transformation of JDBC implementation object */
	private static ReimbursementRepository reimbursementRepository;
	
	private ReimbursementRepositoryJdbc() {
		
	}
	
	public static ReimbursementRepository getInstance() {
		if(reimbursementRepository == null) {
			reimbursementRepository = new ReimbursementRepositoryJdbc();
		}
		
		return reimbursementRepository;
	}

//	Name          Null     Type           
//	------------- -------- -------------- 
//	R_ID          NOT NULL NUMBER         
//	R_REQUESTED   NOT NULL TIMESTAMP(6)   
//	R_RESOLVED             TIMESTAMP(6)   
//	R_AMOUNT      NOT NULL NUMBER(8,2)    
//	R_DESCRIPTION          VARCHAR2(4000) 
//	R_RECEIPT              BLOB           
//	EMPLOYEE_ID   NOT NULL NUMBER         
//	MANAGER_ID             NUMBER         
//	RS_ID         NOT NULL NUMBER         
//	RT_ID         NOT NULL NUMBER  
	
	@Override
	public boolean insert(Reimbursement reimbursement) {
		try(Connection connection = ConnectionUtil.getConnection()) {
			int statementIndex = 0;
			String command = "INSERT INTO REIMBURSEMENT VALUES(NULL, CURRENT_TIMESTAMP,NULL,?,?, NULL,?,?,?,?)";

			PreparedStatement statement = connection.prepareStatement(command);

			//Set attributes to be inserted

			statement.setDouble(++statementIndex, reimbursement.getAmount());
			statement.setString(++statementIndex, reimbursement.getDescription()); 
//			statement.setBlob(++statementIndex, (Blob) reimbursement.getReciept());
			statement.setInt(++statementIndex, reimbursement.getRequester().getId());
			statement.setInt(++statementIndex, reimbursement.getApprover().getId());
			statement.setInt(++statementIndex, reimbursement.getStatus().getId());
			statement.setInt(++statementIndex, reimbursement.getType().getId());
			
			if(statement.executeUpdate() > 0) {
				
				logger.trace("Successfully inserted new reimbursment request"); 
				return true;
			}
		} catch (SQLException e) {
			logger.warn("Exception inserting a new reimbursment request", e);
		}
		return false;
	}
	@Override
	public boolean update(Reimbursement reimbursement) {
		try (Connection connection = ConnectionUtil.getConnection()){	
			int statementIndex = 0;
			String command = "UPDATE REIMBURSEMENT SET R_RESOLVED = CURRENT_TIMESTAMP, MANAGER_ID = ?, RS_ID WHERE R_ID = ?";

			PreparedStatement statement = connection.prepareStatement(command);

			//Set attributes to be inserted
			statement.setInt(++statementIndex, reimbursement.getRequester().getId());
			statement.setInt(++statementIndex, reimbursement.getApprover().getId());
			statement.setInt(++statementIndex, reimbursement.getStatus().getId());
			
			if ( statement.executeUpdate() != 0 )
			{
				logger.trace("Successfully updated reimbursement request");
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e)
		{
			logger.warn("Exception updating reimbursement request", e);
		}
		return false;
	}

	@Override
	public Reimbursement select(int reimbursementId) {
		try (Connection connection = ConnectionUtil.getConnection()){
			int statementIndex = 0;
			String sql = "SELECT * FROM REIMBURSEMENT INNER JOIN REIMBURSEMENT_STATUS "
					+ "ON REIMBURSEMENT.RS_ID = REIMBURSEMENT_STATUS.RS_ID INNER JOIN REIMBURSEMENT_TYPE "
					+ "ON REIMBURSEMENT.RT_ID = REIMBURSEMENT_TYPE.RT_ID WHERE REIMBURSEMENT.R_ID = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(++statementIndex, reimbursementId);
			ResultSet result = statement.executeQuery();

			if (result.next()) {
				Reimbursement reimbursement = new Reimbursement(
						result.getInt("R_ID"),
						result.getTimestamp("R_REQUESTED").toLocalDateTime(),
						null,
						result.getDouble("R_AMOUNT"),
						result.getString("R_DESCRIPTION"),
						EmployeeRepositoryJdbc.getInstance().select(result.getInt("EMPLOYEE_ID")),
						EmployeeRepositoryJdbc.getInstance().select(result.getInt("MANAGER_ID")),
						new ReimbursementStatus(
								result.getInt("RS_ID"),
								result.getString("RS_STATUS")
								),
						new ReimbursementType(
								result.getInt("RT_ID"),
								result.getString("RT_TYPE")
								)
						);
				if (result.getString("R_RESOLVED") != null) {
					reimbursement.setResolved(result.getTimestamp("R_RESOLVED").toLocalDateTime());
				}
				return reimbursement;
			}
			logger.trace("Successfully selected reimbursement request by employee ID");
		} catch (SQLException e) {
			logger.warn("Exception selecting reimbursement request by employee ID", e);
		}
		return new Reimbursement();
	}

	@Override
	public Set<Reimbursement> selectPending(int employeeId) {		
		try (Connection connection = ConnectionUtil.getConnection()){
			int statementIndex = 0;
			String sql = "SELECT * FROM REIMBURSEMENT INNER JOIN REIMBURSEMENT_STATUS "
					+ "ON REIMBURSEMENT.RS_ID = REIMBURSEMENT_STATUS.RS_ID INNER JOIN REIMBURSEMENT_TYPE "
					+ "ON REIMBURSEMENT.RT_ID = REIMBURSEMENT_TYPE.RT_ID WHERE REIMBURSEMENT.EMPLOYEE_ID = ? "
					+ "AND REIMBURSEMENT.RS_ID = ?";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(++statementIndex, employeeId);
			statement.setInt(++statementIndex, 1);
			
			ResultSet result = statement.executeQuery();
			Set<Reimbursement> reimbursementSet = new HashSet<>();
			

			while(result.next()){
				reimbursementSet.add(new Reimbursement(
						result.getInt("R_ID"),
						result.getTimestamp("R_REQUESTED").toLocalDateTime(),
						null,
						result.getDouble("R_AMOUNT"),
						result.getString("R_DESCRIPTION"),
						EmployeeRepositoryJdbc.getInstance().select(result.getInt("EMPLOYEE_ID")),
						EmployeeRepositoryJdbc.getInstance().select(result.getInt("MANAGER_ID")),
						new ReimbursementStatus(
								result.getInt("RS_ID"),
								result.getString("RS_STATUS")
								),
						new ReimbursementType(
								result.getInt("RT_ID"),
								result.getString("RT_TYPE")
								)
						));
			}	
			logger.trace("Successfully selected pending reimbursement request by employee ID");
				return reimbursementSet;
		} catch (SQLException e) {
			logger.warn("Exception selecting pending reimbursement request by employee ID", e);
		} 
		return new HashSet<>();
	}

	@Override
	public Set<Reimbursement> selectFinalized(int employeeId) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			int statementIndex = 0;
			String sql = "SELECT * FROM REIMBURSEMENT INNER JOIN REIMBURSEMENT_STATUS "
					+ "ON REIMBURSEMENT.RS_ID = REIMBURSEMENT_STATUS.RS_ID INNER JOIN REIMBURSEMENT_TYPE "
					+ "ON REIMBURSEMENT.RT_ID = REIMBURSEMENT_TYPE.RT_ID WHERE REIMBURSEMENT.EMPLOYEE_ID = ? "
					+ "AND (REIMBURSEMENT.RS_ID = ? OR REIMBURSEMENT.RS_ID = ?)";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(++statementIndex, employeeId);
			statement.setInt(++statementIndex, 2);
			statement.setInt(++statementIndex, 3);
			
			ResultSet result = statement.executeQuery();
			Set<Reimbursement> reimbursementSet = new HashSet<>();

			while (result.next()) {
				reimbursementSet.add(new Reimbursement(
						result.getInt("R_ID"),
						result.getTimestamp("R_REQUESTED").toLocalDateTime(),
						null,
						result.getDouble("R_AMOUNT"),
						result.getString("R_DESCRIPTION"),
						EmployeeRepositoryJdbc.getInstance().select(result.getInt("EMPLOYEE_ID")),
						EmployeeRepositoryJdbc.getInstance().select(result.getInt("MANAGER_ID")),
						new ReimbursementStatus(
								result.getInt("RS_ID"),
								result.getString("RS_STATUS")
								),
						new ReimbursementType(
								result.getInt("RT_ID"),
								result.getString("RT_TYPE")
								)
						)
						);
			}
			logger.trace("Successfully selected finalized reimbursement request by employee ID");
				return reimbursementSet;
		} catch (SQLException e) {
			logger.warn("Exception selecting finalized reimbursement request by employee ID", e);
		} 
		return new HashSet<>();
	}

	@Override
	public Set<Reimbursement> selectAllPending() {
		try (Connection connection = ConnectionUtil.getConnection()) {
			int statementIndex = 0;
			String sql = "SELECT * FROM REIMBURSEMENT INNER JOIN REIMBURSEMENT_STATUS "
					+ "ON REIMBURSEMENT.RS_ID = REIMBURSEMENT_STATUS.RS_ID INNER JOIN REIMBURSEMENT_TYPE "
					+ "ON REIMBURSEMENT.RT_ID = REIMBURSEMENT_TYPE.RT_ID WHERE REIMBURSEMENT.RS_ID = ?";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(++statementIndex, 1);

			ResultSet result = statement.executeQuery();
			Set<Reimbursement> reimbursementSet = new HashSet<>();

			while (result.next()) {
				reimbursementSet.add(new Reimbursement(
						result.getInt("R_ID"),
						result.getTimestamp("R_REQUESTED").toLocalDateTime(),
						null,
						result.getDouble("R_AMOUNT"),
						result.getString("R_DESCRIPTION"),
						EmployeeRepositoryJdbc.getInstance().select(result.getInt("EMPLOYEE_ID")),
						EmployeeRepositoryJdbc.getInstance().select(result.getInt("MANAGER_ID")),
						new ReimbursementStatus(
								result.getInt("RS_ID"),
								result.getString("RS_STATUS")
								),
						new ReimbursementType(
								result.getInt("RT_ID"),
								result.getString("RT_TYPE")
								)
						));
				logger.trace("Successfully selected all pending reimbursement requests");
			}
				return reimbursementSet;
		} catch (SQLException e) {
			logger.warn("Exception selecting all pending reimbursement requests", e);
		} 
		return new HashSet<>();
	}
	@Override
	public Set<Reimbursement> selectAllFinalized() {
		try (Connection connection = ConnectionUtil.getConnection()) {
			int statementIndex = 0;
			String command = "SELECT * FROM REIMBURSEMENT R INNER JOIN REIMBURSEMENT_STATUS RS "
					+ "ON R.RS_ID = RS.RS_ID INNER JOIN REIMBURSEMENT_TYPE RT ON R.RT_ID = RT.RT_ID "
					+ "WHERE R.RS_ID = ? OR R.RS_ID = ?";

			PreparedStatement statement = connection.prepareStatement(command);
			statement.setInt(++statementIndex, 2);
			statement.setInt(++statementIndex, 3);

			ResultSet result = statement.executeQuery();
			Set<Reimbursement> reimbursementSet = new HashSet<>();

			while (result.next()) {
				reimbursementSet.add(new Reimbursement(
						result.getInt("R_ID"),
						result.getTimestamp("R_REQUESTED").toLocalDateTime(),
						null,
						result.getDouble("R_AMOUNT"),
						result.getString("R_DESCRIPTION"),
						EmployeeRepositoryJdbc.getInstance().select(result.getInt("EMPLOYEE_ID")),
						EmployeeRepositoryJdbc.getInstance().select(result.getInt("MANAGER_ID")),
						new ReimbursementStatus(
								result.getInt("RS_ID"),
								result.getString("RS_STATUS")
								),
						new ReimbursementType(
								result.getInt("RT_ID"),
								result.getString("RT_TYPE")
								)
						));
				logger.trace("Successfully selected all finalized reimbursement requests");
			}
			return reimbursementSet;
		} catch (SQLException e) {
			logger.warn("Exception selecting all finalized reimbursement requests", e);
		} 
		return new HashSet<>();
	}

	@Override
	public Set<ReimbursementType> selectTypes() {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String command = "SELECT * FROM REIMBURSEMENT_TYPE";

			PreparedStatement statement = connection.prepareStatement(command);

			ResultSet result = statement.executeQuery();
			Set<ReimbursementType> reimbursementTypes = new HashSet<>();

			while (result.next()) {
				reimbursementTypes.add(new ReimbursementType(
						result.getInt("RT_ID"),
						result.getString("RT_TYPE")
						));
				logger.trace("Successfully selected reimbursement type");
			}
			return reimbursementTypes;
		} catch (SQLException e) {
			logger.warn("Exception selecting reimbursement type", e);
		} 
		return new HashSet<>();
	}
}