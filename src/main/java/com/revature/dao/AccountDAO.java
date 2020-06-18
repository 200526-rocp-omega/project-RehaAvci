package com.revature.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.Account;
//DATA ACCES OBJECT IN REGARDS TO DATA ACCES WANTED TO LEVERAGE ABSTRACTION
	//IN GENERAL IS A CLASS
		//CREATES INTERFACE TO USE ABSTRACTION
			//INTERFACE USED TO HIDE ALL LOGIC FROM THE CLASSES WHICH CONTAIN THEM
import com.revature.models.CheckingAccount;
import com.revature.models.SavingsAccount;
import com.revature.models.User;
import com.revature.util.ConnectionUtil;

public class AccountDAO implements IAccountDAO{


	@Override
	public int submit(Account a) {
			int A = 0;
			try(Connection conn = ConnectionUtil.getConnection()) {
				String sql = "INSERT INTO ACCOUNTS (balance,status_id,type_id) VALUES (?, ?, ?)";
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setDouble(1,a.getBalance());
				stmt.setInt(2, a.getStatusId());
				stmt.setInt(3, a.getType().getTypeId());
				stmt.setInt(4, a.getAccountId());
				A = stmt.executeUpdate();
				
				ResultSet resultSet = stmt.executeQuery();
				
				while(resultSet.next()) {
					int id = resultSet.getInt(1); // Grabs the value from the first column
					a.setAccountId(id);  // Now that we know the Value, we can access our new ID and assign it to our account.
				}

		} catch (SQLException e) {
			e.printStackTrace();
			return A;
		}
		return A;
	}
	
	@Override
	public int update(Account a) {
		try(Connection conn = ConnectionUtil.getConnection()) {
			String sql = "UPDATE ACCOUNTS SET balance=?,status_id=?,type_id=? WHERE id = ?";
			//String sql = "INSERT INTO USERS (username, password, first_name, last_name, email, role_id) VALUES (?,?,?,?,?,?)";

			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setDouble(1,a.getBalance());
			stmt.setInt(2, a.getStatusId());
			stmt.setInt(3, a.getType().getTypeId());
			stmt.setInt(4, a.getAccountId());
			
			if(stmt.executeUpdate() > 0) {
				return 0;
			}
			else {
				return 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	@Override
	public int highestAccountId() {
		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "SELECT MAX(id) AS id FROM ACCOUNTS";
			Statement stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery(sql);
			
			if(rs.next()) {
				return rs.getInt("id");
			}
			return -1;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	@Override
	public int unlinkAccountFromUser(Account a, User u) {
		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "DELETE FROM USER_ACCOUNTS WHERE user_id = ? AND account_id = ?)";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, u.getId());
			stmt.setInt(2, a.getAccountId());
			stmt.executeUpdate();
			
			return 0;
		} catch(SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	@Override
	public int linkAccountToUser(Account a, User u) {
		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "INSERT INTO USERS_ACCOUNTS (user_id,account_id) VALUES (?,?)";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, u.getId());
			stmt.setInt(2, a.getAccountId());
			stmt.executeUpdate();
			
			return 0;
		} catch(SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}



	@Override
	public int insert(Account a) { //re
		try (Connection conn = ConnectionUtil.getConnection()) { //obtains conneciton from utility class
			String sql = "INSERT INTO ACCOUNTS (balance, status_id, type_id) VALUES (?,?,?)";
			// the ? are placeholders for input values, they work for PreparedStatements, and are designed to protect from SQL Injection
			PreparedStatement stmt = conn.prepareStatement(sql); //using prepared statement to prevent sql injection
			//inserting below values into the ?'s above
			double balance = a.getBalance(); //initial object creation of balance
			//AccountType accounttype = a.getType(); //initial account type 
			//AccountStatus accountstatus = a.getStatus();
		//	stmt.setDouble(1, double balance = a.getBalance(); //initial object creation of balance);
			//			stmt.setInt(6, u.getRole().getId());
			stmt.setDouble(1, balance);
			stmt.setInt(2, a.getStatusId());
			//stmt.setInt(3, a.getTypeId());
			stmt.setInt(3, a.getType().getTypeId());
			stmt.executeUpdate();
			return 0;
		} catch(SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}

	
	@Override
	public List<Account> findAll() { // Return all users
		List<Account> everyAccount = new ArrayList<>();
		try (Connection conn = ConnectionUtil.getConnection()) {// This is a 'try with resources' block. 
			//Allows us to instantiate some variable, and at the end of try it will auto-close 
			//to prevent memory leaks, even if exception is thrown.
			String sql = "SELECT * FROM ACCOUNTS ORDER BY id";
			Statement stmnt = conn.createStatement();
			ResultSet resultSet = stmnt.executeQuery(sql); // Right as this is executed, the query runs to the database and grabs the info
			/*while(rs.next()) {
				int id = rs.getInt("id"); //grabs first column from the sql statement above
				String username = rs.getString("username");
				String password = rs.getString("password");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String email = rs.getString("email");
				int roleId = rs.getInt("role_id");
				String rName = rs.getString("role");
				
				Role role = new Role(roleId, rName);
				User u = new User(id, username, password, firstName, lastName, email, role); //user object
				
			 * 
			 */
			while(resultSet.next()) { // For each entry in the result set
				Account A = null;
				int id = resultSet.getInt("id"); // Grab the account id
				double balance = resultSet.getDouble("balance");
				int statID = resultSet.getInt("status_id");
				int typeID = resultSet.getInt("type_id");
				
				if(typeID ==1) { //makes the empty account container type checkings
					A = new CheckingAccount(id,balance,statID);
				}
				if(typeID ==2) { //makes the empty account container type savings
					A = new SavingsAccount(id,balance,statID);
				}
				
				everyAccount.add(A); // add User object to the list
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
			return new ArrayList<>(); // If something goes wrong, return an empty list.
		}
		return everyAccount;
	}
	
	
	@Override
	public Account findById(int id) { 
		Account A = null;	
		try (Connection conn = ConnectionUtil.getConnection()) {
			
			//Account A = null;
			
			String sql = "SELECT * FROM ACCOUNTS "
							+ "INNER JOIN ACCOUNT_STATUS ON ACCOUNTS.status_id = ACCOUNT_STATUS.id "
							+ "INNER JOIN ACCOUNT_TYPE ON ACCOUNTS.type_id = ACCOUNT_TYPE.id "
							+ "WHERE ACCOUNTS.ID = ?";
			PreparedStatement stmnt = conn.prepareStatement(sql);
			stmnt.setInt(1, id); 
			
			ResultSet resultSet = stmnt.executeQuery(); // grabs result set of the query
			/*/while there is still a non empty line
			//while(resultSet.next()) { 
				double balance = resultSet.getDouble("balance");
				int statusId = resultSet.getInt("status_id");
				String statusName = resultSet.getString("status");
				int typeId = resultSet.getInt("type_id");
				String typeName = resultSet.getString("type");
				int accountId = resultSet.getInt("id");
				//and use that data to create a Account object accordingly
				AccountStatus accountStatus = new AccountStatus(statusId,statusName);
				AccountType accountType = new AccountType(typeId,typeName);
			/*/	

			while(resultSet.next()) {
			//	Account A = null;
				int Id = resultSet.getInt("id");
				double balance = resultSet.getDouble("balance");
				int statusId = resultSet.getInt("status_id");
				int typeId = resultSet.getInt("type_id");
								
				if(typeId ==1) { //makes the empty account container type checkings
					A = new CheckingAccount(Id,balance,statusId);
				}
				if(typeId ==2) { //makes the empty account container type savings
					A = new SavingsAccount(Id,balance,statusId);
				}
			}
				
			
		} catch (SQLException e) {
			e.printStackTrace();
			return A;
		}
			return A;
	}
	
	@Override
	public List<Account> findByStatus(int statusId) { // Locate files of an appropriate status (pending, open, closed, denied)
		
		//Account B = null;
		List<Account> everyAccount = new ArrayList<>();

		try (Connection conn = ConnectionUtil.getConnection()) {// This is a 'try with resources' block. 
			//Allows us to instantiate some variable, and at the end of try it will auto-close 
			//to prevent memory leaks, even if exception is thrown.
			String sql = "SELECT * FROM ACCOUNTS WHERE status_id = ? ORDER BY id";
			PreparedStatement stmnt = conn.prepareStatement(sql);
			stmnt.setInt(1, statusId);
			ResultSet resultSet = stmnt.executeQuery(); // Right as this is executed, the query runs to the database and grabs the info

			while(resultSet.next()) { // For each entry in the result set
				Account B = null;
				int id = resultSet.getInt("ID"); // Grab the account id
				double balance = resultSet.getDouble("balance");
				int statusID = resultSet.getInt("status_id");
				int typeID = resultSet.getInt("type_id");

				if(typeID ==1) { //makes the empty account container type checkings
					B = new CheckingAccount(id,balance,statusID);
				}
				if(typeID ==2) { //makes the empty account container type savings
					B = new SavingsAccount(id,balance,statusID);
				}

				everyAccount.add(B); // add User object to the list
			}

		} catch(SQLException e) {
			e.printStackTrace();
			//return new ArrayList<Account>(); // If something goes wrong, return an empty list.
			return new ArrayList<>(); // If something goes wrong, return an empty list.

		}
		return everyAccount;
	}

	@Override
	public List<Account> findByUser(int id) {
	//	Account C = null;
		List<Account> everyAccount = new ArrayList<>();
		
		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM ACCOUNTS"
					+ " INNER JOIN USERS_ACCOUNTS ON ACCOUNTS.id = USERS_ACCOUNTS.account_id" 
					+ " WHERE USERS_ACCOUNTS.user_id = ?"
					+ " ORDER BY ACCOUNTS.id";
			PreparedStatement stmnt = conn.prepareStatement(sql);
			stmnt.setInt(1, id);
			ResultSet resultSet = stmnt.executeQuery(); // Right as this is executed, the query runs to the database and grabs the info
			
			while(resultSet.next()) {
				Account C = null;
				int accId = resultSet.getInt("id");
				double balance = resultSet.getDouble("balance");
				int statusId = resultSet.getInt("status_id");
				int typeId = resultSet.getInt("type_id");
								
				if(typeId == 1) {
					C = new CheckingAccount(accId,balance,statusId);
				}
				else if (typeId == 2) {
					C = new SavingsAccount(accId,balance,statusId);
				}
				everyAccount.add(C);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
		return everyAccount;
	}
	
	
}
