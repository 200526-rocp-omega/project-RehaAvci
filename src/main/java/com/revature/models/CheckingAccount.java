
package com.revature.models;

public class CheckingAccount extends Account {

	public CheckingAccount() {
		super();
		setType(new AccountType(1,"Checking"));
	}
	
	
	public CheckingAccount(int accountID, double balance, AccountStatus status) {
		super();
		setAccountId(accountID);
		setBalance(balance);
		setStatus(status);
		setType(new AccountType(1,"Checking"));
	}
	
	public CheckingAccount(int accountID, double balance, int status) {
		super();
		if(Account.statusMap.isEmpty()) {
			Account.statusMap.put(1,AccountStatus.Pending);
			Account.statusMap.put(2, AccountStatus.Open);
			Account.statusMap.put(3, AccountStatus.Closed);
			Account.statusMap.put(4, AccountStatus.Denied);
		}
		
		setAccountId(accountID);
		setBalance(balance);
		setStatus(Account.statusMap.get(status));
		setType(new AccountType(1,"Checking"));
	}
	
	@Override
	public String toString() {
		return "CheckingAccount [accountId=" + getAccountId() + ", balance=$" + getBalance() + ", status=" + getStatus() + ", type=" + getType();
	}
}