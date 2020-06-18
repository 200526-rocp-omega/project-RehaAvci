package com.revature.services;

//SERVICE LAYER PROVIDES THE LOGIC FOR THE ACTUAL APPLICATION
	//BANKING 
		//ALL SPECIFIC BANKING INFO GOES IN THIS LAYER
			//LOGIN, APPLY FOR ACCOUNT, REGISTER, OIPEN ACCOUNT, WITHDRAW,. DEPOSIT, TRANSFER, ACRUE INTEREST, UPGRADE ACCOUNT, JOINT USERS
			//NEED EMPLOYEES TO BE ABLE TO LOOK AT OTHER EMPLOYEES INFORMATION
import java.util.List;
import com.revature.dao.AccountDAO;
import com.revature.exceptions.NoMoneyException;
import com.revature.models.Account;
import com.revature.models.User;

//service layer is a layer that is designed to enforce your business logic
//there are miscellaneous rules that define how your application will function

//			Ex: May not withdraw money over the current balance

//this design is simply furthering same design structure that we have used up to now

//how you go about designing the details of this layer is up to you
//due to the nature of the "business logic" being rather arbitrary
//this layer has the MOST creativity involved....most other layers are pretty boiler plate
//where you copy and paste most methods
public class AccountService {
		private AccountDAO accountDAO = new AccountDAO();
	//A starting place that i like to use, is to also create CRUD methods in the service layer
		//that will be used to interact with the DAO

		// Then additionally, you can have extra methods to enforce whaterver features/rules that you want
		//for example, we might also have a login/logout method
	
	
		public int insert(Account a) {	
			return accountDAO.insert(a);
		}
		
		public List<Account> findAll() {
			return accountDAO.findAll();
		}
		
		public List<Account> findByStatus(int statusId){ //MAKES STATUS ID INT 
			return accountDAO.findByStatus(statusId);
		}
		
		public Account findById(int accountId) {
			return accountDAO.findById(accountId);
		}

		public List<Account> findByUser(int id) {
			return accountDAO.findByUser(id);

		}

		public int withdraw(Account clientbankwithdrawAccount, double amount) {
			if ( amount <= 0) {
				//no money
				throw new NoMoneyException();
			} else {
				clientbankwithdrawAccount.setBalance(clientbankwithdrawAccount.getBalance()-amount);
			}
			return accountDAO.update(clientbankwithdrawAccount);
		}

		public int deposit(Account clientbankdepositAccount, double amount) {
			//WILL BE COPY PASTE OF WITHDRAW BUT OPPOSITE SIDE OF TRANSACTION
			if ( amount <= 0) {
				//no money
				throw new NoMoneyException();
			} else {
				clientbankdepositAccount.setBalance(clientbankdepositAccount.getBalance()+amount);
			}
			return accountDAO.update(clientbankdepositAccount);
		}

		public int transfer(Account transactionSender, Account transactionReceiver, double amount) {
				if (amount <= 0) {
					throw new IllegalArgumentException();
				}
				else {
					transactionSender.setBalance(transactionSender.getBalance() - amount);
					transactionReceiver.setBalance(transactionReceiver.getBalance() + amount);
					
					if(accountDAO.update(transactionSender) == 0) {
						if(accountDAO.update(transactionReceiver) == 0) {
							return 0;
						}
						else {
							transactionSender.setBalance(transactionSender.getBalance() + amount);
							return 1;
						}
					}
					else {
						return 1;
					}
				}
			}

		public int linkAccountToUser(Account a, User u) {
			return accountDAO.linkAccountToUser(a, u);
		}

		public int highestAccountId() {
			return accountDAO.highestAccountId();
		}

		public int update(Account a) {
				return accountDAO.update(a);
		}

}
		