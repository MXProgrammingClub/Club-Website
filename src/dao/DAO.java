package dao;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles storage and retrieval of information from the database.
 * @author Julia McClellan
 */
public class DAO
{
	/**
	 * Creates a statement.
	 * @return The statement
	 */
	public static Statement statement(Connection c)
	{
		try
		{
			return c.createStatement();
		}
		catch(SQLException e)
		{
			return null;
		}
	}
	
	/**
	 * Calls the Statement's executeUpdate method with the given string of SQL.
	 * @param sql The SQL command to execute.
	 * @return Whether the update was successful.
	 */
	public static boolean executeUpdate(String sql, Statement s)
	{
		try
		{
			s.executeUpdate(sql);
			return true;
		}
		catch(SQLException e)
		{e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Calls the Statement's executeQuery method with the given string of SQL.
	 * @param sql The SQL command to execute.
	 * @return The ResultSet found, or null if the query was unsuccessful.
	 */
	public static ResultSet executeQuery(String sql, Statement s)
	{
		try
		{
			return s.executeQuery(sql);
		}
		catch(SQLException e)
		{e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Calls the given ResultSet's getInt method with the given column and returns the retrieved integer.
	 * @param result The ResultSet to retrieve the integer from.
	 * @param col The number of the column to check.
	 * @return The retrieved integer, or -1 if unsuccessful: may lead to errors if -1 is stored in the database.
	 */
	public static int getIntFromRS(ResultSet result, int col)
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
	public static String getStringFromRS(ResultSet result, int col)
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
	 * Calls the given ResultSet's getArray method with the given column and returns the retrieved array.
	 * @param result The ResultSet to retrieve the array from.
	 * @param col The number of the column to check.
	 * @return The retrieved array, or null if unsuccessful.
	 */
	public static Array getArrayFromRS(ResultSet result, int col)
	{
		try
		{
			return result.getArray(col);
		}
		catch(SQLException e)
		{
			return null;
		}
	}
	
	public static Object getArray(Array a)
	{
		try
		{
			return a.getArray();
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
	public static boolean next(ResultSet result)
	{
		try
		{
			return result.next();
		}
		catch(SQLException e)
		{
			return false;
		}
	}
	
	/**
	 * Returns the current row number of the given ResultSet.
	 * @param result The ResultSet to check for the row number of.
	 * @return The row currently pointed to, or 0 if there is an error.
	 */
	public static int getRow(ResultSet result)
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
	 * Checks whether the cursor of the ResultSet is currently after the last row.
	 * @param result The ResultSet to check.
	 * @return Whether the cursor is after the last row, or true if there is an error.
	 */
	public static boolean afterLast(ResultSet result)
	{
		try
		{
			return result.isAfterLast();
		}
		catch(SQLException e)
		{
			return true;
		}
	}
	
	/**
	 * A toString method for an integer array to correspond with SQL syntax.
	 * @param a The array to represent as a string.
	 * @return The string version of the array.
	 */
	public static String arrayToString(int[] a)
	{
		if(a.length == 0) return "{}";
		String str = "{";
		for(int i: a)
		{
			str += i + ", ";
		}
		return str.substring(0, str.length() - 2) + "}";	
	}
}