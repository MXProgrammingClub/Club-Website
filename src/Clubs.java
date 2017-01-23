import java.sql.Array;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Methods to create, access, and modify club information.
 * @author Julia
 */
public class Clubs
{
	/**
	 * Adds a new club to the system.
	 * @param name The club name.
	 * @param description A short description of the club.
	 * @return Whether the club was added successfully.
	 */
	public static boolean addClub(String name, String description, int[] heads)
	{
		return Data.executeUpdate("INSERT INTO clubs VALUES(DEFAULT, \'" + name + "\', \'" + description + "\', \'{}\', \'" + Data.arrayToString(heads) + "\')");
	}
	
	/**
	 * Updates the name of the club with the given id.
	 * @param id The id of the club whose information to update.
	 * @param name The name of the club.
	 * @return If the update was successful.
	 */
	public static boolean updateClubName(int id, String name)
	{
		return Data.executeUpdate("UPDATE clubs SET name = \'" + name + "\' WHERE id = " + id);
	}
	
	/**
	 * Updates the name of the club with the given id.
	 * @param id The id of the club whose information to update.
	 * @param name The name of the club.
	 * @return If the update was successful.
	 */
	public static boolean updateClubDescription(int id, String description)
	{
		return Data.executeUpdate("UPDATE clubs SET description = \'" + description + "\' WHERE id = " + id);
	}
	
	/**
	 * Sorts the clubs into groups based on the user's participation. 
	 * @param userID
	 * @return  
	 */
	public static ResultSet[] getClubs(int userID)
	{
		ResultSet membership = Data.executeQuery("SELECT id, members, heads FROM clubs");
		
		ArrayList<ArrayList<Integer>> groups = new ArrayList<ArrayList<Integer>>(3);
		for(int i = 0; i < 3; i++)
		{
			groups.add(new ArrayList<Integer>());
		}
		
		while(Data.next(membership))
		{
			if(contains(membership, 3, userID)) // user is a head of a club
			{
				groups.get(2).add(new Integer(Data.getIntFromRS(membership, 1)));
			}
			else if(contains(membership, 2, userID)) // user is member of club
			{
				groups.get(1).add(new Integer(Data.getIntFromRS(membership, 1)));
			}
			else // user is not part of club
			{
				groups.get(0).add(new Integer(Data.getIntFromRS(membership, 1)));
			}
		}
		
		ResultSet[] information = new ResultSet[3];
		for(int i = 0; i < 3; i++)
		{
			String str = groups.get(i).toString();
			str = str.substring(1, str.length() - 1);
			information[i] = Data.executeQuery("SELECT id, name, description, heads FROM clubs WHERE id in (" + str + ")");
		}
		return information;
	}
	
	/**
	 * Returns whether the userID is in the array stored in the given column of the ResultSet.
	 * @param rs The ResultSet with the array.
	 * @param col The column of the array.
	 * @param userID The user to look for.
	 * @return Whether the user is in the group
	 */
	private static boolean contains(ResultSet rs, int col, int userID)
	{
		Array a = Data.getArrayFromRS(rs, col);
		if(a == null) return false;
		Integer[] members = (Integer[])Data.getArray(a);
		for(Integer id: members)
		{
			if(id == userID)
			{
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args)
	{
		Data.connect();
		System.out.println(getClubs(1));
	}
}