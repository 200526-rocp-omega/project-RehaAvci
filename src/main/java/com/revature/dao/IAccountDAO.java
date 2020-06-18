package com.revature.dao;
import java.util.List;
import com.revature.models.Account;
import com.revature.models.User;

public interface IAccountDAO {
 //crud operations that interfere with user in database
//purpose of an interface\
		//  allows you to specify that multiple classes implement some common functionality.

	public int insert(Account a); //Create Operation
	public List<Account> findAll(); //Read Method
	public List<Account> findByStatus(int statusId);
	public Account findById(int accountId);
	List<Account> findByUser(int id);
	int update(Account a);
	int unlinkAccountFromUser(Account a, User u);
	int linkAccountToUser(Account a, User u);
	int submit(Account a);
	int highestAccountId();
}
