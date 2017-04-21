package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import controller.ConnectionController;

/**
 * Methods to create, access, and modify club information.
 * @author Julia
 */
public class ClubDAO
{
	/**
	 * Adds a new club to the system.
	 * @param name The club name.
	 * @param description A short description of the club.
	 * @return Whether the club was added successfully.
	 */
	public static boolean addClub(String name, String description, int[] heads)
	{
		Connection c = ConnectionController.connect();
		Statement s = DAO.statement(c);
		ResultSet rs = DAO.executeQuery("SELECT id FROM clubs WHERE name = \'" + name + "\'", s);
		DAO.next(rs);
		if(DAO.getRow(rs) != 0) return false; //clubs can't have the same name
		
		if(!DAO.executeUpdate("INSERT INTO clubs VALUES(DEFAULT, \'" + name + "\', \'" + description + "\')", s)) return false;
		
		rs = DAO.executeQuery("SELECT id FROM clubs WHERE name = \'" + name + "\'", s);
		DAO.next(rs);
		int id = DAO.getIntFromRS(rs, 1);
		
		for(int head: heads)
		{
			if(!DAO.executeUpdate("INSERT INTO members VALUES(" + id + ", " + head + ", TRUE)", s)) return false;
		}
		return true;
	}
	
	/**
	 * Updates the name of the club with the given id.
	 * @param id The id of the club whose information to update.
	 * @param name The name of the club.
	 * @return If the update was successful.
	 */
	public static boolean updateClubName(int id, String name)
	{
		Connection c = ConnectionController.connect();
		Statement s = DAO.statement(c);
		return DAO.executeUpdate("UPDATE clubs SET name = \'" + name + "\' WHERE id = " + id, s);
	}
	
	/**
	 * Updates the name of the club with the given id.
	 * @param id The id of the club whose information to update.
	 * @param name The name of the club.
	 * @return If the update was successful.
	 */
	public static boolean updateClubDescription(int id, String description)
	{
		Connection c = ConnectionController.connect();
		Statement s = DAO.statement(c);
		return DAO.executeUpdate("UPDATE clubs SET description = \'" + description + "\' WHERE id = " + id, s);
	}
	
	/**
	 * Sorts the clubs into groups based on the user's participation. 
	 * @param userID The user to sort the clubs for.
	 * @return The clubs in an array of ArrayLists, with the first having clubs the user is not part of, the second where the user is a member, and the third where the
	 * member is a head.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList<Integer>[] getClubs(int userID)
	{
		Connection c = ConnectionController.connect();
		Statement s = DAO.statement(c);
		ArrayList[] temp = new ArrayList[3];
		ArrayList<Integer>[] result = (ArrayList<Integer>[])temp;
		
		ResultSet clubs = DAO.executeQuery("SELECT club FROM members WHERE \"user\" = " + userID + " AND is_head = FALSE", s);
		result[1] = convertToArray(clubs);
		clubs = DAO.executeQuery("SELECT club FROM members WHERE \"user\" = " + userID + " AND is_head = TRUE", s);
		result[2] = convertToArray(clubs);
		
		String str = "";
		for(int i = 1; i < 3; i++)
		{
			for(int id: result[i])
			{
				str += id + ", ";
			}
		}
		if(str.length() != 0) str = str.substring(0, str.length() - 2);
		
		clubs = DAO.executeQuery("SELECT id FROM clubs WHERE NOT id in (" + str + ")", s);
		result[0] = convertToArray(clubs);
		
		return result;
	}
	
	private static ArrayList<Integer> convertToArray(ResultSet data)
	{
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(; DAO.next(data); DAO.afterLast(data))
		{
			result.add(DAO.getIntFromRS(data, 1));
		}
		return result;
	}
	
	/**
	 * Adds a member to the club. Before calling, it should be verified that the user is not already a member of the club.
	 * @param clubID The ID of the club to add a member to.
	 * @param userID The ID of the user to add to the club.
	 * @return Whether the update was successful.
	 */
	public static boolean addMember(int clubID, int userID)
	{
		Connection c = ConnectionController.connect();
		Statement s = DAO.statement(c);
		return DAO.executeUpdate("INSERT INTO members VALUES(" + clubID + ", " + userID + ", FALSE)", s);
	}
	
	/**
	 * Removes the given member from the club.
	 * @param clubID The club to remove the member from.
	 * @param userID The user to remove from the club.
	 * @return Whether the operation was successful.
	 */
	public static boolean removeMember(int clubID, int userID)
	{
		Connection c = ConnectionController.connect();
		Statement s = DAO.statement(c);
		return DAO.executeQuery("DELETE FROM members WHERE club = " + clubID + " AND user = " + userID, s) != null;
	}
	
	/**
	 * Adds a head to the club. Before calling, it should be verified that the user is not already a head of the club.
	 * @param clubID The ID of the club to add a head to.
	 * @param userID The ID of the user to add to the club.
	 * @return Whether the update was successful.
	 */
	public static boolean addHead(int clubID, int userID)
	{
		Connection c = ConnectionController.connect();
		Statement s = DAO.statement(c);
		return DAO.executeUpdate("INSERT INTO members VALUES(" + clubID + ", " + userID + ", TRUE)", s);
	}
}