package Appointment;

import Customer.Customer;
import Location.Country;
import Location.CountryDivision;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

/**
 * @author Chris Sequeira
 * the Appointment class defines the Appointment objects, which are the centerpiece of this application.
 *
 * Each appointment has multiple properties, notably custom objects defined in other packages throughout this application.
 */
public class Appointment {
    public IntegerProperty apptID;
    public String apptTitle;
    public String apptDescription;
    public String apptLocation;
    public Country apptCountry;
    public CountryDivision apptCountryDivision;
    public int apptContactID;
    public String apptType;
    public ZonedDateTime apptStart;
    public ZonedDateTime apptEnd;
    public Customer apptCustomer;
    public SimpleIntegerProperty apptCustomerID;
    public SimpleIntegerProperty apptUserID;

    public Appointment() {
        this.apptID = new SimpleIntegerProperty();
        this.apptTitle = new String();
        this.apptDescription = new String();
        this.apptLocation = new String();
        this.apptCountry = new Country();
        this.apptCountryDivision = new CountryDivision();
        this.apptType = new String();
        this.apptCustomer = new Customer();
        this.apptCustomerID = new SimpleIntegerProperty();
        this.apptUserID = new SimpleIntegerProperty();
    }

    //setters

    public void setApptID(int id) {
        this.apptID.set(id);
    }

    public void setApptTitle(String apptTitle) {
        this.apptTitle = apptTitle;
    }

    public void setApptDescription(String apptDescription) {
        this.apptDescription = apptDescription;
    }

    public void setApptLocation(String apptLocation) {
        this.apptLocation = apptLocation;
    }

    public void setApptContact(int apptContactID) {
        this.apptContactID = apptContactID;
    }

    public void setApptType(String apptType) {
        this.apptType = apptType;
    }

    public void setApptStart(ZonedDateTime apptStart) {
        this.apptStart = apptStart;
    }

    public void setApptEnd(ZonedDateTime apptEnd) {
        this.apptEnd = apptEnd;
    }

    public void setApptCustomerID(int id) {
        this.apptCustomerID.set(id);
    }

    public void setApptUserID(int id) {
        this.apptUserID.set(id);
    }

    public void setApptCountry(Country apptCountry) {
        this.apptCountry = apptCountry;
    }

    public void setApptCountryDivision(CountryDivision apptCountryDivision) {
        this.apptCountryDivision = apptCountryDivision;
    }

    public void setApptCustomer(Customer apptCustomer) {
        this.apptCustomer = apptCustomer;
    }

    //getters

    public void getApptStartTime(LocalDate date, int startHour, int startMin) {
        Date.valueOf(date);
    LocalTime.of(startHour, startMin);
    }

    public void getApptEndTime(LocalDate date, int endHour, int endMin) {

    }

    public int getApptID() {
        return apptID.get();
    }

    public String getApptTitle() {
        return apptTitle;
    }

    public String getApptDescription() {
        return apptDescription;
    }

    public String getApptLocation() {
        return apptLocation;
    }

    public int getApptContactID() {
        return apptContactID;
    }

    public String getApptType() {
        return apptType;
    }

    public ZonedDateTime getApptStart() {
        return apptStart;
    }

    public ZonedDateTime getApptEnd() {
        return apptEnd;
    }

    public int getApptCustomerID() {
        return apptCustomerID.get();
    }

    public int getApptUserID() {
        return apptUserID.get();
    }

    public IntegerProperty apptIDProperty() {
        return apptID;
    }

    public Country getApptCountry() {
        return apptCountry;
    }

    public int getApptCountryID() { return apptCountry.getCountryID(); }

    public CountryDivision getApptCountryDivision() {
        return apptCountryDivision;
    }

    public int getApptCountryDivisionID() { return apptCountryDivision.getDivisionID(); }

    public Customer getApptCustomer() {
        return apptCustomer;
    }

    public SimpleIntegerProperty apptCustomerIDProperty() {
        return apptCustomerID;
    }

    public SimpleIntegerProperty apptUserIDProperty() {
        return apptUserID;
    }
}
