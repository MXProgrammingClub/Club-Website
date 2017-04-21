package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.mindrot.jbcrypt.BCrypt;

import controller.ConnectionController;

/**
 * Methods to create, access, and modify user information.
 * @author Julia McClellan
 */
public class UserDAO
{
	/**
	 * Verifies the email and password combination to log a user into their account.
	 * @param email The user's email.
	 * @param password The password to verify.
	 * @return The user's id number, or -1 if login was unsuccessful.
	 */
	public static int login(String email, String password)
	{
		Connection c = ConnectionController.connect();
		Statement s = DAO.statement(c);
		ResultSet result = DAO.executeQuery("SELECT id, password FROM users WHERE email = \'" + email + "\'", s);
		if(result == null) return -1;
		
		DAO.next(result);
		if(DAO.getRow(result) == 0) return -1; // If there are no rows, the row number is 0.
		
		String hashed = DAO.getStringFromRS(result, 2);
		if(!BCrypt.checkpw(password, hashed)) return -1;
		else return DAO.getIntFromRS(result, 1);
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
		Connection c = ConnectionController.connect();
		Statement s = DAO.statement(c);
		String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
		return DAO.executeUpdate("INSERT INTO users VALUES(DEFAULT, \'" + email + "\', \'" + first + "\', \'" + last + "\', \'" + hashed + "\')", s);
	}
	
	/**
	 * Updates the email address of the user with the given id.
	 * @param id The id of the user whose information to update.
	 * @param email The user's new email.
	 * @return If update was successful.
	 */
	public static boolean updateEmail(int id, String email)
	{
		Connection c = ConnectionController.connect();
		Statement s = DAO.statement(c);
		return DAO.executeUpdate("UPDATE users SET email = \'" + email + "\' WHERE id = " + id, s);
	}
	
	/**
	 * Updates the password of the user with the given id.
	 * @param id The id of the user whose information to update.
	 * @param password The user's password.
	 * @return If update was successful.
	 */
	public static boolean updatePassword(int id, String password)
	{
		Connection c = ConnectionController.connect();
		Statement s = DAO.statement(c);
		String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
		return DAO.executeUpdate("UPDATE users SET password = \'" + hashed + "\' WHERE id = " + id, s);
	}
}