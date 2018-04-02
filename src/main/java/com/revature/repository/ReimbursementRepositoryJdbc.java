package com.revature.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.model.Employee;
import com.revature.model.EmployeeRole;
import com.revature.model.Reimbursement;
import com.revature.model.ReimbursementStatus;
import com.revature.model.ReimbursementType;
import com.revature.util.ConnectionUtil;

public class ReimbursementRepositoryJdbc implements ReimbursementRepository {
	private static ReimbursementRepositoryJdbc repository = new ReimbursementRepositoryJdbc();
	private static Logger logger = Logger.getLogger(EmployeeRepositoryJdbc.class);

	// Constructor
	private ReimbursementRepositoryJdbc() {}

	// Make it Singleton
	public static ReimbursementRepositoryJdbc getInstance(){		
		return repository;
	}

	@Override
	public boolean insert(Reimbursement reimbursement) {
		logger.trace("Inserting new reinbursement.");

		try (Connection connection = ConnectionUtil.getConnection()){
			int parameterIndex = 0;
			String sql = "INSERT INTO REIMBURSEMENT(R_ID, R_REQUESTED, R_RESOLVED, R_AMOUNT, R_DESCRIPTION, EMPLOYEE_ID, MANAGER_ID, RS_ID, RT_ID) VALUES (NULL,?, NULL,?,?,?,NULL,?,?)";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setTimestamp(++parameterIndex, Timestamp.valueOf(reimbursement.getRequested()));
			statement.setDouble(++parameterIndex, reimbursement.getAmount());
			statement.setString(++parameterIndex, reimbursement.getDescription());
			statement.setInt(++parameterIndex, reimbursement.getRequester().getId());
			statement.setInt(++parameterIndex, reimbursement.getStatus().getId());
			statement.setInt(++parameterIndex, reimbursement.getType().getId());

			if (statement.executeUpdate() > 0) {
				logger.info("Insert successful!");
				return true;
			} else {
				throw new SQLException();
			}
		} catch (SQLException e) {
			logger.error("Exception at ReimbursementRepositoryJDBC.insert", e);}
		return false;
	}

	@Override
	public boolean update(Reimbursement reimbursement){
		logger.trace("Updating reimbursement");

		try (Connection connection = ConnectionUtil.getConnection()) {
			int parameterIndex = 0;
			String sql = "UPDATE REIMBURSEMENT SET R_RESOLVED = ?, R_AMOUNT = ?, R_DESCRIPTION = ?, MANAGER_ID = ?, RS_ID = ?, RT_ID = ? WHERE R_ID = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			
			statement.setTimestamp(++parameterIndex, Timestamp.valueOf(reimbursement.getResolved()));
			statement.setDouble(++parameterIndex, reimbursement.getAmount());
			statement.setString(++parameterIndex, reimbursement.getDescription());
			statement.setInt(++parameterIndex, reimbursement.getApprover().getId());
			statement.setInt(++parameterIndex,  reimbursement.getStatus().getId());
			statement.setInt(++parameterIndex, reimbursement.getType().getId());
			statement.setInt(++parameterIndex, reimbursement.getId());
			if (statement.executeUpdate() > 0){
				logger.info("Update Successful!");
				return true;
			}
		} catch(SQLException e) {
			logger.error("Error at ReimbursementRepositoryJDBC.update");
		}
		return false;
	}

	@Override
	public Reimbursement select(int reimbursementId){
		logger.trace("Grabbing reinbursement by ID");
		try (Connection connection = ConnectionUtil.getConnection()){
			int parameterIndex = 0;
			String sql = "SELECT * FROM REIMBURSEMENT INNER JOIN REIMBURSEMENT_STATUS ON REIMBURSEMENT.RS_ID = REIMBURSEMENT_STATUS.RS_ID INNER JOIN REIMBURSEMENT_TYPE ON REIMBURSEMENT.RT_ID = REIMBURSEMENT_TYPE.RT_ID WHERE REIMBURSEMENT.R_ID = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(++parameterIndex, reimbursementId);
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
				logger.info("Reinbursement Successful!");
				return reimbursement;
			}
		} catch (SQLException e) {
			logger.error("Exception at ReimbursementRepositoryJDBC.select", e);
		}
		return null;
	}

	@Override
	public Set<Reimbursement> selectPending(int employeeId) {
		logger.trace("Grabbing pending reimbursements");
		try (Connection connection = ConnectionUtil.getConnection()) {
			int parameterIndex = 0;
			String sql = "SELECT * FROM REIMBURSEMENT INNER JOIN REIMBURSEMENT_STATUS ON REIMBURSEMENT.RS_ID = REIMBURSEMENT_STATUS.RS_ID INNER JOIN REIMBURSEMENT_TYPE ON REIMBURSEMENT.RT_ID = REIMBURSEMENT_TYPE.RT_ID WHERE REIMBURSEMENT.EMPLOYEE_ID = ? AND REIMBURSEMENT.RS_ID = ?";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(++parameterIndex, employeeId);
			statement.setInt(++parameterIndex, 1);

			ResultSet result = statement.executeQuery();
			Set<Reimbursement> reimbursements = new HashSet<>();
			logger.trace(reimbursements);
			while (result.next()) {
				reimbursements.add(new Reimbursement(
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
			logger.info("Selected pending requests successful!");
			return reimbursements;

		} catch (SQLException e) {
			logger.error("Exception at ReimbursementRepositoryJDBC.selectPending.", e);
		}
		return null;
	}

	@Override
	public Set<Reimbursement> selectFinalized(int employeeId) {
		logger.trace("Grabbing finalized reimbursements");
		try (Connection connection = ConnectionUtil.getConnection()) {
			int parameterIndex = 0;
			String sql = "SELECT * FROM REIMBURSEMENT INNER JOIN REIMBURSEMENT_STATUS ON REIMBURSEMENT.RS_ID = REIMBURSEMENT_STATUS.RS_ID INNER JOIN REIMBURSEMENT_TYPE ON REIMBURSEMENT.RT_ID = REIMBURSEMENT_TYPE.RT_ID WHERE REIMBURSEMENT.EMPLOYEE_ID = ? AND (REIMBURSEMENT.RS_ID = ? OR REIMBURSEMENT.RS_ID = ?)";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(++parameterIndex, employeeId);
			statement.setInt(++parameterIndex, 2);
			statement.setInt(++parameterIndex, 3);
			ResultSet result = statement.executeQuery();
			Set<Reimbursement> reimbursements = new HashSet<>();

			while (result.next()) {
				reimbursements.add(new Reimbursement(
						result.getInt("R_ID"),
						result.getTimestamp("R_REQUESTED").toLocalDateTime(),
						result.getTimestamp("R_RESOLVED").toLocalDateTime(),
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
			logger.info("All finalized reimbursements selected!");
			logger.info(reimbursements);
			return reimbursements;

		} catch (SQLException e) {
			logger.error("Exception at ReimbursementRepositoryJDBC.selectFinalized", e);
		}
		return null;
	}

	@Override
	public Set<Reimbursement> selectAllPending() {
		logger.trace("Grabbing pending reimbursements");
		try (Connection connection = ConnectionUtil.getConnection()) {
			int parameterIndex = 0;
			String sql = "SELECT * FROM REIMBURSEMENT INNER JOIN REIMBURSEMENT_STATUS ON REIMBURSEMENT.RS_ID = REIMBURSEMENT_STATUS.RS_ID INNER JOIN REIMBURSEMENT_TYPE ON REIMBURSEMENT.RT_ID = REIMBURSEMENT_TYPE.RT_ID WHERE REIMBURSEMENT.RS_ID = ?";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(++parameterIndex, 1);

			ResultSet result = statement.executeQuery();
			Set<Reimbursement> reimbursements = new HashSet<>();

			while (result.next()) {
				reimbursements.add(new Reimbursement(
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
			logger.trace("All pending reimbursements selected!");
			return reimbursements;

		} catch (SQLException e) {
			logger.info("Exception at ReimbursementRepositoryJDBC.selectAllPending.", e);
		}
		return null;
	}

	@Override
	public Set<Reimbursement> selectAllFinalized() {
		logger.trace("Grabbing finalized reimbursements");
		try (Connection connection = ConnectionUtil.getConnection()) {
			int parameterIndex = 0;
			String sql = "SELECT * FROM REIMBURSEMENT INNER JOIN REIMBURSEMENT_STATUS ON REIMBURSEMENT.RS_ID = REIMBURSEMENT_STATUS.RS_ID INNER JOIN REIMBURSEMENT_TYPE ON REIMBURSEMENT.RT_ID = REIMBURSEMENT_TYPE.RT_ID WHERE (REIMBURSEMENT.RS_ID = ? OR REIMBURSEMENT.RS_ID = ?)";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(++parameterIndex, 2);
			statement.setInt(++parameterIndex, 3);
			ResultSet result = statement.executeQuery();
			Set<Reimbursement> reimbursements = new HashSet<>();

			while (result.next()) {
				reimbursements.add(new Reimbursement(
						result.getInt("R_ID"),
						result.getTimestamp("R_REQUESTED").toLocalDateTime(),
						result.getTimestamp("R_RESOLVED").toLocalDateTime(),
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
			logger.info("All finalized reimbursements selected.");
			return reimbursements;

		} catch (SQLException e) {
			logger.error("Exception at ReimbursementRepositoryJDBC.selectAllFinalized.", e);
		}
		return null;
	}

	@Override
	public Set<ReimbursementType> selectTypes() {
		logger.trace("ReimbursementRepositoryJDBC.selectTypes");
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM REIMBURSEMENT_TYPE";
			PreparedStatement statement = connection.prepareStatement(sql);

			ResultSet result = statement.executeQuery();
			Set<ReimbursementType> reimbursementTypes = new HashSet<>();

			while(result.next()) {
				reimbursementTypes.add(new ReimbursementType(
						result.getInt("RT_ID"),
						result.getString("RT_TYPE")
						));
			}
			logger.info("Selected all types of reimbursements.");
			return reimbursementTypes;
		} catch (SQLException e) {
			logger.error("Exception at ReimbursementRepositoryJDBC.selectTypes");
		}
		return null;
	}
}