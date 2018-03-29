package com.revature.service;

import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.model.Employee;
import com.revature.model.Reimbursement;
import com.revature.model.ReimbursementType;
import com.revature.repository.ReimbursementRepository;
import com.revature.repository.ReimbursementRepositoryJdbc;

public class ReimbursementServiceAlpha implements ReimbursementService {

	private static ReimbursementService service = new ReimbursementServiceAlpha();
	private static Logger logger = Logger.getLogger(ReimbursementServiceAlpha.class);
	private ReimbursementRepository repository = ReimbursementRepositoryJdbc.getInstance();
	
	private ReimbursementServiceAlpha() {}
	
	public static ReimbursementService getInstance() {
		return service;
	}
	
	@Override
	public boolean submitRequest(Reimbursement reimbursement) {
		return repository.insert(reimbursement);
	}

	@Override
	public boolean finalizeRequest(Reimbursement reimbursement) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Reimbursement getSingleRequest(Reimbursement reimbursement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Reimbursement> getUserPendingRequests(Employee employee) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Reimbursement> getUserFinalizedRequests(Employee employee) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Reimbursement> getAllPendingRequests() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Reimbursement> getAllResolvedRequests() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ReimbursementType> getReimbursementTypes() {
		// TODO Auto-generated method stub
		return null;
	}

}
