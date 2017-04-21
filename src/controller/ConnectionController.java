package controller;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionController
{
	private static final String DB_NAME = "clubs", ADMIN = "postgres", PASS = "!General10";
	
	/**
	 * Connects to the database.
	 * @return The connection
	 */
	public static Connection connect()
	{
	    try
	    {
	    	Class.forName("org.postgresql.Driver");
	    	Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + DB_NAME, ADMIN, PASS);
	    	return c;
	    }
	    catch (Exception e)
	    {
	    	return null;
	    }
	}
}
