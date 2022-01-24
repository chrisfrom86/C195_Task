package Customer;

import Location.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Customer {
    public IntegerProperty customerID;
    public String customerName;
    public String customerAddress;
    public String customerPostalCode;
    public String customerPhone;
    public Country customerCountry;
    public CountryDivision customerCountryDivision;

    public Customer() {
    }

    @Override
    public String toString(){ return (customerName); }

    //setters

    public void setCustomerID(int customerID) {
        this.customerID = new SimpleIntegerProperty();
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

    public void setCustomerCountry(Country customerCountry) {
        this.customerCountry = customerCountry;
    }

    public void setCustomerCountryDivision(CountryDivision customerCountryDivision) {
        this.customerCountryDivision = customerCountryDivision;
    }

    //getters

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

    public Country getCustomerCountry() {
        return customerCountry;
    }

    public CountryDivision getCustomerCountryDivision() {
        return customerCountryDivision;
    }

    public int getCustomerCountryID() { return customerCountry.countryID.getValue(); }

    public String getCustomerCountryName() {
        return customerCountry.getCountry();
    }

    public String getCustomerDivision() {
        return customerCountryDivision.getDivision();
    }
}
