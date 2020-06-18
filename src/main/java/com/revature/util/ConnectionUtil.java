
//UTIL IS IN CHARGE OF THE CONNECTION

package com.revature.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil { //where connection happens
	
	private static Connection conn = null;
	
	//Privatye constructor PREVENTS us from ever instantiating this class
	private ConnectionUtil() {
		super();
	}
	
	public static Connection getConnection() { //perform connection to database
		
		/*
		 * Wer will be using DriverManager to obtain ourt connection to the DB
		 * we will need to provide it some credential information:
		 * Connection sStreing: jdbvc:oracle:thin:@ENDPOINT:PORT:SID
		 * 
		 * 		jdbc:oracle:thin:@ENDPOINT:5121:ORCL
		 * username
		 * password
		 */
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			//use driver manage to perform action
			try {
				conn = DriverManager.getConnection( //connection string 
						"jdbc:oracle:thin:@training.cstrlatj5obx.us-east-2.rds.amazonaws.com:1521:ORCL",
						"beaver",
						"chew"); //HARD CODED PASSWROD
				// WHICH means if you push this to github everyone can see your password very unsafe
				//Recommendation: Use environment variables instead
						
				
			} catch(SQLException e) {
				e.printStackTrace();
			}
			
		} catch(ClassNotFoundException e) {
			System.out.println(":Did not find Oracle JDBC Driver Class!");
		}
		
		return conn;
	}

}
