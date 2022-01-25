package Contact;

import Main.DB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Chris Sequeira
 * this data access object interacts with the contacts table in the database.
 */
public class ContactDAO {

    /**
     * this method queries all entries in the contacts table.
     * @return an ObservableList of Contacts is returned to populate the TableView when this method is called.
     * @throws SQLException
     */
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

    /**
     * this method gets all contact info by its {@param id} which is referenced in the appointments table.
     * @param id
     * @return a Contact object is returned so that it can be loaded into an Appointment object.
     * @throws SQLException
     */
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

    /**
     * this method gets a contact ID from the database based on a given {@param name}.
     *
     * it is used to query the database for all appointments based on a given contact_ID.
     *
     * This is relevant in the report generated on the AppointmentView form.
     * @return the contact ID is returned as an Integer to pass into a database query in {@link Appointment.ApptDAO}.
     * @throws SQLException
     */
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
