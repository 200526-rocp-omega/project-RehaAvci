package com.revature.authorization;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.revature.controllers.AccountController;
import com.revature.exceptions.AuthorizationException;
import com.revature.exceptions.NotLoggedInException;
import com.revature.exceptions.RoleNotAllowedException;
import com.revature.models.Account;
import com.revature.models.AccountStatus;
import com.revature.models.User;

public class AuthService {
	private static final AccountController accountController = new AccountController();

	public static void guard(HttpSession session, String...roles) { //str...roles are the combination of emploee and admin
		User currentUser = session ==null ? null : (User) session.getAttribute("currentUser");
		//checks if session is null if it isnt null will use the session to get the attribute
		if(session == null || currentUser == null ) { // if not session or the current user isnt loged in throw below
			throw new NotLoggedInException();
		}
		
		boolean found = false;
		//at this point the current use must be logged in
		String role = currentUser.getRole().getRole();
		for(String allowedRole : roles) {
			if(allowedRole.equals(role)) {
				found = true;
			}
		}
		
		if(!found) {
			throw new RoleNotAllowedException();
		}
	}
	
	
		
	public static void guard(HttpSession session, int id, String...roles) {
			try {
				guard(session, roles);
			} catch(RoleNotAllowedException e) {
				User current = (User) session.getAttribute("currentUser");
				if(id == current.getId()) {
					throw e;
			   }
		   }
	  }

	public static void guardAccount(HttpSession session,int accountId,String...roles) {
		try {
			guard(session,roles);
		} catch(RoleNotAllowedException e) {
			User current = (User) session.getAttribute("currentUser");
			List<Account> accounts = accountController.findByUser(current.getId());
			boolean authorized = false;
			for(Account acc : accounts) {
				if(accountId == acc.getAccountId()) {
					authorized = true;
				}
			}
			if(!authorized) {
				throw new RoleNotAllowedException();
			}
		}
	}

	public static void checkStatus(HttpSession session, int accountId) {
		Account a = accountController.findById(accountId);
			if(a.getStatus() != AccountStatus.Open) {
				throw new AuthorizationException();
			}
	}
}

