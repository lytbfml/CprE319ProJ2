import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class ServerDBTest {

    //Table for testing
    public final static String tablename = "testUser";

    /**
     * Connect with database mysql.cs.iastate.edu/db319t46
     * Team46
     */
    public static Connection getConnection() {
        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            //Database url
            String url = "jdbc:mysql://mysql.cs.iastate.edu:3306/db319t46";
            //username and password
            String username = "dbu319t46";
            String password = "thu_Ru6e";
            //connect
            Connection con = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected!");
            return con;
        } catch (SQLException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            System.out.println(e);
        }
        return null;
    }

    /**
     * Test area
     */
    public static void main(String[] args) throws SQLException {
        System.out.println("Hello World!");
//        getConnection();
//        creatTable(tablename);
//        set("yangxiao", "123", "yangxiao@iastate.edu");
        System.out.println(get("yangxiao"));
    }

    /**
     * Create table in data base
     *
     * @param tableName the name of table
     * @throws SQLException SQLException for stmt closing
     */
    public static void creatTable(String tableName) throws SQLException {
        String createString = "CREATE TABLE IF NOT EXISTS " + tableName + "("
                + "username VARCHAR(30) NOT NULL, "
                + "password VARCHAR(30) NOT NULL, "
                + "email VARCHAR(30) NOT NULL, "
                + "PRIMARY KEY(username), UNIQUE KEY(username), UNIQUE KEY(email))";
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = getConnection();
            stmt = con.prepareStatement(createString);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }

            if (con != null) {
                con.close();
            }
        }
    }

    /**
     * Add elements in table if not exist. If it;s already in table, then do print it(do something)
     * @param email email
     * @param username username
     * @param password password
     * @throws SQLException
     */
    public static void set(String username, String password, String email) throws SQLException {
        //Initialize all connections and statements
        Connection con = null;
        PreparedStatement set = null;
        PreparedStatement read = null;

        try {
            con = getConnection();
            read = con.prepareStatement(
                    "SELECT * FROM " + tablename + " WHERE username = '" + username + "' LIMIT 1");
            ResultSet result = read.executeQuery();
            if (result.next()) {
                System.out.println("User " + username + " found! " + result.getString("username") + ", " + "password: "
                        + result.getString("password") + ", " + "email: " + result.getString("email"));
            } else {
                set = con.prepareStatement(
                        "INSERT INTO " + tablename + "(username, password, email) VALUES (?, ?, ?)");
                set.setString(1, username);
                set.setString(2, password);
                set.setString(3, email);
                set.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            //Close all connection and statement
            if (set != null) {
                set.close();
            }
            if (read != null) {
                read.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    /**
     * Get element from table
     * @param username
     * @return return
     * @throws SQLException
     */
    public static String get(String username) throws SQLException {
        Connection con = null;
        PreparedStatement read = null;
        String data = null;
        try {
            con = getConnection();
            read = con.prepareStatement(
                    "SELECT * FROM " + tablename + " WHERE USERNAME = '" + username + "' LIMIT 1"); // *

            ResultSet result = read.executeQuery();

            if (result.next()) {
                data = "User " + username + " found! " + ", " + "password: "
                        + result.getString("password") + ", " + "email: " + result.getString("email");
            } else {
                data = "User '" + username + "' does not exist!";
            }
            return data;
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        } finally {
            if (read != null) {
                read.close();
            }

            if (con != null) {
                con.close();
            }
        }
    }
}
