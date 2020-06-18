//SERVLETS ARE AN API IN JAVA TO PROCESS HTTP REQUESTS
	//API is a collection of classes
		//servlet --> generic servlet --> http servelt --> custom servlet
			//OUR SERVLETS WILL EXTEND HTTP SERVLET


package com.revature.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.User;
import com.revature.services.UserService;
import com.revature.templates.LoginTemplate;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = -4415167968525232599L;
		private static final ObjectMapper om = new ObjectMapper(); //extracts json and turns it into an object
		//final makes it immutable om will always be om the same with service
		private static final UserService service = new UserService();
		//service will be immutable the object itself can be modified but the reference cannot be changed, the object referenced will never be changed
		
		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse res) 
				throws ServletException, IOException {	
		}
		
		@Override
		protected void doPost(HttpServletRequest req, HttpServletResponse res) 
				throws ServletException, IOException {	
			
			HttpSession session = req.getSession(); //if login is scucesful create a session
			
			User current = (User) session.getAttribute("currentUser");
			
			//Already loged in
			if(current != null) {
				
				res.setStatus(400);
				res.getWriter().println("You are already logged in as user " + current.getUsername()); //print will add a new line vie the ln after print
				return; //to quit the method early
			}
			
			BufferedReader reader = req.getReader(); //represents the extractor of the body.			
//			
			StringBuilder sb = new StringBuilder();
//			
			String line; 
			while( (line = reader.readLine()) != null ) {
				sb.append(line);
			}
//			/*/
//			 * the (line = reader.readline() part obtains the value for a single line form the body of te request and stores it into the line variable
//			 * than the not = to null part compares the contents of the string (value) to null. 
//			 * 
//			 * if readline() method is null that means we are at the end
//			 */
//			
			String body = sb.toString();
//			
		
			LoginTemplate lt = om.readValue(body, LoginTemplate.class); //we serilailize the body of the request to obtain the information that was sent to us
						
			User u = service.login(lt);
			PrintWriter writer = res.getWriter(); //send response if login failed
			
			if(u == null) {
				//login failed
				res.setStatus(400);
								
				writer.println("Invalid Credentials");
				return;
			}
			
			
			//login succesful
			
			
			
			session.setAttribute("currentUser",  u); //puts entire user into the session
			//the session will generate a session and will put the u in the response and send it back to the client
			
			res.setStatus(200);
			
			writer.println(om.writeValueAsString(u)); //moved write belopw User u since i need the wreiter to be outside the loop
			
			res.setContentType("application/json");
		}
}
