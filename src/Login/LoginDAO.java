package Login;

import Main.DB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Chris Sequeira
 *
 * The LoginDAO is the data access object file for Login.java.
 */
public class LoginDAO extends Login {

    /**
     * tryLogin takes two parameters {@param username} {@param password} and checks them using other methods in this class.
     * @return Boolean, true if username/password match entries in the database, false if either or both are incorrect/not found.
     * @throws SQLException
     */
    public static boolean tryLogin(String username, String password) throws SQLException {
        boolean loginSuccess;

        loginSuccess = checkUsername(username) && checkPassword(password);

        return loginSuccess;
    }

    /**
     * checkUsername takes a String {@param username} and checks it against the users table in the database.
     * The SQL command is prepared in a PreparedStatement and submitted to the database.
     * @return returns a boolean, true if there is a match in the database, false by default
     * @throws SQLException
     */
    public static boolean checkUsername(String username) throws SQLException {
        System.out.println("LoginDAO - checkUsername");
        boolean usernameMatch = false;
        String sql = "select * from users where user_name = ?";
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            try {
                usernameMatch = username.equalsIgnoreCase(rs.getString("user_name"));
                loggedInUserID = rs.getInt("user_id");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return usernameMatch;
    }

    /**
     * checkPassword takes a String {@param password} and checks it against the users table in the database.
     * The SQL command is prepared in a PreparedStatement and submitted to the database.
     * @return returns a boolean, true if there is a match in the database, false by default
     * @throws SQLException
     */
    public static boolean checkPassword(String password) throws SQLException {
        System.out.println("LoginDAO - checkPassword: " + password);
        boolean passwordMatch = false;
        String sql = "select * from users where password = ?";
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ps.setString(1, password);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            try {
                passwordMatch = password.equalsIgnoreCase(rs.getString("password"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return passwordMatch;
    }

    /**
     * getUsernameByID takes an Integer {@param id} and checks it against the users table in the database
     * The SQL command is prepared in a PreparedStatement and submitted to the database
     * @return
     * @throws SQLException
     */
    public static String getUsernameByID(int id) throws SQLException {
        DB.getConnection();
        String sql = "select * from users where user_id = ?";
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        String result = null;

        while (rs.next()) {
            result = rs.getString("user_name");
        }
        return result;
    }
}
