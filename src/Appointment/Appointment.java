package Appointment;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Appointment {
    public static int apptID;
    public static String apptTitle;
    public static String apptDescription;
    public static String apptLocation;
    public static String apptContact;
    public static String apptType;
    public static LocalDateTime apptStart;
    public static LocalDateTime apptEnd;
    public static int apptCustomerID;
    public static int apptUserID;

    public Appointment(int apptID,
                       String apptTitle,
                       String apptDescription,
                       String apptLocation,
                       String apptContact,
                       String apptType,
                       LocalDateTime apptStart,
                       LocalDateTime apptEnd,
                       int apptCustomerID,
                       int apptUserID) {
        this.apptID = apptID;
        this.apptTitle = apptTitle;
        this.apptDescription = apptDescription;
        this.apptLocation = apptLocation;
        this.apptContact = apptContact;
        this.apptType = apptType;
        this.apptStart = apptStart;
        this.apptEnd = apptEnd;
        this.apptCustomerID = apptCustomerID;
        this.apptUserID = apptUserID;
    }

    public void getApptStartTime(LocalDate date, int startHour, int startMin) {
        Date.valueOf(date);
    LocalTime.of(startHour, startMin);
    }

    public void getApptEndTime(LocalDate date, int endHour, int endMin) {

    }

    public static int getApptID() {
        return apptID;
    }

    public static String getApptTitle() {
        return apptTitle;
    }

    public static String getApptDescription() {
        return apptDescription;
    }

    public static String getApptLocation() {
        return apptLocation;
    }

    public static String getApptContact() {
        return apptContact;
    }

    public static String getApptType() {
        return apptType;
    }

    public static LocalDateTime getApptStart() {
        return apptStart;
    }

    public static LocalDateTime getApptEnd() {
        return apptEnd;
    }

    public static int getApptCustomerID() {
        return apptCustomerID;
    }

    public static int getApptUserID() {
        return apptUserID;
    }

    public void setApptID(int apptID) {
        this.apptID = apptID;
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

    public void setApptStart(LocalDateTime apptStart) {
        this.apptStart = apptStart;
    }

    public void setApptEnd(LocalDateTime apptEnd) {
        this.apptEnd = apptEnd;
    }

    public void setApptCustomerID(int apptCustomerID) {
        this.apptCustomerID = apptCustomerID;
    }

    public void setApptUserID(int apptUserID) {
        this.apptUserID = apptUserID;
    }
}
