package Customer;

import Location.Location;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Customer {
    public IntegerProperty customerID;
    public String customerName;
    public String customerAddress;
    public String customerPostalCode;
    public String customerPhone;
    public Location customerLocation;

    public void Customer() {
        this.customerID = new SimpleIntegerProperty();
        this.customerName = new String();
        this.customerAddress = new String();
        this.customerPostalCode = new String();
        this.customerPhone = new String();
        this.customerLocation = new Location();
    }

    public void setCustomerID(int customerID) {
        this.customerID.set(customerID);
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public void setCustomerPostalCode(String customerPostalCode) {
        this.customerPostalCode = customerPostalCode;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public void setCustomerLocation(Location customerLocation) {
        this.customerLocation = customerLocation;
    }

    public int getCustomerID() {
        return customerID.get();
    }

    public IntegerProperty customerIDProperty() {
        return customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public String getCustomerPostalCode() {
        return customerPostalCode;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public Location getCustomerLocation() {
        return customerLocation;
    }
}
