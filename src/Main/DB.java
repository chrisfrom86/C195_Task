package Main;

import java.sql.Connection;
import java.sql.DriverManager;

public class DB {
    private static final String databaseName="c195pa";
    private static final String DB_URL="jdbc:mysql://localhost:3306/"+databaseName;
    private static final String username="root";
    private static final String password="password";
    private static final String MYSQLJDBCDriver = "com.mysql.jdbc.Driver";
    static Connection conn;
    public static void makeConnection() throws Exception{
        Class.forName(MYSQLJDBCDriver);
        conn= DriverManager.getConnection(DB_URL,username,password);
    }
    public static void closeConnection() throws Exception{
        conn.close();
    }
}
