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
		try
		{
			s.executeUpdate("INSERT INTO users VALUES(DEFAULT, \'" + email + "\', \'" + first + "\', \'" + last + "\', \'" + hashed + "\')");
			return true;
		}
		catch(SQLException e)
		{
			return false;
		}
	}
	
	/**
	 * Adds a new club to the system.
	 * @param name The club name.
	 * @param description A short description of the club.
	 * @return Whether the club was added successfully.
	 */
	public static boolean addClub(String name, String description)
	{
		try
		{
			s.executeUpdate("INSERT INTO clubs VALUES(DEFAULT, \'" + name + "\', \'" + description + "\')");
			return true;
		}
		catch(SQLException e)
		{
			return false;
		}
	}
}