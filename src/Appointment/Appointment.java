package Appointment;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    public IntegerProperty apptID;
    public String apptTitle;
    public String apptDescription;
    public String apptLocation;
    public String apptContact;
    public String apptType;
    public Timestamp apptStart;
    public Timestamp apptEnd;
    public SimpleIntegerProperty apptCustomerID;
    public SimpleIntegerProperty apptUserID;

    public Appointment() {
        this.apptID = new SimpleIntegerProperty();
        this.apptTitle = new String();
        this.apptDescription = new String();
        this.apptLocation = new String();
        this.apptContact = new String();
        this.apptType = new String();
        this.apptStart = apptStart;
        this.apptEnd = apptEnd;
        this.apptCustomerID = new SimpleIntegerProperty();
        this.apptUserID = new SimpleIntegerProperty();
    }

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

    public void setApptContact(String apptContact) {
        this.apptContact = apptContact;
    }

    public void setApptType(String apptType) {
        this.apptType = apptType;
    }

    public void setApptStart(Timestamp apptStart) {
        this.apptStart = apptStart;
    }

    public void setApptEnd(Timestamp apptEnd) {
        this.apptEnd = apptEnd;
    }

    public void setApptCustomerID(int id) {
        this.apptCustomerID.set(id);
    }

    public void setApptUserID(int id) {
        this.apptUserID.set(id);
    }

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

    public String getApptContact() {
        return apptContact;
    }

    public String getApptType() {
        return apptType;
    }

    public Timestamp getApptStart() {
        return apptStart;
    }

    public Timestamp getApptEnd() {
        return apptEnd;
    }

    public int getApptCustomerID() {
        return apptCustomerID.get();
    }

    public int getApptUserID() {
        return apptUserID.get();
    }

}
