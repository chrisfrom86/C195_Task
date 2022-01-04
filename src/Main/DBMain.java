package Main;

public class DBMain {
    public static void main(String[] args) throws Exception{
        DB.makeConnection();
        System.out.println("Connection Successful");
        DB.closeConnection();
        System.out.println("Connection Closed");
    }
}
