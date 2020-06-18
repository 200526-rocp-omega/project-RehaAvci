package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.Role;
import com.revature.models.User;
//DATA ACCES OBJECT IN REGARDS TO DATA ACCES WANTED TO LEVERAGE ABSTRACTION
	//IN GENERAL IS A CLASS
		//CREATES INTERFACE TO USE ABSTRACTION
			//INTERFACE USED TO HIDE ALL LOGIC FROM THE CLASSES WHICH CONTAIN THEM


import com.revature.util.ConnectionUtil;

public class UserDAO implements IUserDAO {

	@Override
	public int insert(User u) { //re

		try (Connection conn = ConnectionUtil.getConnection()) { //obtains conneciton from utility class
			
			String sql = "INSERT INTO USERS (username, password, first_name, last_name, email, role_id) VALUES (?,?,?,?,?,?)";
			// the ? are placeholders for input values, they work for PreparedStatements, and are designed to protect from SQL Injection
			
			PreparedStatement stmt = conn.prepareStatement(sql); //using prepared statement to prevent sql injection
			//inserting below values into the ?'s above
			
			stmt.setString(1, u.getUsername());
			stmt.setString(2, u.getPassword());
			stmt.setString(3, u.getFirstName());
			stmt.setString(4, u.getLastName());
			stmt.setString(5, u.getEmail());
			stmt.setInt(6, u.getRole().getId());

			return stmt.executeUpdate();
			
		} catch(SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public List<User> findAll() {//implement, this method finds all records from the user table
		List<User> allUsers = new ArrayList<>();
		//must have sql eception error try catch due to check exception
		
		
		//Below is a try-with-resource block
		//It allows us to instatniate some variable for use only insde the ttry block
		//And then at the end, it will autmoatically invoke the close() method on the resource
		//the close() method prevents memory leaks
		try (Connection conn = ConnectionUtil.getConnection()) { //obtains conneciton from utility class
			String sql = "SELECT * FROM USERS INNER JOIN ROLES ON USERS.role_id = ROLES.id";
			
			Statement stmt = conn.createStatement();
			
			//state^ returns query result, must have a resultset
			ResultSet rs = stmt.executeQuery(sql);
			
			//^sql statment will get run an give data back and the .execute allows us to do this
			while(rs.next()) {
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
				
				allUsers.add(u);
			}
		} catch(SQLException e) {
			// if something foes wrong. return an empy list
			e.printStackTrace();
			return new ArrayList<>();
		}
		
		
		return allUsers;
	}

	@Override
	public User findById(int id) {
		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM USERS INNER JOIN ROLES ON USERS.role_id = ROLES.id WHERE USERS.id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) { //identical to find all method
				String username = rs.getString("username");
				String password = rs.getString("password");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String email = rs.getString("email");
				int roleId = rs.getInt("role_id");
				String rName = rs.getString("role");
				
				//and use that data to create a User object accordingly
				Role role = new Role(roleId, rName);
				return new User(id, username, password, firstName, lastName, email, role); //user object
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override //login in method implemented
	public User findByUsername(String username) { //identical to find all method		
		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM USERS INNER JOIN ROLES ON USERS.role_id = ROLES.id WHERE username = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, username);
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) { //identical to find all method
				int id = rs.getInt("id"); //grabs first column from the sql statement above
				String password = rs.getString("password");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String email = rs.getString("email");
				int roleId = rs.getInt("role_id");
				String rName = rs.getString("role");
				
				//and use that data to create a User object accordingly
				Role role = new Role(roleId, rName);
				return new User(id, username, password, firstName, lastName, email, role); //user object
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public int update(User u) {
		try(Connection conn = ConnectionUtil.getConnection()) {
			String sql = "UPDATE USERS SET " + "USERNAME = ?, PASSWORD = ?, FIRST_NAME = ?, LAST_NAME = ?, EMAIL = ?, ROLE_ID = ? WHERE ID = ?"; 
			//String sql = "INSERT INTO USERS (username, password, first_name, last_name, email, role_id) VALUES (?,?,?,?,?,?)";

			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1,u.getUsername());
			stmt.setString(2,u.getPassword());
			stmt.setString(3,u.getFirstName());
			stmt.setString(4,u.getLastName());
			stmt.setString(5,u.getEmail());
			stmt.setInt(6,u.getRole().getRoleId());

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


}
