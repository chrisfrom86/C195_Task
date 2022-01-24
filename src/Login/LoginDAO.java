package Login;

import Main.DB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO extends Login {

    public static boolean tryLogin(String username, String password) throws SQLException {
        boolean loginSuccess = false;

        loginSuccess = checkUsername(username) && checkPassword(password);

        return loginSuccess;
    }

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

    public static boolean checkPassword(String password) throws SQLException {
        System.out.println("LoginDAO - checkPassword");
        boolean passwordMatch = false;
        String sql = "select * from users where user_name = ?";
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ps.setString(1, password);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            try {
                passwordMatch = password.equalsIgnoreCase(rs.getString("user_name"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return passwordMatch;
    }

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
