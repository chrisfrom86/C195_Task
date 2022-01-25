package Customer;

import Location.Country;
import Location.CountryDivision;
import Main.DB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import static Location.LocDAO.*;

/**
 * @author Chris Sequeira
 *
 * The CustomerDAO class is the data access object for getting info from the customers table.
 */
public class CustomerDAO {

    /**
     * this method queries all entries in the customers table and loads each Customer into an observable list.
     * @return
     * @throws Exception
     */
    public static ObservableList<Customer> getAllCustomers() throws Exception {
        String sql = "select * from customers, first_level_divisions where customers.division_id = first_level_divisions.division_id";
        ObservableList<Customer> custList = FXCollections.observableArrayList();
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        //System.out.println("result set = " + rs);

        try {
            System.out.println("CustomerDAO - Select * from customers command issued to db");
            while (rs.next()) {
                Customer cust = new Customer();
                //System.out.println(rs.getInt("customer_id"));
                //System.out.println(rs.getMetaData());
                cust.setCustomerID(rs.getInt("Customer_ID"));
                cust.setCustomerName(rs.getString("customer_name"));
                cust.setCustomerAddress(rs.getString("address"));
                cust.setCustomerPostalCode(rs.getString("postal_code"));
                cust.setCustomerPhone(rs.getString("phone"));
                CountryDivision div = new CountryDivision();
                div.setDivision(rs.getString("division"));
                div.setDivisionID(rs.getInt("division_id"));
                Country loc = new Country();
                loc.setCountry(getCountryOfDivisionByID(rs.getInt("country_id")));
                loc.setCountryID(rs.getInt("country_id"));
                cust.setCustomerCountry(loc);
                cust.setCustomerCountryDivision(div);
                custList.add(cust);
                //System.out.println(rs.getInt(1) + " added to " + custList);
            }
            System.out.println("CustomerDAO - Customer objects added to ObservableList<Customer>");
        } catch (SQLException e) {
            System.out.println("CustomerDAO - Error querying all customers");
            e.printStackTrace();
        }

        return custList;
    }

    /**
     * this method gets all info about a customer from the customers table based on a given ID {@param id} and stores it into a Customer object.
     *
     * @return single Customer object populated with information from the customers table based on a given ID.
     * @throws Exception
     */
    public static Customer getCustomerByID(int id) throws Exception {
        DB.getConnection();
        String sql = "select * from customers where customer_id = ?";
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        Customer result = new Customer();

        while (rs.next()) {
            result.setCustomerID(rs.getInt("customer_id"));
            result.setCustomerName(rs.getString("customer_name"));
            result.setCustomerAddress(rs.getString("address"));
            result.setCustomerPostalCode(rs.getString("postal_code"));
            result.setCustomerPhone(rs.getString("phone"));
            CountryDivision div = getDivisionByID(rs.getInt("division_id"));
            result.setCustomerCountry(getCountryByID(div.countryID));
            result.setCustomerCountryDivision(getDivisionByID(rs.getInt("division_id")));
        }
        return result;
    }

    /**
     * This method takes validated information about a customer and parses it into a PreparedStatement, then inserts it into the database.
     * @param name
     * @param address
     * @param postalCode
     * @param phone
     * @param div
     * @throws SQLException
     */
    public static void addCustomer(String name, String address, String postalCode, String phone, CountryDivision div) throws SQLException {
        DB.getConnection();
        String sql = "INSERT INTO customers(customer_name, address, postal_code, phone, create_date, created_by, last_update, last_updated_by, division_id)" +
                " VALUES(?,?,?,?,NOW(),?,NOW(),?,?);";
        //String sql = "insert into customers(customer_name, address, postal_code, phone, division_id) values(?,?,?,?,?);";
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setString(5, "created_by user");
        ps.setString(6, "last_updated_by user");
        CountryDivision loc = div;
        ps.setInt(7, loc.getDivisionID());

        ps.executeUpdate();
    }

    /**
     * This method takes validated information about a customer and parses it into a PreparedStatement, then updates the database based on a given {@param id}.
     * @param id
     * @param name
     * @param address
     * @param postalCode
     * @param phone
     * @param div
     * @throws SQLException
     */
    public static void updateCustomer(int id, String name, String address, String postalCode, String phone, CountryDivision div) throws SQLException {
        DB.getConnection();
        String sql = "update customers set customer_name = ?, address = ?, postal_code = ?, phone= ?, last_update = NOW(), last_updated_by = ?, division_id = ? where customer_id = ?";
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        CountryDivision loc = div;
        ps.setString(5, "Last Update User"); // need to add as constructor parameter
        ps.setInt(6, loc.getDivisionID());
        ps.setInt(7, id);

        ps.executeUpdate();
    }

    /**
     * this method deletes a row from the customers table based on a given {@param id}.
     *
     * if a customer has appointments, it will throw a SQL exception based on foreign key constraints.
     * @param id
     * @return if successful, a String is returned confirming the ID of the deleted Customer. if failed due to existing appointments, an error String is returned.
     * @throws SQLException
     */
    public static String deleteCustomer(int id) throws SQLException {
        DB.getConnection();
        try {
            String sql = "delete from customers where customer_id = ?";
            PreparedStatement ps = DB.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return "Customer ID " + id + " deleted";
        } catch (SQLIntegrityConstraintViolationException e){
            return "Delete customer's appointments first";
        }
    }
}
