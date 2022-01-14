package Customer;

import Location.Location;
import Main.DB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {

    public static ObservableList<Customer> getAllCustomers() throws Exception {
        String sql = "select * from customers, first_level_divisions where customers.division_id = first_level_divisions.division_id";
        //customers.customer_id, first_level_divisions.division_id, first_level_divisions.division, customer_name, address, postal_code, phone, customers.division_id
        ObservableList<Customer> custList = FXCollections.observableArrayList();
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        System.out.println("result set = " + rs);

        try {
            System.out.println("Select * from customers command issued to db");
            while (rs.next()) {
                Customer cust = new Customer();
                System.out.println(rs.getInt("customer_id"));
                int id = rs.getInt("customer_id");
                System.out.println(id);
                cust.setCustomerID(id);
                cust.setCustomerName(rs.getString("customer_name"));
                cust.setCustomerAddress(rs.getString("address"));
                cust.setCustomerPostalCode(rs.getString("postal_code"));
                cust.setCustomerPhone(rs.getString("phone"));
                Location loc = new Location();
                loc.setDivision(rs.getString("division"));
                loc.setDivisionID(rs.getInt("division_id"));
                cust.setCustomerLocation(loc);
                custList.add(cust);
                System.out.println(rs.getInt(1) + " added to " + custList);
            }
            System.out.println("Customer objects added to ObservableList<Customer>");
        } catch (SQLException e) {
            System.out.println("Error querying all customers");
            e.printStackTrace();
        }

        return custList;
    }

    public void addCustomer() {

    }
}
