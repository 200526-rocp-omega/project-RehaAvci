package com.revature.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class Account {
	  private int accountId; // primary key
	  private double balance;  // not null
	  private AccountStatus status;
	  private AccountType type;
	  
	public Account(int accountId, double balance, AccountStatus status, AccountType type) {
		super();
		this.accountId = accountId;
		this.balance = balance;
		this.status = status;
		this.type = type;
	}

	public Account() {
		super();
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		if(balance < 0) {
			throw new IllegalArgumentException();
		}
		this.balance = balance;
	}

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	public AccountType getType() {
		return type;
	}

	public void setType(AccountType type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accountId, balance);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Account)) {
			return false;
		}
		Account other = (Account) obj;
		return accountId == other.accountId
				&& Double.doubleToLongBits(balance) == Double.doubleToLongBits(other.balance);
	}

	@Override
	public String toString() {
		return "Account [accountId=" + accountId + ", balance=" + balance + "]";
	}
	  
	public int getStatusId() {
		
		int statusInt = 0;
		
		for(int key : statusMap.keySet()) {
			if(statusMap.get(key) == this.status) {
				statusInt = key;
			}
		}
		return statusInt;
	}
	
	public static Map<Integer,AccountStatus> statusMap = new HashMap<>();	  
	  
	}