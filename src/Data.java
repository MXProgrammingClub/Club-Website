import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Handles storage and retrieval of information from the database.
 * @author Julia McClellan
 */
public class Data
{
	private static final String DB_NAME = "clubs", ADMIN = "postgres", PASS = "password";
	
	private static Connection c = null;
	private static Statement s = null;
	
	/**
	 * Connects to the database. Should be done on startup to enable all future operations.
	 * @return Whether connection was successful.
	 */
	public static boolean connect()
	{
	    try
	    {
	    	Class.forName("org.postgresql.Driver");
	    	c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + DB_NAME, ADMIN, PASS);
	    	s = c.createStatement();
	    }
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    	return false;
	    }
	    return true;
	}
	
	/**
	 * Returns whether there is currently a connection to the database.
	 * @return If the Connection object is not null.
	 */
	public static boolean isConnected()
	{
		return c != null;
	}
	
	/**
	 * Calls the Statement's executeUpdate method with the given string of SQL.
	 * @param sql The SQL command to execute.
	 * @return Whether the update was successful.
	 */
	private static boolean executeUpdate(String sql)
	{
		try
		{
			s.executeUpdate(sql);
			return true;
		}
		catch(SQLException e)
		{
			return false;
		}
	}
	
	/**
	 * Calls the Statement's executeQuery method with the given string of SQL.
	 * @param sql The SQL command to execute.
	 * @return The ResultSet found, or null if the query was unsuccessful.
	 */
	private static ResultSet executeQuery(String sql)
	{
		try
		{
			return s.executeQuery(sql);
		}
		catch(SQLException e)
		{
			return null;
		}
	}
	
	/**
	 * Calls the given ResultSet's getInt method with the given column and returns the retrieved integer.
	 * @param result The ResultSet to retrieve the integer from.
	 * @param col The number of the column to check.
	 * @return The retrieved integer, or -1 if unsuccessful: may lead to errors if -1 is stored in the database.
	 */
	private static int getIntFromRS(ResultSet result, int col)
	{
		try
		{
			return result.getInt(col);
		}
		catch(SQLException e)
		{
			return -1;
		}
	}
	
	/**
	 * Calls the given ResultSet's getString method with the given column and returns the retrieved string.
	 * @param result The ResultSet to retrieve the string from.
	 * @param col The number of the column to check.
	 * @return The retrieved string, or null if unsuccessful.
	 */
	private static String getStringFromRS(ResultSet result, int col)
	{
		try
		{
			return result.getString(col);
		}
		catch(SQLException e)
		{
			return null;
		}
	}
	
	/**
	 * Calls the ResultSet's next method.
	 * @param result The ResultSet to move to the next row of.
	 */
	private static void next(ResultSet result)
	{
		try
		{
			result.next();
		}
		catch(SQLException e) {}
	}
	
	/**
	 * Returns the current row number of the given ResultSet.
	 * @param result The ResultSet to check for the row number of.
	 * @return The row currently pointed to, or 0 if there is an error.
	 */
	private static int getRow(ResultSet result)
	{
		try
		{
			return result.getRow();
		}
		catch(SQLException e)
		{
			return 0;
		}
	}
	
	/**
	 * Verifies the email and password combination to log a user into their account.
	 * @param email The user's email.
	 * @param password The password to verify.
	 * @return The user's id number, or -1 if login was unsuccessful.
	 */
	public static int login(String email, String password)
	{
		ResultSet result = executeQuery("SELECT id, password FROM users WHERE email = \'" + email + "\'");
		if(result == null) return -1;
		
		next(result);
		if(getRow(result) == 0) return -1; // If there are no rows, the row number is 0.
		
		String hashed = getStringFromRS(result, 2);
		if(!BCrypt.checkpw(password, hashed)) return -1;
		else return getIntFromRS(result, 1);
	}
	
	/**
	 * Adds a new user to the system.
	 * @param email The user's verified email address.
	 * @param first The user's first name.
	 * @param last The user's last name
	 * @param password The user's password.
	 * @return Whether the user was added successfully.
	 */
	public static boolean addUser(String email, String first, String last, String password)
	{
		String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
		return executeUpdate("INSERT INTO users VALUES(DEFAULT, \'" + email + "\', \'" + first + "\', \'" + last + "\', \'" + hashed + "\')");
	}
	
	/**
	 * Updates the email address of the user with the given id.
	 * @param id The id of the user whose information to update.
	 * @param email The user's new email.
	 * @return If update was successful.
	 */
	public static boolean updateEmail(int id, String email)
	{
		return executeUpdate("UPDATE users SET email = \'" + email + "\' WHERE id = " + id);
	}
	
	/**
	 * Updates the first name of the user with the given id.
	 * @param id The id of the user whose information to update.
	 * @param name The user's first name.
	 * @return If update was successful.
	 */
	public static boolean updateFirstName(int id, String name)
	{
		return executeUpdate("UPDATE users SET first_name = \'" + name + "\' WHERE id = " + id);
	}
	
	/**
	 * Updates the last name of the user with the given id.
	 * @param id The id of the user whose information to update.
	 * @param name The user's last name.
	 * @return If update was successful.
	 */
	public static boolean updateLastName(int id, String name)
	{
		return executeUpdate("UPDATE users SET last_name = \'" + name + "\' WHERE id = " + id);
	}
	
	/**
	 * Updates the password of the user with the given id.
	 * @param id The id of the user whose information to update.
	 * @param password The user's password.
	 * @return If update was successful.
	 */
	public static boolean updatePassword(int id, String password)
	{
		String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
		return executeUpdate("UPDATE users SET password = \'" + hashed + "\' WHERE id = " + id);
	}
	
	/**
	 * Adds a new club to the system.
	 * @param name The club name.
	 * @param description A short description of the club.
	 * @return Whether the club was added successfully.
	 */
	public static boolean addClub(String name, String description)
	{
		return executeUpdate("INSERT INTO clubs VALUES(DEFAULT, \'" + name + "\', \'" + description + "\')");
	}
	
	/**
	 * Updates the name of the club with the given id.
	 * @param id The id of the club whose information to update.
	 * @param name The name of the club.
	 * @return If the update was successful.
	 */
	public static boolean updateClubName(int id, String name)
	{
		return executeUpdate("UPDATE clubs SET name = \'" + name + "\' WHERE id = " + id);
	}
	
	/**
	 * Updates the name of the club with the given id.
	 * @param id The id of the club whose information to update.
	 * @param name The name of the club.
	 * @return If the update was successful.
	 */
	public static boolean updateClubDescription(int id, String description)
	{
		return executeUpdate("UPDATE clubs SET description = \'" + description + "\' WHERE id = " + id);
	}
}