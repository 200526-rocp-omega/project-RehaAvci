package com.revature.dao;

import java.util.List;

import com.revature.models.User;

public interface IUserDAO { //crud operations that interfere with user in database
//purpose of an interface\
		//  allows you to specify that multiple classes implement some common functionality.

	public int insert(User u); //Create Operation
	
	public List<User> findAll(); //Read Method
	public User findById(int id);
	public User findByUsername(String username);
	
	public int update(User u); //Update method
	
}
