package database;

import java.sql.*;

public class DbConnection {
	
	private String errorMessage = null;
	private Connection conn = null;
	
	public String getErrorMessage() { return errorMessage; }
	
	public Connection getConnection() { return conn; }
	
	public boolean createConnection ( String databaseURL, String username, String password ) {
		
	  try {
	  
		  Class.forName("org.postgresql.Driver");
	  
	  } catch (ClassNotFoundException cnfe) { 
		  
		  errorMessage = "Couldn't Find DB Driver Class"; 
		  return false;
		  
	  }
	  
	  try {
	    
		  conn = DriverManager.getConnection( databaseURL, username, password);
	  
	  } catch (SQLException e) { 
		  
		  errorMessage = "Database Exception: " + e; 
		  return false;
		  
	  }
		
	  return true;
	  
	}
	
}