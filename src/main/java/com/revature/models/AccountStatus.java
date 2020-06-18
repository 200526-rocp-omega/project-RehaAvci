package com.revature.models;

public enum AccountStatus { //sets possible account types as Pending, Open, Closed, Denied
	Pending, Open, Closed, Denied
	
	//So we can just list all four of them now, because we know that it won't change later on.
	//If we didn't have enums, we would have to do something like:
	/*/
	"public static final int Pending = 0;\r\n" + 
	"public static final int Open  = 1;\r\n" + 
	"public static final int Closed = 2;\r\n" + 
	"public static final int Denied  = 3;"/*/
}