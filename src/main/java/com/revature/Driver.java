package com.revature;

import com.revature.dao.IUserDAO;
import com.revature.dao.UserDAO;
import com.revature.models.Role;
import com.revature.models.User;

public class Driver {

	public static void main(String[] args) {
		IUserDAO dao = new UserDAO();
		
		//test
		User testUser = new User(0,"sguy1","pass2","Some","Guy","sguy12@email.com", new Role(1, "Standard"));
		
		System.out.println(dao.insert(testUser));//actually inserts the data
		
		for (User u : dao.findAll()) {
			System.out.println(u);
		}
		
	}

}
