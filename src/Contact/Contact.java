package Contact;

/**
 * the Contact class defines the Contact objects for use throughout the application.
 *
 * Each appointment in the application has a single Contact object associated with it.
 */
public class Contact {
    public int contactID;
    public String contactName;
    public String contactEmail;

    public Contact() {
    }

    /**
     * the toString() method is overridden so that the modify appointment form ComboBox displays the contactName.
     * @return
     */
    @Override
    public String toString() { return contactName; }

    //setters

    public void setContactID(int contactID) { this.contactID = contactID; }

    public void setContactName(String contactName) { this.contactName = contactName; }

    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    //getters

    public int getContactID() { return contactID; }

    public String getContactName() { return contactName; }

    public String getContactEmail() { return contactEmail; }
}
