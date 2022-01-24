package Main;

public class MainDAO {
    public static void main(String[] args) throws Exception{
        DB.makeConnection();
        System.out.println("DB - Connection Successful");
        DB.closeConnection();
        System.out.println("DB - Connection Closed");
    }
}
