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
		ResultSet rs = Data.executeQuery("SELECT id FROM clubs WHERE name = \'" + name + "\'");
		Data.next(rs);
		if(Data.getRow(rs) != 0) return false; //clubs can't have the same name
		
		if(!Data.executeUpdate("INSERT INTO clubs VALUES(DEFAULT, \'" + name + "\', \'" + description + "\')")) return false;
		
		rs = Data.executeQuery("SELECT id FROM clubs WHERE name = \'" + name + "\'");
		Data.next(rs);
		int id = Data.getIntFromRS(rs, 1);
		
		for(int head: heads)
		{
			if(!Data.executeUpdate("INSERT INTO members VALUES(" + id + ", " + head + ", TRUE)")) return false;
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
	 * @param userID The user to sort the clubs for.
	 * @return The clubs in an array of ArrayLists, with the first having clubs the user is not part of, the second where the user is a member, and the third where the
	 * member is a head.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList<Integer>[] getClubs(int userID)
	{
		ArrayList[] temp = new ArrayList[3];
		ArrayList<Integer>[] result = (ArrayList<Integer>[])temp;
		
		ResultSet clubs = Data.executeQuery("SELECT club FROM members WHERE \"user\" = " + userID + " AND is_head = FALSE");
		result[1] = convertToArray(clubs);
		clubs = Data.executeQuery("SELECT club FROM members WHERE \"user\" = " + userID + " AND is_head = TRUE");
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
		
		clubs = Data.executeQuery("SELECT id FROM clubs WHERE NOT id in (" + str + ")");
		result[0] = convertToArray(clubs);
		
		return result;
	}
	
	private static ArrayList<Integer> convertToArray(ResultSet data)
	{
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(; Data.next(data); Data.afterLast(data))
		{
			result.add(Data.getIntFromRS(data, 1));
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
		return Data.executeUpdate("INSERT INTO members VALUES(" + clubID + ", " + userID + ", FALSE)");
	}
	
	/**
	 * Removes the given member from the club.
	 * @param clubID The club to remove the member from.
	 * @param userID The user to remove from the club.
	 * @return Whether the operation was successful.
	 */
	public static boolean removeMember(int clubID, int userID)
	{
		return Data.executeQuery("DELETE FROM members WHERE club = " + clubID + " AND user = " + userID) != null;
	}
	
	/**
	 * Adds a head to the club. Before calling, it should be verified that the user is not already a head of the club.
	 * @param clubID The ID of the club to add a head to.
	 * @param userID The ID of the user to add to the club.
	 * @return Whether the update was successful.
	 */
	public static boolean addHead(int clubID, int userID)
	{
		return Data.executeUpdate("INSERT INTO members VALUES(" + clubID + ", " + userID + ", TRUE)");
	}
}