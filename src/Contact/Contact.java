package Contact;

public class Contact {
    public int contactID;
    public String contactName;
    public String contactEmail;

    public Contact() {
    }

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
