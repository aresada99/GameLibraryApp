package Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Database {
	private static Connection connection = null;
private static boolean cond=true;
	public static void establishConnection() {
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:Database.db");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void closeConnection() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			establishConnection();
			Statement statement = connection.createStatement();

			String createTableQueryUsers = "CREATE TABLE IF NOT EXISTS Users ("
					+ "    uid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, username VARCHAR(20),"
					+ "    password VARCHAR(20), balance FLOAT(5)  CHECK (balance >= 0),  logged_in BOOL);";
			statement.executeUpdate(createTableQueryUsers);
			String createTableQueryGames = "CREATE TABLE IF NOT EXISTS Games ("
					+ "    gid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name VARCHAR(20),"
					+ "    description VARCHAR(40), publisher VARCHAR(20),"
					+ "    price FLOAT(5)  CHECK (price >= 0), score FLOAT(5)  CHECK (score >= 0),"
					+ "    is_multi BOOL);";
			statement.executeUpdate(createTableQueryGames);
			String createTableQueryGame_Ownership = "CREATE TABLE IF NOT EXISTS Game_Ownership ("
					+ "    uid INT(5) NOT NULL, gid INT(5) NOT NULL, PRIMARY KEY (uid,gid),"
					+ "    FOREIGN KEY (uid) REFERENCES Users (uid) ON DELETE CASCADE,"
					+ "    FOREIGN KEY (gid) REFERENCES Games (gid) ON DELETE CASCADE);";
			statement.executeUpdate(createTableQueryGame_Ownership);

			Scanner input = new Scanner(System.in);
			Scanner input2 = new Scanner(System.in);
			Scanner input3 = new Scanner(System.in);
			Scanner input4 = new Scanner(System.in);
			input4.useLocale(Locale.US);
			while (cond) {
				System.out.println(
						"(1)Login\n(2)Register\n(3)Add Admin(Not Done)\n(4)Add Example Users and Games\n(5)A User's Games\n(6)List Game Info\n(7)List Game Info of User\n(8)List All Users\n(9)List All Games\n(10)List All Ownership\n(11)Clear Tables\n(12)Exit");
				System.out.print("Enter a code number for the operation: ");
				String code = input.nextLine();
				switch (code) {
				case "1": {
					while (true) {
						System.out.print("Enter username: ");
						String username = input.nextLine();
						System.out.print("Enter password: ");
						String password = input.nextLine();
						boolean login = login(username, password);
						if (login) {
							System.out.println("Hello " + username);
							setLoggedAllOut();
							System.out.println("a" + whoLoggedIn());
							setLoggedIn(username);
							System.out.println("" + whoLoggedIn());

							int uid = getUid(username);
							System.out.println("User ID: " + uid);
							float balance = getBalance(uid);
							System.out.println("Balance: " + balance);
							ArrayList<Integer> gids = listGamesOfUser(uid);
							for (int i = 0; i < gids.size(); i++) {
								ArrayList<String> info = listGameInfo(gids.get(i));
								for (int j = 0; j < info.size(); j++) {
									System.out.print(info.get(j) + " ");
								}
								System.out.println(";");
							}
							System.out.println("(1)Buy Game\n(2)Refund Game\n(3)Add funds\n(4)Logout");
							System.out.print("Enter a code number for the operation: ");
							String code2 = input.nextLine();
							switch (code2) {
							case "1": {
								ArrayList<Integer> gids2 = listGamesOfNotOwnedByUser(uid);
								System.out.println("Store:");
								for (int i = 0; i < gids2.size(); i++) {
									ArrayList<String> info = listGameInfo(gids2.get(i));
									for (int j = 0; j < info.size(); j++) {
										System.out.print(info.get(j) + " ");
									}
									System.out.println(";");
								}
								System.out.print("Enter the game ID: ");
								int gid = input2.nextInt();
								if (getPrice(gid) <= balance) {
									addGameToUser(uid, gid);
									setBalance(uid, balance - getPrice(gid));
									System.out
											.println("Game " + getName(gid) + " purchased for " + getPrice(gid) + ".");
								} else {
									System.out.println("Insufficent balance.");
								}
								break;
							}
							case "2": {
								System.out.print("Enter the game ID: ");
								int gid = input2.nextInt();
								PreparedStatement stmt = connection
										.prepareStatement("DELETE FROM Game_Ownership WHERE uid=? AND gid=?;");
								stmt.setInt(1, uid);
								stmt.setInt(2, gid);
								stmt.executeUpdate();
								setBalance(uid, balance + getPrice(gid));
								System.out.println("Game " + getName(gid) + " refunded for " + getPrice(gid) + ".");
								break;
							}
							case "3": {
								System.out.print("Add funds: ");
								float funds = input4.nextFloat();
								setBalance(uid, balance + funds);
								break;
							}
							case "4": {
								System.out.println("Logged Out.");
								return;
							}
							}
							break;
						} else {
							System.out.println("Incorrect Combination");
						}
					}
					break;
				}

				case "2": {
					System.out.print("Enter username: ");
					String username = input.nextLine();
					System.out.print("Enter password: ");
					String password = input.nextLine();
					if (!usernameExists(username)) {
						addUser(username, password, 0);
						System.out.println("Succesfully Regitstered.");
					} else {
						System.out.println("Username " + username + " already exists.");
					}
					break;
				}
				case "4": {
					System.out.print("Enter the amount of example users to add: ");
					int count = input2.nextInt();
					addExampleUsers(count);
					System.out.print("Enter the amount of example games to add: ");
					int count2 = input2.nextInt();
					addExampleGames(count2);
					int maxUid = getMaxUid();
					System.out.println(maxUid);
					int maxGid = getMaxGid();
					System.out.println(maxGid);
					System.out.print("Add random games to users (true or false): ");
					boolean choice = input3.nextBoolean();
					if (choice)
						addRandomGamesToUsers();
					break;
				}
				case "5": {
					System.out.print("Enter the user ID: ");
					int uid = input2.nextInt();
					ArrayList<Integer> gids = listGamesOfUser(uid);
					for (int i = 0; i < gids.size(); i++) {
						System.out.println(gids.get(i));
					}

					break;
				}

				case "6": {
					System.out.print("Enter the game ID: ");
					int gid = input2.nextInt();
					ArrayList<String> info = listGameInfo(gid);
					for (int i = 0; i < info.size(); i++) {
						System.out.println(info.get(i));
					}

					break;
				}
				case "7": {
					System.out.print("Enter the user ID: ");
					int uid = input2.nextInt();
					ArrayList<Integer> gids = listGamesOfUser(uid);
					for (int i = 0; i < gids.size(); i++) {
						ArrayList<String> info = listGameInfo(gids.get(i));
						for (int j = 0; j < info.size(); j++) {
							System.out.print(info.get(j) + " ");
						}
						System.out.println(";");
					}

					break;
				}
				case "8": {
					ArrayList<Integer> uids = listAllUsers();
					for (int i = 0; i < uids.size(); i++) {
						System.out.println(uids.get(i));
					}

					break;
				}
				case "9": {
					ArrayList<Integer> gids = listAllGames();
					for (int i = 0; i < gids.size(); i++) {
						System.out.println(gids.get(i));
					}

					break;
				}
				case "10": {
					ArrayList<Integer> uids = listAllUsers();
					for (int i = 0; i < uids.size(); i++) {
						System.out.println("Uid: " + uids.get(i));
						ArrayList<Integer> gids = listGamesOfUser(uids.get(i));
						for (int j = 0; j < gids.size(); j++) {
							System.out.println(uids.get(i) + ": " + gids.get(j));
						}
					}

					break;
				}
				case "11": {
					statement.executeUpdate("DELETE FROM Users WHERE uid>0;");
					statement.executeUpdate("UPDATE `sqlite_sequence` SET `seq` = 0 WHERE `name` = 'Users'");
					statement.executeUpdate("drop table if exists Users");
//					statement.executeUpdate("DELETE FROM Games WHERE gid>0;");
//					statement.executeUpdate("UPDATE `sqlite_sequence` SET `seq` = 0 WHERE `name` = 'Games'");
//					statement.executeUpdate("drop table if exists Games");
					statement.executeUpdate("DELETE FROM Game_Ownership WHERE uid>0 AND gid>0;");
					statement.executeUpdate("UPDATE `sqlite_sequence` SET `seq` = 0 WHERE `name` = 'Game_Ownership'");
					statement.executeUpdate("drop table if exists Game_Ownership");
					break;
				}

				case "12": {
					input.close();
					input2.close();
					input3.close();
					input4.close();
					statement.close();
					closeConnection();
					return;
				}
				default: {
					System.out.println("Try again.");
					break;
				}
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static boolean login(String username, String password) {
		boolean exists = false;
		try {
			PreparedStatement stmt = connection
					.prepareStatement("SELECT COUNT(*) FROM Users WHERE username=? AND password=?;");
			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				if (rs.getInt(1) > 0) {
					exists = true;
				}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return exists;
	}

	public static void addUser(String username, String password, float balance) {
		try {
			PreparedStatement stmt = connection
					.prepareStatement("insert into Users (username, password, balance) values (?,?,?);");
			stmt.setString(1, username);
			stmt.setString(2, password);
			stmt.setFloat(3, balance);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static void addGame(String name, String description, String publisher, float price, float score,
			boolean is_multi) {
		try {
			PreparedStatement stmt = connection.prepareStatement(
					"insert into Games (name, description, publisher, price, score, is_multi) values (?,?,?,?,?,?);");
			stmt.setString(1, name);
			stmt.setString(2, description);
			stmt.setString(3, publisher);
			stmt.setFloat(4, price);
			stmt.setFloat(5, score);
			stmt.setBoolean(6, is_multi);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static void addGameToUser(int uid, int gid) {
		try {
			PreparedStatement stmt = connection.prepareStatement("insert into Game_Ownership (uid, gid) values (?,?);");
			stmt.setInt(1, uid);
			stmt.setInt(2, gid);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static void deleteGameFromUser(int uid, int gid) {
		try {
			PreparedStatement stmt = connection.prepareStatement("DELETE FROM Game_Ownership WHERE uid=? AND gid=?;");
			stmt.setInt(1, uid);
			stmt.setInt(2, gid);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static ArrayList<Integer> listGamesOfUser(int uid) {
		ArrayList<Integer> gids = new ArrayList<Integer>();
		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT gid FROM Game_Ownership WHERE uid=?;");
			stmt.setInt(1, uid);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				gids.add(rs.getInt("gid"));
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return gids;
	}

	public static ArrayList<Integer> listGamesOfNotOwnedByUser(int uid) {
		ArrayList<Integer> gids = new ArrayList<Integer>();
		try {
			PreparedStatement stmt = connection.prepareStatement(
					"SELECT * FROM Games WHERE gid NOT in (SELECT gid FROM Game_Ownership WHERE uid=?);");
			stmt.setInt(1, uid);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				gids.add(rs.getInt("gid"));
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return gids;
	}

	public static ArrayList<String> listGameInfo(int gid) {
		ArrayList<String> info = new ArrayList<String>();
		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Games WHERE gid=?;");
			stmt.setInt(1, gid);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				info.add("" + rs.getInt("gid"));
				info.add(rs.getString("name"));
				info.add(rs.getString("description"));
				info.add(rs.getString("publisher"));
				info.add("" + rs.getFloat("price"));
				info.add("" + rs.getFloat("score"));
				info.add("" + rs.getBoolean("is_multi"));
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return info;
	}

	public static ArrayList<Integer> listAllUsers() {
		ArrayList<Integer> uids = new ArrayList<Integer>();
		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT uid FROM Users;");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				uids.add(rs.getInt("uid"));
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return uids;
	}

	public static ArrayList<Integer> listAllGames() {
		ArrayList<Integer> gids = new ArrayList<Integer>();
		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT gid FROM Games;");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				gids.add(rs.getInt("gid"));
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return gids;
	}

	public static ArrayList<String> listAllUsersInfo() {
		ArrayList<String> info = new ArrayList<String>();
		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Users;");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				info.add("" + rs.getInt("uid"));
				info.add(rs.getString("username"));
				info.add("" + rs.getString("description"));
				info.add("" + rs.getFloat("balance"));
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return info;
	}

	public static ArrayList<String> listAllGamesInfo() {
		ArrayList<String> info = new ArrayList<String>();
		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Games;");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				info.add("" + rs.getInt("gid"));
				info.add(rs.getString("name"));
				info.add(rs.getString("description"));
				info.add(rs.getString("publisher"));
				info.add("" + rs.getFloat("price"));
				info.add("" + rs.getFloat("score"));
				info.add("" + rs.getInt("score"));
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return info;
	}

	public static int getUid(String username) {
		int uid = 0;
		try {

			PreparedStatement stmt = connection.prepareStatement("SELECT uid FROM Users WHERE username=?;");
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				uid = rs.getInt("uid");
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return uid;
	}

	public static int getGid(String name) {
		int uid = 0;
		try {

			PreparedStatement stmt = connection.prepareStatement("SELECT gid FROM Games WHERE name=?;");
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				uid = rs.getInt("gid");
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return uid;
	}

	public static String getUsername(int uid) {
		String result = "";
		try {

			PreparedStatement stmt = connection.prepareStatement("SELECT username FROM Users WHERE uid=?;");
			stmt.setInt(1, uid);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("username");
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static void setUsername(int uid, String username) {
		try {
			PreparedStatement stmt = connection.prepareStatement("UPDATE Users SET username=? WHERE uid=?;");
			stmt.setString(1, username);
			stmt.setInt(2, uid);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static String getPassword(int uid) {
		String result = "";
		try {

			PreparedStatement stmt = connection.prepareStatement("SELECT password FROM Users WHERE uid=?;");
			stmt.setInt(1, uid);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("username");
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static void setPassword(int uid, String password) {
		try {
			PreparedStatement stmt = connection.prepareStatement("UPDATE Users SET password=? WHERE uid=?;");
			stmt.setString(1, password);
			stmt.setInt(2, uid);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static float getBalance(int uid) {
		float result = 0;
		try {

			PreparedStatement stmt = connection.prepareStatement("SELECT balance FROM Users WHERE uid=?;");
			stmt.setInt(1, uid);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result = rs.getFloat("balance");
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static void setBalance(int uid, float balance) {
		try {
			PreparedStatement stmt = connection.prepareStatement("UPDATE Users SET balance=? WHERE uid=?;");
			stmt.setFloat(1, balance);
			stmt.setInt(2, uid);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static String getName(int gid) {
		String result = "";
		try {

			PreparedStatement stmt = connection.prepareStatement("SELECT name FROM Games WHERE gid=?;");
			stmt.setInt(1, gid);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("name");
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static void setName(int gid, String name) {
		try {
			PreparedStatement stmt = connection.prepareStatement("UPDATE Games SET name=? WHERE gid=?;");
			stmt.setString(1, name);
			stmt.setInt(2, gid);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static String getDescription(int gid) {
		String result = "";
		try {

			PreparedStatement stmt = connection.prepareStatement("SELECT description FROM Games WHERE gid=?;");
			stmt.setInt(1, gid);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("description");
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static void setDescription(int gid, String description) {
		try {
			PreparedStatement stmt = connection.prepareStatement("UPDATE Games SET description=? WHERE gid=?;");
			stmt.setString(1, description);
			stmt.setInt(2, gid);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static String getPublisher(int gid) {
		String result = "";
		try {

			PreparedStatement stmt = connection.prepareStatement("SELECT publisher FROM Games WHERE gid=?;");
			stmt.setInt(1, gid);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("publisher");
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static void setPublisher(int gid, String publisher) {
		try {
			PreparedStatement stmt = connection.prepareStatement("UPDATE Games SET publisher=? WHERE gid=?;");
			stmt.setString(1, publisher);
			stmt.setInt(2, gid);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static float getPrice(int gid) {
		float result = 0;
		try {

			PreparedStatement stmt = connection.prepareStatement("SELECT price FROM Games WHERE gid=?;");
			stmt.setInt(1, gid);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result = rs.getFloat("price");
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static void setPrice(int gid, float price) {
		try {
			PreparedStatement stmt = connection.prepareStatement("UPDATE Games SET price=? WHERE gid=?;");
			stmt.setFloat(1, price);
			stmt.setInt(2, gid);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static float getScore(int gid) {
		float result = 0;
		try {

			PreparedStatement stmt = connection.prepareStatement("SELECT score FROM Games WHERE gid=?;");
			stmt.setInt(1, gid);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result = rs.getFloat("score");
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static void setScore(int gid, float score) {
		try {
			PreparedStatement stmt = connection.prepareStatement("UPDATE Games SET score=? WHERE gid=?;");
			stmt.setFloat(1, score);
			stmt.setInt(2, gid);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static boolean getIsMulti(int gid) {
		boolean result = false;
		try {

			PreparedStatement stmt = connection.prepareStatement("SELECT is_multi FROM Games WHERE gid=?;");
			stmt.setInt(1, gid);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result = rs.getBoolean("is_multi");
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static void setIsMulti(int gid, boolean is_multi) {
		try {
			PreparedStatement stmt = connection.prepareStatement("UPDATE Games SET is_multi=? WHERE gid=?;");
			stmt.setBoolean(1, is_multi);
			stmt.setInt(2, gid);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static void setLoggedIn(String username) {
		try {
			PreparedStatement stmt = connection.prepareStatement("UPDATE Users SET logged_in=true WHERE username=?;");
			stmt.setString(1, username);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static void setLoggedAllOut() {
		try {
			PreparedStatement stmt = connection.prepareStatement("UPDATE Users SET logged_in=false;");
			stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static int whoLoggedIn() {
		int result = 0;
		try {

			PreparedStatement stmt = connection.prepareStatement("SELECT uid FROM Users WHERE logged_in=true;");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result = rs.getInt("uid");
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static void deleteGameWithName(String name) {
		try {
			PreparedStatement stmt = connection.prepareStatement("DELETE FROM Games WHERE name=?;");
			stmt.setString(1, name);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static void deleteGame(int gid) {
		try {
			PreparedStatement stmt = connection.prepareStatement("DELETE FROM Games WHERE gid=?;");
			stmt.setInt(1, gid);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static boolean usernameExists(String username) {
		boolean exists = false;
		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) FROM Users WHERE username=?;");
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				if (rs.getInt(1) > 0) {
					exists = true;
				}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return exists;
	}

	public static int getMaxUid() {
		int uid = 0;
		try {

			PreparedStatement stmt = connection.prepareStatement("SELECT MAX(uid) FROM Users;");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				uid = rs.getInt(1);
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return uid;
	}

	public static int getMaxGid() {
		int gid = 0;
		try {

			PreparedStatement stmt = connection.prepareStatement("SELECT MAX(gid) FROM Games;");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				gid = rs.getInt(1);
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return gid;
	}

	public static void addExampleUsers(int count) {
		try {
			PreparedStatement stmt = connection
					.prepareStatement("insert into Users (username, password, balance) values (?,?,?);");
			int nextUid = getMaxUid() + 1;
			Random rand = new Random();
			int randomNumber;
			float randomFloat;
			for (int i = nextUid; i < nextUid + count; i++) {
				randomNumber = rand.nextInt(1000);
				randomFloat = rand.nextFloat();
				stmt.setString(1, "User" + i + "un");
				stmt.setString(2, "User" + i + "pw");
				stmt.setFloat(3, randomNumber + randomFloat);
				stmt.executeUpdate();
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static void addExampleGames(int count) {
		try {
			PreparedStatement stmt = connection.prepareStatement(
					"insert into Games (name, description, publisher, price, score, is_multi) values (?,?,?,?,?,?);");
			int nextGid = getMaxGid() + 1;
			Random rand = new Random();
			int randomNumber;
			float randomFloat;
			boolean randomBool;
			for (int i = nextGid; i < nextGid + count; i++) {
				randomNumber = rand.nextInt(99);
				randomFloat = rand.nextFloat();
				randomBool = rand.nextBoolean();
				stmt.setString(1, "Game" + i + "name");
				stmt.setString(2, "Game" + i + "description");
				stmt.setString(3, "Game" + i + "publisher");
				stmt.setFloat(4, randomNumber + randomFloat);
				randomNumber = rand.nextInt(99);
				randomFloat = rand.nextFloat();
				stmt.setFloat(5, randomNumber + randomFloat);
				stmt.setBoolean(6, randomBool);
				stmt.executeUpdate();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static void addRandomGamesToUsers() {
		try {
			PreparedStatement stmt = connection.prepareStatement("insert into Game_Ownership (uid, gid) values (?,?);");
			PreparedStatement stmt2 = connection.prepareStatement("SELECT COUNT(*) FROM Users WHERE uid=?;");
			PreparedStatement stmt3 = connection.prepareStatement("SELECT COUNT(*) FROM Games WHERE gid=?;");
			PreparedStatement stmt4 = connection
					.prepareStatement("SELECT COUNT(*) FROM Game_Ownership WHERE uid=? AND gid=?;");
			int maxUid = getMaxUid();
			int maxGid = getMaxGid();
			Random rand = new Random();
			boolean randomBool;
			for (int i = 1; i <= maxUid; i++) {
				boolean exists = false;
				stmt2.setInt(1, i);
				ResultSet rs = stmt2.executeQuery();
				if (rs.next())
					if (rs.getInt(1) > 0)
						exists = true;
				rs.close();
				for (int j = 1; j <= maxGid; j++) {
					boolean exists2 = false;
					stmt3.setInt(1, j);
					ResultSet rs2 = stmt3.executeQuery();
					if (rs2.next())
						if (rs2.getInt(1) > 0)
							exists2 = true;
					rs2.close();
					boolean exists3 = false;
					stmt4.setInt(1, i);
					stmt4.setInt(2, j);
					ResultSet rs3 = stmt4.executeQuery();
					if (rs3.next())
						if (rs3.getInt(1) > 0)
							exists3 = true;
					rs3.close();
					randomBool = rand.nextBoolean();
					if (randomBool && exists && exists2 && !exists3) {
						stmt.setInt(1, i);
						stmt.setInt(2, j);
						stmt.executeUpdate();
					}
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}