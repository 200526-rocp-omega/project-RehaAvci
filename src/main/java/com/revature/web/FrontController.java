package com.revature.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.authorization.AuthService;
import com.revature.controllers.AccountController;
import com.revature.controllers.UserController;
import com.revature.exceptions.AuthorizationException;
import com.revature.exceptions.NotLoggedInException;
import com.revature.models.Account;
import com.revature.models.User;
import com.revature.templates.BankClientTransferTemplate;
import com.revature.templates.MessageTemplate;
import com.revature.templates.DurationTemplate;

public class FrontController extends HttpServlet {
	private static final long serialVersionUID = -4854248294011883310L;
	private static final UserController userController = new UserController();
	private static final AccountController accountController = new AccountController();
	private static final ObjectMapper om = new ObjectMapper(); // extracts json and turns it into an object

	@Override //quick check to see if you are actually overiding in the compilation phase
	protected void doGet(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException { //tomcat knows the name, override will make sure that the record signature 
		//a method such as doGets would not since tomcat is not familiar
		res.setContentType("application/json");
		final String URI = req.getRequestURI().replace("/JDBCDemo", "").replaceFirst("/","");
		String[] portions = URI.split("/");
	//	System.out.println(Arrays.toString(portions));	
		try {
			switch(portions[0]) {
			case "users":
				if(portions.length == 2) {
					// Delegate to a Controller method to handle obtaining a User by ID
					int id = Integer.parseInt(portions[1]);
					AuthService.guard(req.getSession(false), id, "Employee", "Admin");
					User u = userController.findUserById(id);
					res.setStatus(200);
					res.getWriter().println(om.writeValueAsString(u));
				} else {
					// Delegate to a Controller method to handle obtaining ALL Users
					AuthService.guard(req.getSession(false), "Employee", "Admin");
					List<User> all = userController.findAllUsers();
					res.setStatus(200);
					res.getWriter().println(om.writeValueAsString(all));
				}
				
				break;
			case "accounts":
				//case "account"
					//		case "accounts": //URL: /accounts
						//		if(portions.length == 2) {
						//			int statusId = Integer.parseInt(portions[2]);
				if(portions.length == 3) {
					List<Account> everyAccount;
					switch(portions[1]) {
					
					case "owner":
						int id = Integer.parseInt(portions[2]);
						AuthService.guard(req.getSession(false), id, "Employee","Admin");
						everyAccount = accountController.findAccountsByOwner(id);
						res.setStatus(200);
						res.getWriter().println(om.writeValueAsString(everyAccount));
						break;
					
					case "status": //IF THE WORD AFTER ACCOUNTS IS STATUS
						AuthService.guard(req.getSession(false), "Employee", "Admin");
						everyAccount = accountController.findByStatus(Integer.parseInt(portions[2]));
						//^ grabs the integer from portion 2 similar to the user id earlier in this code
						res.setStatus(200);
						res.getWriter().println(om.writeValueAsString(everyAccount));
						break;
					}
					
				} else if (portions.length == 2) {
					int accountid = Integer.parseInt(portions[1]);
					System.out.println(accountid);
					AuthService.guardAccount(req.getSession(false), accountid, "Employee","Admin");
					Account a = accountController.findById(accountid);
					res.setStatus(200);
					res.getWriter().println(om.writeValueAsString(a));
					
				} else if (portions.length == 1) {
					AuthService.guard(req.getSession(false), "Employee","Admin");
					List<Account> everyAccount = accountController.findAll();
					res.setStatus(200);
					res.getWriter().println(om.writeValueAsString(everyAccount));
					
				}
				
				res.setContentType("application/json");
				break;
			}
			
		} catch(AuthorizationException e) {
			res.setStatus(401);
			MessageTemplate message = new MessageTemplate("The incoming token has expired");
			
			res.getWriter().println(om.writeValueAsString(message));
		} catch(NumberFormatException e) {
			res.setStatus(400);
			MessageTemplate message = new MessageTemplate("The id provided was not an integer");
			res.getWriter().println(om.writeValueAsString(message));
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException { //throws keyword is only used to handle checked exceptions
			
			res.setContentType("application/json");
			final String URI = req.getRequestURI().replace("/JDBCDemo", "").replaceFirst("/","");
			
			String[] portions = URI.split("/");
			
			try {
				switch(portions[0]) {
				case "logout": //use usercontrol logout method to logout
					if(userController.logout(req.getSession(false))) { //get session is anm overloaded method allowing it to take a boolean 
					//which will specify if there was a new session, does not create a new session if the boolean is false
					
					//if they are trying to log out and they dont ahve a session dw about it theres no log in to begin with
						res.setStatus(200);
						res.getWriter().println("You have been successfully logged out");
					} else {
						res.setStatus(400);
						res.getWriter().println("You were not logged in to begin with");
					}
					break;
				
				case "accounts":
					req.getSession();
					if (portions.length == 2) { //start another switch round inside this if statement
						BufferedReader bufferedReader = req.getReader();
						StringBuilder stringBuilder = new StringBuilder();
						String Duration;
						
						while((Duration = bufferedReader.readLine()) != null) {
							stringBuilder.append(Duration);
							}
						String duration = stringBuilder.toString();
						//start of switch block
						switch(portions[1]) { //takes 2nd part of URI
						case "withdraw":
							BankClientTransferTemplate Withdraw = om.readValue(duration, BankClientTransferTemplate.class);
							//need to check requirements
							AuthService.checkStatus(req.getSession(false), Withdraw.getAccountId());
							AuthService.guardAccount(req.getSession(false), Withdraw.getAccountId(), "Admin");
							//passed checking requirements
							Account clientbankwithdrawAccount = accountController.findById(Withdraw.getAccountId());
							if (clientbankwithdrawAccount.getBalance() < Withdraw.getAmount()) {
								res.setStatus(400);
								MessageTemplate message = new MessageTemplate("your attempting to stealing from the bank, fbi is on their way");
								res.getWriter().println(om.writeValueAsString(message));
							} 
							else {
								try {
									accountController.withdraw(clientbankwithdrawAccount, Withdraw.getAmount());
									res.setStatus(200);
									Withdraw.getAmount();
									MessageTemplate message = new MessageTemplate("\"message\": \"${amount} has been withdrawn from Account #{accountId}\"");
									res.getWriter().println(om.writeValueAsString(message));
								} catch (IllegalArgumentException e) {
									res.setStatus(400);
									MessageTemplate message = new MessageTemplate("your attempting to stealing from the bank, fbi is on their way");
									res.getWriter().println(om.writeValueAsString(message));								}
							}
							break;	
						case "deposits":
							BankClientTransferTemplate Deposit = om.readValue(duration, BankClientTransferTemplate.class);
							//need to check requirements
							AuthService.checkStatus(req.getSession(false), Deposit.getAccountId());
							AuthService.guardAccount(req.getSession(false), Deposit.getAccountId(), "Admin");
							//passed checking requirements
							Account clientbankdepositAccount = accountController.findById(Deposit.getAccountId());
							if (clientbankdepositAccount == null) {
								res.setStatus(400);
								res.getWriter().println(om.writeValueAsString(new MessageTemplate("No Account")));
								}
							else {
									try {
										accountController.deposit(clientbankdepositAccount,Deposit.getAmount());
										res.setStatus(200);
										String depositMessage = "$" + Deposit.getAmount() + " has been deposited into Account #" + clientbankdepositAccount.getAccountId();
										res.getWriter().println(om.writeValueAsString(new MessageTemplate(depositMessage)));
									} catch (IllegalArgumentException e) {
										res.setStatus(400);
										res.getWriter().println(om.writeValueAsString(new MessageTemplate("Cannot deposit $0 or less.")));
									}
							
							}
							break;
						
						case "passtime":
							AuthService.guard(req.getSession(false),"Admin");
							BufferedReader timeReader = req.getReader();
						
							StringBuilder timeBuilder = new StringBuilder();
						
							String curLine;
						
							while ((curLine = timeReader.readLine()) != null) {
								timeBuilder.append(curLine);
							}
						
							String timeBody = timeBuilder.toString();
							DurationTemplate time = om.readValue(timeBody, DurationTemplate.class);
							int months = time.getNumOfMonths();
							List<Account> all = accountController.findAll();
							for(int count = 0; count < months; count++) {
								for(Account acc : all) {
									if(acc.getType().getTypeId() == 2) {
										System.out.println("Original balance: " + acc.getBalance());
										acc.setBalance(acc.getBalance() + (acc.getBalance() * .06 / 12.0)); //.06 is my interest rate
										System.out.println("Final balance: " + acc.getBalance());
										accountController.update(acc);
								}
							}
						}
						default:
							MessageTemplate message = new MessageTemplate("Not logged in, Please Log In!");
							res.getWriter().println(om.writeValueAsString(message));
						}
					} else {
						MessageTemplate message = new MessageTemplate("Not logged in, Please Log In!");
						res.getWriter().println(om.writeValueAsString(message));
					}
				
					break;
					}
					}catch (NotLoggedInException e) { //If user isn't logged in
					res.setStatus(401);
					MessageTemplate message = new MessageTemplate("You are not logged in. Go to /users?login and POST your credentials");
					res.getWriter().println(om.writeValueAsString(message));
					
				} catch (AuthorizationException e) { // If they put in bad credentials
					res.setStatus(400);
					MessageTemplate message = new MessageTemplate("Invalid login credentials");
					res.getWriter().println(om.writeValueAsString(message));
				}
			}
	}
	

