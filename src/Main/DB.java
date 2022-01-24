package Main;

import Appointment.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.sql.rowset.CachedRowSet;
import java.sql.*;

public class DB {
    private static final String databaseName="c195pa";
    private static final String DB_URL="jdbc:mysql://localhost:3306/"+databaseName;
    private static final String username="root";
    private static final String password="password";
    private static final String MYSQLJDBCDriver = "com.mysql.cj.jdbc.Driver";
    static Connection conn;

    public static void makeConnection(){
        try {
            Class.forName(MYSQLJDBCDriver);
        }
        catch (ClassNotFoundException e) {
            System.out.println("DB - Where is your MySQL JDBC Driver?");
            e.printStackTrace();
        }

        try {
            conn = DriverManager.getConnection(DB_URL, username, password);
        }
        catch (SQLException e) {
            System.out.println("DB - Connection failed! Check output console");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return conn;
    }

    public static void closeConnection() throws Exception{
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        }
        catch (Exception e) {
            throw e;
        }

    }

    //this method is for insert/delete/update operation on database
    public static void dbExecuteUpdate(String sqlStmt) throws Exception {
        Statement stmt = null;
        try {
            makeConnection();
            System.out.println("DB - connection to db established");
            stmt = conn.createStatement();
            System.out.println("DB - SQL statement created");
            stmt.executeUpdate(sqlStmt);
            System.out.println("DB - stmt.executeUpdate(sqlStmt) successful. db updated");

        }
        catch (SQLException e) {
            System.out.println("DB - Problem occurred at dbExecuteUpdate operation " +e);
            throw e;
        }
        finally {
            if(stmt != null) {
                //stmt.close();
            }
            //closeConnection();
        }
    }

    //to retrieve data from database
    public static ResultSet dbExecuteQuery(String sqlQuery) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            makeConnection();
            System.out.println("DB - connection to db established");
            stmt = conn.prepareStatement(sqlQuery);
            System.out.println("DB - SQL statement created");
            stmt.executeQuery(sqlQuery);
            System.out.println("DB - stmt.executeQuery(sqlQuery) successful. db updated");
            rs = stmt.executeQuery(sqlQuery);
        }
        catch (SQLException e){
            System.out.println("DB - Error occurred in dbExecute operation " +e);
            throw e;
        }
        finally {
            if(rs != null) {
                //rs.close();
            }
            if (stmt != null) {
                //stmt.close();
            }
            //closeConnection();
        }
        return rs;
    }

}
