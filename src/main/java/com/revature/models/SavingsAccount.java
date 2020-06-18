package com.revature.models;

public class SavingsAccount extends Account {
	//Average Interest Rate for Savings Accounts. According to the FDIC, the national average interest rate on savings accounts currently stands at 0.06% APY.
	public static double annualInterest = .06;
	
	/*/public CheckingAccount() {
		super();
		setType(new AccountType(1,"Checking"));
	/*/

	public SavingsAccount() {
		super();
		setType(new AccountType(2,"Savings"));
	}
	
	public SavingsAccount(int accountID, double balance, AccountStatus status) {
		super();
		setAccountId(accountID);
		setBalance(balance);
		setStatus(status);
		setType(new AccountType(2,"Savings"));
	}
	
	public SavingsAccount(int accountID, double balance, int status) {
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
		setType(new AccountType(2,"Savings"));
	}

	@Override
	public String toString() {
		return "SavingsAccount [accountId=" + getAccountId() + ", balance=$" + getBalance() + ", status=" + getStatus() + ", type=" + getType();
	}
	
	
}