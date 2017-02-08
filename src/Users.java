import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Methods to create, access, and modify user information.
 * @author Julia McClellan
 */
public class Users
{
	/**
	 * Verifies the email and password combination to log a user into their account.
	 * @param email The user's email.
	 * @param password The password to verify.
	 * @return The user's id number, or -1 if login was unsuccessful.
	 */
	public static int login(String email, String password)
	{
		ResultSet result = Data.executeQuery("SELECT id, password FROM users WHERE email = \'" + email + "\'");
		if(result == null) return -1;
		
		Data.next(result);
		if(Data.getRow(result) == 0) return -1; // If there are no rows, the row number is 0.
		
		String hashed = Data.getStringFromRS(result, 2);
		if(!BCrypt.checkpw(password, hashed)) return -1;
		else return Data.getIntFromRS(result, 1);
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
		return Data.executeUpdate("INSERT INTO users VALUES(DEFAULT, \'" + email + "\', \'" + first + "\', \'" + last + "\', \'" + hashed + "\')");
	}
	
	/**
	 * Updates the email address of the user with the given id.
	 * @param id The id of the user whose information to update.
	 * @param email The user's new email.
	 * @return If update was successful.
	 */
	public static boolean updateEmail(int id, String email)
	{
		return Data.executeUpdate("UPDATE users SET email = \'" + email + "\' WHERE id = " + id);
	}
	
	/**
	 * Updates the first name of the user with the given id.
	 * @param id The id of the user whose information to update.
	 * @param name The user's first name.
	 * @return If update was successful.
	 */
	public static boolean updateFirstName(int id, String name)
	{
		return Data.executeUpdate("UPDATE users SET first_name = \'" + name + "\' WHERE id = " + id);
	}
	
	/**
	 * Updates the last name of the user with the given id.
	 * @param id The id of the user whose information to update.
	 * @param name The user's last name.
	 * @return If update was successful.
	 */
	public static boolean updateLastName(int id, String name)
	{
		return Data.executeUpdate("UPDATE users SET last_name = \'" + name + "\' WHERE id = " + id);
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
		return Data.executeUpdate("UPDATE users SET password = \'" + hashed + "\' WHERE id = " + id);
	}
}