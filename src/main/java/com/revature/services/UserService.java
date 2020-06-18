
//SERVICE LAYER PROVIDES THE LOGIC FOR THE ACTUAL APPLICATION
	//BANKING 
		//ALL SPECIFIC BANKING INFO GOES IN THIS LAYER
			//LOGIN, APPLY FOR ACCOUNT, REGISTER, OIPEN ACCOUNT, WITHDRAW,. DEPOSIT, TRANSFER, ACRUE INTEREST, UPGRADE ACCOUNT, JOINT USERS
			//NEED EMPLOYEES TO BE ABLE TO LOOK AT OTHER EMPLOYEES INFORMATION


package com.revature.services;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.revature.dao.IUserDAO;
import com.revature.dao.UserDAO;
import com.revature.exceptions.NotLoggedInException;
import com.revature.models.User;
import com.revature.templates.LoginTemplate;

//service layer is a layer that is designed to enforce your business logic
//there are miscellaneous rules that define how your application will function

// 			Ex: May not withdraw money over the current balance

//this design is simply furthering same design structure that we have used up to now

//how you go about designing the details of this layer is up to you
//due to the nature of the "business logic" being rather arbitrary
//this layer has the MOST creativity involved....most other layers are pretty boiler plate
//where you copy and paste most methods
public class UserService {

		
		private IUserDAO dao = new UserDAO();
	//A starting place that i like to use, is to also create CRUD methods in the service layer
		//that will be used to interact with the DAO

		// Then additionally, you can have extra methods to enforce whaterver features/rules that you want
		//for example, we might also have a login/logout method
	
	
		public int insert(User u) {	
			return dao.insert(u);
		}
		
		public List<User> findAll() {
			return dao.findAll();
		}
		
		public User findByID(int id) {
			return dao.findById(id);
		}
		
		public User findByUsername(String username) { //usernames are unique so it will return just 1 user
			return dao.findByUsername(username);
		}
		
		public int update(User u) {
			return dao.update(u);
		}
	
		public User login(LoginTemplate lt) {
			
			User userFromDB = findByUsername(lt.getUsername()); //uses find by username method to find user object 
			
			//incorrect Username
			if(userFromDB == null) { //no username that matches
				return null;
			}
			
			//username correct password isnt
			if(userFromDB.getPassword().equals(lt.getPassword())) { //password matches password of user in database
				return userFromDB;
			}
			
			//username was correct, but the password was not
			return null; //if username is correct but password isnt returns null
		}
		
		public void logout(HttpSession session) {
			if(session == null || session.getAttribute("currentUser") == null) {
				throw new NotLoggedInException("User must be logged in, in order to logout.");
			}
			
			session.invalidate();
		}
}
