package Contact;

import Customer.Customer;
import Main.DB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactDAO {

    public static ObservableList<Contact> getAllContacts() throws SQLException {
        System.out.println("contactDAO - querying all contacts from db");
        String sql = "select * from contacts";
        ObservableList<Contact> contactList = FXCollections.observableArrayList();
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            try {
                Contact contact = new Contact();
                contact.contactID = rs.getInt("contact_id");
                contact.contactName = rs.getString("contact_name");
                contact.contactEmail = rs.getString("email");
                contactList.add(contact);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return contactList;
    }

    public static Contact getContactByID(int id) throws SQLException {
        System.out.println("contactDAO - getting contact by ID");
        String sql = "select * from contacts where contact_id = ?";
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        Contact contact = new Contact();

        while (rs.next()) {
            try {
                contact.contactID = rs.getInt("contact_id");
                contact.contactName = rs.getString("contact_name");
                contact.contactEmail = rs.getString("email");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return contact;
    }

    public static int getContactIDByName(String name) throws SQLException {
        int id = 0;
        System.out.println("contactDAO - getting contact by name");
        String sql = "select * from contacts where contact_name = ?";
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        Contact contact = new Contact();

        while (rs.next()) {
            try {
                contact.contactID = rs.getInt("contact_id");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return contact.contactID;
    }
}
