import java.sql.Connection;
import java.sql.DriverManager;
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
	 * Adds a new user to the system.
	 * @param email The user's verified email address.
	 * @param first THe user's first name.
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