package com.revature.controllers;
import java.util.List;

import com.revature.models.Account;
import com.revature.models.User;
import com.revature.services.AccountService;

public class AccountController {
	private final AccountService AccountService = new AccountService();
	
	public List<Account> findAll(){
		return AccountService.findAll();
	}
	
	public List<Account> findByStatus(int statusId){
		return AccountService.findByStatus(statusId);
	}

	public Account findById(int accountid) {
		return AccountService.findById(accountid);
	}

	public List<Account> findByUser(int id) {
		return AccountService.findByUser(id);
	}

	public List<Account> findAccountsByOwner(int id) {
		return AccountService.findByUser(id);
	}
	
	public int update(Account a) {
		return AccountService.update(a);
	}
	public int insert(Account a, User u) {
		int addError =  AccountService.insert(a);
		if (addError == 0) {
			int addedID = AccountService.highestAccountId();
			a.setAccountId(addedID);
			int linkError = AccountService.linkAccountToUser(a, u);
			if(linkError == 0) {
				return addedID;
			}
			else {
				return -1;
			}
		}
		return -1;
	}

	public int withdraw(Account clientbankwithdrawAccount, double amount) {
		return AccountService.withdraw(clientbankwithdrawAccount, amount);
	}

	public int deposit(Account clientbankdepositAccount, double amount) {
		return AccountService.deposit(clientbankdepositAccount, amount);
	}

	public int transfer(Account transactionSender, Account transactionReceiver, double amount) {
		return AccountService.transfer(transactionSender, transactionReceiver, amount);
	}
		
	}
	


