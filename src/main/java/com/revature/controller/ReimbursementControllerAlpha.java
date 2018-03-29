package com.revature.controller;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.revature.ajax.ClientMessage;
import com.revature.model.Employee;
import com.revature.model.Reimbursement;
import com.revature.model.ReimbursementStatus;
import com.revature.model.ReimbursementType;
import com.revature.service.ReimbursementServiceAlpha;

public class ReimbursementControllerAlpha implements ReimbursementController {
	
	private static ReimbursementController reimbursementController = new ReimbursementControllerAlpha();
	private static Logger logger = Logger.getLogger(ReimbursementControllerAlpha.class);
    private ReimbursementControllerAlpha() {}

    public static ReimbursementController getInstance(){
        return reimbursementController;
    }

	@Override
	public Object submitRequest(HttpServletRequest request) {
		logger.trace("Submiting Request");
		Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");
		if (loggedEmployee == null) {
			return "login.html";
		} else if (loggedEmployee.getEmployeeRole().getId() == 2) {
			return "manager-home.html";
		} else if (request.getMethod() == "GET") {
			return "reimbursement-request.html";
		} 
			logger.trace(loggedEmployee.toString());
			Reimbursement reimbursement = new Reimbursement(
					0,
					LocalDateTime.now(),
					null,
					Double.parseDouble(request.getParameter("amount")),
					request.getParameter("description"),
					loggedEmployee,
					null,
					new ReimbursementStatus(1, "PENDING"),
					new ReimbursementType(Integer.parseInt(request.getParameter("reimbursementTypeId")), request.getParameter("reimbursementTypeName"))
					);
			if (ReimbursementServiceAlpha.getInstance().submitRequest(reimbursement)) {
				return new ClientMessage("REIMBURSEMENT SUBMISSION SUCCESSFUL");
			} else {
				return new ClientMessage("REIMBURSEMENT SUBMISSION WENT WRONG");
			}
		}
	
	@Override
	public Object singleRequest(HttpServletRequest request) {
		return null;
	}

	@Override
	public Object multipleRequests(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object finalizeRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getRequestTypes(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
