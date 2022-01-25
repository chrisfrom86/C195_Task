package Appointment;

import Contact.Contact;
import Main.DB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.*;
import java.util.TimeZone;

import static Contact.ContactDAO.getContactIDByName;
import static Login.LoginDAO.getUsernameByID;

/**
 * @author Chris Sequeira
 * this data access object queries and interacts with the appointments table of the database.
 */
public class ApptDAO {

    /**
     * this method returns all entries from the appointments table.
     * @return all appointments are returned as Appointment objects in an ObservableList.
     * @throws Exception
     */
    public static ObservableList<Appointment> getAllAppointments() throws Exception {
        String sql = "select * from appointments";
        ObservableList<Appointment> apptList = FXCollections.observableArrayList();
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        //System.out.println("result set = " + rs);

        try {
            System.out.println("ApptDAO - Select * from appointments command issued to db");
            while (rs.next()) {
                Appointment appt = new Appointment();
                appt.setApptID(rs.getInt("appointment_id"));
                appt.setApptTitle(rs.getString(2));
                appt.setApptDescription(rs.getString(3));
                appt.setApptLocation(rs.getString(4));
                appt.setApptContact(rs.getInt("contact_id"));
                appt.setApptType(rs.getString(5));

                Timestamp startTS = (rs.getTimestamp("start"));
                ZoneId gmtZoneId = ZoneId.of("Europe/London"); //gmt
                ZoneId localZoneId = TimeZone.getDefault().toZoneId(); //local
                ZonedDateTime apptStartFromDB = ZonedDateTime.of(startTS.toLocalDateTime().toLocalDate(), startTS.toLocalDateTime().toLocalTime(), gmtZoneId);
                ZonedDateTime apptStartLocal = apptStartFromDB.withZoneSameInstant(localZoneId);
                appt.setApptStart(apptStartLocal);

                Timestamp endTS = (rs.getTimestamp("end"));
                ZonedDateTime apptEndFromDB = ZonedDateTime.of(endTS.toLocalDateTime().toLocalDate(), endTS.toLocalDateTime().toLocalTime(), gmtZoneId);
                ZonedDateTime apptEndLocal = apptEndFromDB.withZoneSameInstant(localZoneId);
                appt.setApptEnd(apptEndLocal);

                appt.setApptCustomerID(rs.getInt("customer_id"));
                appt.setApptUserID(rs.getInt("user_id"));
                apptList.add(appt);
                //System.out.println(rs.getInt(1) + " added to " + apptList);
            }
            //apptList = getApptObjects(rs);
            System.out.println("ApptDAO - Appointment objects added to ObservableList<Appointment>");
        } catch (SQLException e) {
            System.out.println("ApptDAO - Error querying all appointments");
            e.printStackTrace();
        }
        return apptList;
    }

    /**
     * this method gets all appointments with a start or end date within 15 minutes of login.
     *
     * The time is converted from system local time to gmt before sending the query to the database.
     *
     * this method is called when the {@link ApptWarning} is called.
     * @return an ObservableList of Appointment objects is returned.
     * @throws SQLException
     */
    public static ObservableList<Appointment> get15MinUpcomingAppts() throws SQLException {
        DB.getConnection();
        ZoneId gmtZoneId = ZoneId.of("Europe/London"); //gmt

        ObservableList<Appointment> soonAppts = FXCollections.observableArrayList();
        String sql = "select * from appointments where start between ? and date_sub(?, interval -15 minute);";
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ZonedDateTime nowLocalZDT = ZonedDateTime.now();
        ZonedDateTime nowGMTZDT = nowLocalZDT.withZoneSameInstant(gmtZoneId);
        Timestamp nowLocalTS = Timestamp.valueOf(nowGMTZDT.toLocalDateTime());
        ps.setString(1, nowLocalTS.toString());
        ps.setString(2, nowLocalTS.toString());
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Appointment appt = new Appointment();
            appt.setApptID(rs.getInt("appointment_id"));
            Timestamp startTS = (rs.getTimestamp("start"));
            ZoneId localZoneId = TimeZone.getDefault().toZoneId(); //local
            ZonedDateTime apptStartFromDB = ZonedDateTime.of(startTS.toLocalDateTime().toLocalDate(), startTS.toLocalDateTime().toLocalTime(), gmtZoneId);
            ZonedDateTime apptStartLocal = apptStartFromDB.withZoneSameInstant(localZoneId);
            appt.setApptStart(apptStartLocal);

            soonAppts.add(appt);
        }
        return soonAppts;
    }

    /**
     * this method gets all appointments within 1 month of the current local time.
     *
     * this method is called when the radio button is selected on {@link AppointmentView}.
     * @return an ObservableList of Appointments is returned to populate the TableView in {@link AppointmentView}.
     * @throws SQLException
     */
    public static ObservableList<Appointment> get1MonthUpcomingAppts() throws SQLException {
        DB.getConnection();
        ObservableList<Appointment> soonAppts = FXCollections.observableArrayList();
        String sql = "select * from appointments where start between now() and date_sub(now(), interval -1 month);";
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Appointment appt = new Appointment();
            appt.setApptID(rs.getInt("appointment_id"));
            appt.setApptTitle(rs.getString(2));
            appt.setApptDescription(rs.getString(3));
            appt.setApptLocation(rs.getString(4));
            appt.setApptContact(rs.getInt("contact_id"));
            appt.setApptType(rs.getString(5));
            appt.setApptID(rs.getInt("appointment_id"));

            Timestamp startTS = (rs.getTimestamp("start"));
            ZoneId gmtZoneId = ZoneId.of("Europe/London"); //gmt
            ZoneId localZoneId = TimeZone.getDefault().toZoneId(); //local
            ZonedDateTime apptStartFromDB = ZonedDateTime.of(startTS.toLocalDateTime().toLocalDate(), startTS.toLocalDateTime().toLocalTime(), gmtZoneId);
            ZonedDateTime apptStartLocal = apptStartFromDB.withZoneSameInstant(localZoneId);
            appt.setApptStart(apptStartLocal);

            Timestamp endTS = (rs.getTimestamp("end"));
            ZonedDateTime apptEndFromDB = ZonedDateTime.of(endTS.toLocalDateTime().toLocalDate(), endTS.toLocalDateTime().toLocalTime(), gmtZoneId);
            ZonedDateTime apptEndLocal = apptEndFromDB.withZoneSameInstant(localZoneId);
            appt.setApptEnd(apptEndLocal);

            appt.setApptCustomerID(rs.getInt("customer_id"));
            appt.setApptUserID(rs.getInt("user_id"));
            soonAppts.add(appt);
        }
        return soonAppts;
    }

    /**
     * this method gets all appointments within 1 week of the current local time.
     *
     * this method is called when the radio button is selected on {@link AppointmentView}.
     * @return an ObservableList of Appointments is returned to populate the TableView in {@link AppointmentView}.
     * @throws SQLException
     */
    public static ObservableList<Appointment> get1WeekUpcomingAppts() throws SQLException {
        DB.getConnection();
        ObservableList<Appointment> soonAppts = FXCollections.observableArrayList();
        String sql = "select * from appointments where start between now() and date_sub(now(), interval -1 week);";
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Appointment appt = new Appointment();
            appt.setApptID(rs.getInt("appointment_id"));
            appt.setApptTitle(rs.getString(2));
            appt.setApptDescription(rs.getString(3));
            appt.setApptLocation(rs.getString(4));
            appt.setApptContact(rs.getInt("contact_id"));
            appt.setApptType(rs.getString(5));

            Timestamp startTS = (rs.getTimestamp("start"));
            ZoneId gmtZoneId = ZoneId.of("Europe/London"); //gmt
            ZoneId localZoneId = TimeZone.getDefault().toZoneId(); //local
            ZonedDateTime apptStartFromDB = ZonedDateTime.of(startTS.toLocalDateTime().toLocalDate(), startTS.toLocalDateTime().toLocalTime(), gmtZoneId);
            ZonedDateTime apptStartLocal = apptStartFromDB.withZoneSameInstant(localZoneId);
            appt.setApptStart(apptStartLocal);

            Timestamp endTS = (rs.getTimestamp("end"));
            ZonedDateTime apptEndFromDB = ZonedDateTime.of(endTS.toLocalDateTime().toLocalDate(), endTS.toLocalDateTime().toLocalTime(), gmtZoneId);
            ZonedDateTime apptEndLocal = apptEndFromDB.withZoneSameInstant(localZoneId);
            appt.setApptEnd(apptEndLocal);

            appt.setApptCustomerID(rs.getInt("customer_id"));
            appt.setApptUserID(rs.getInt("user_id"));
            soonAppts.add(appt);
        }
        return soonAppts;
    }

    /**
     * this method gets all appointments based on a Customer selected from a ComboBox in {@link AppointmentView}.
     *
     * {@param name} is provided from the ComboBox and passed into a PreparedStatement before querying the database.
     * @return an ObservableList of Appointments is returned to populate the TableView in {@link AppointmentView}.
     * @throws SQLException
     */
    public static ObservableList<Appointment> getApptsByCustomer(String name) throws SQLException {
        DB.getConnection();
        ObservableList<Appointment> custAppts = FXCollections.observableArrayList();
        String sql = "select * from appointments, customers where (customers.customer_name = ?) and (customers.customer_id = appointments.customer_id);";
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ps.setString(1, name);
        System.out.println(ps);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Appointment appt = new Appointment();
            appt.setApptID(rs.getInt("appointment_id"));
            appt.setApptTitle(rs.getString("title"));
            appt.setApptDescription(rs.getString("description"));
            appt.setApptLocation(rs.getString("location"));
            appt.setApptContact(rs.getInt("contact_id"));
            appt.setApptType(rs.getString("type"));

            Timestamp startTS = (rs.getTimestamp("start"));
            ZoneId gmtZoneId = ZoneId.of("Europe/London"); //gmt
            ZoneId localZoneId = TimeZone.getDefault().toZoneId(); //local
            ZonedDateTime apptStartFromDB = ZonedDateTime.of(startTS.toLocalDateTime().toLocalDate(), startTS.toLocalDateTime().toLocalTime(), gmtZoneId);
            ZonedDateTime apptStartLocal = apptStartFromDB.withZoneSameInstant(localZoneId);
            appt.setApptStart(apptStartLocal);

            Timestamp endTS = (rs.getTimestamp("end"));
            ZonedDateTime apptEndFromDB = ZonedDateTime.of(endTS.toLocalDateTime().toLocalDate(), endTS.toLocalDateTime().toLocalTime(), gmtZoneId);
            ZonedDateTime apptEndLocal = apptEndFromDB.withZoneSameInstant(localZoneId);
            appt.setApptEnd(apptEndLocal);

            appt.setApptCustomerID(rs.getInt("customer_id"));
            appt.setApptUserID(rs.getInt("user_id"));
            custAppts.add(appt);
        }
        return custAppts;
    }

    /**
     * this method queries the appointments table with a {@param start} and {@param end} time, and if either are within a current appointment's time, returns false.
     *
     * @return boolean is true if there are no overlapping appointments found, false if any overlapping appointments are found.
     * @throws SQLException
     */
    public static boolean checkApptOverlapByCustomer(ZonedDateTime start, ZonedDateTime end) throws SQLException {
        boolean apptsOverlap = true;

        DB.getConnection();
        String sql = "select * from appointments where ((? between start and end) or (? between start and end));";
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ZoneId gmtZoneId = ZoneId.of("Europe/London"); //gmt
        ZonedDateTime startZDT = start.withZoneSameInstant(gmtZoneId);
        Timestamp startTS = Timestamp.valueOf(startZDT.toLocalDateTime());
        ps.setTimestamp(1, startTS);
        ZonedDateTime endZDT = end.withZoneSameInstant(gmtZoneId);
        Timestamp endTS = Timestamp.valueOf(endZDT.toLocalDateTime());
        ps.setTimestamp(2, endTS);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            apptsOverlap = false;
        }

        return apptsOverlap;
    }

    /**
     * this method queries the appointments table with a {@param start} and {@param end} time, and if either are within a current appointment's time, returns false.
     *
     * This method also has {@param id} because it is called in {@link ModifyAppointment} based on a given existing Appointment.
     *
     * @return boolean is true if there are no overlapping appointments found, false if any overlapping appointments are found.
     * @throws SQLException
     */
    public static boolean checkApptOverlapByCustomer(int id, ZonedDateTime start, ZonedDateTime end) throws SQLException {
        boolean apptsOverlap = true;

        DB.getConnection();
        String sql = "select * from appointments where not appointment_id = ? and ((? between start and end) or (? between start and end));";
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ps.setInt(1, id);
        ZoneId gmtZoneId = ZoneId.of("Europe/London"); //gmt
        start.withZoneSameInstant(gmtZoneId);
        Timestamp startTS = Timestamp.valueOf(start.toLocalDateTime());
        ps.setTimestamp(2, startTS);
        end.withZoneSameInstant(gmtZoneId);
        Timestamp endTS = Timestamp.valueOf(end.toLocalDateTime());
        ps.setTimestamp(3, endTS);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            apptsOverlap = false;
        }

        return apptsOverlap;
    }

    /**
     * This method accepts validated inputs from {@link AddAppointment} and inserts them into the appointments table.
     *
     * A new appointment_ID is generated in the database based on an AutoIncrement column.
     *
     * @param title
     * @param description
     * @param contactID
     * @param loc
     * @param type
     * @param start
     * @param end
     * @param customerID
     * @param userID
     * @throws SQLException
     */
    public static void addAppointment(String title, String description, int contactID, String loc, String type, ZonedDateTime start, ZonedDateTime end, int customerID, int userID) throws SQLException {
        DB.getConnection();
        String sql = "INSERT INTO appointments(title, description, location, type, start, end, create_date, created_by, last_update, last_updated_by, customer_id, user_id, contact_id)" +
                " VALUES(?,?,?,?,?,?,NOW(),?,NOW(),?,?,?,?);";
        PreparedStatement pst = DB.getConnection().prepareStatement(sql);
        pst.setString(1, title); //title
        pst.setString(2, description); //description
        pst.setString(3, loc); //location
        pst.setString(4, type); //type
        ZoneId gmtZoneId = ZoneId.of("Europe/London"); //gmt
        ZonedDateTime startZDT = start.withZoneSameInstant(gmtZoneId);
        Timestamp startTS = Timestamp.valueOf(startZDT.toLocalDateTime());
        pst.setTimestamp(5, startTS); //start
        ZonedDateTime endZDT = end.withZoneSameInstant(gmtZoneId);
        Timestamp endTS = Timestamp.valueOf(endZDT.toLocalDateTime());
        pst.setTimestamp(6, endTS); //end
        pst.setString(7, getUsernameByID(userID)); //created_by
        pst.setString(8, getUsernameByID(userID)); //last_updated_by
        pst.setInt(9, customerID); //customer_id
        pst.setInt(10, userID); //user_id
        pst.setInt(11, contactID); //contact_id
        System.out.println(pst);
        pst.executeUpdate();
    }

    /**
     * This method accepts validated inputs from {@link ModifyAppointment} and updates an entry in the appointments table.
     *
     * The appointment updated is based on the appointment_id passed as {@param apptID}.
     *
     * @param title
     * @param description
     * @param contactID
     * @param loc
     * @param type
     * @param start
     * @param end
     * @param customerID
     * @param userID
     * @throws SQLException
     */
    public static void updateAppointment(int apptID, String title, String description, int contactID, String loc, String type, ZonedDateTime start, ZonedDateTime end, int customerID, int userID) throws SQLException {
        DB.getConnection();
        String sql = "update appointments set title = ?, description = ?, location = ?, type = ?, start = ?, end = ?, last_update = NOW(), last_updated_by = ?, customer_id = ?, user_id = ?, contact_id = ?" +
                " where appointment_id = ?";
        PreparedStatement pst = DB.getConnection().prepareStatement(sql);
        pst.setString(1, title); //title
        pst.setString(2, description); //description
        pst.setString(3, loc); //location
        pst.setString(4, type); //type

        ZoneId localZoneId = TimeZone.getDefault().toZoneId(); //local
        ZoneId gmtZoneId = ZoneId.of("Europe/London"); //gmt

        ZonedDateTime apptStartZDT = ZonedDateTime.of(start.toLocalDateTime().toLocalDate(), start.toLocalDateTime().toLocalTime(), localZoneId);
        ZonedDateTime apptStartToGMT = apptStartZDT.withZoneSameInstant(gmtZoneId);
        Timestamp startTS = Timestamp.valueOf(apptStartToGMT.toLocalDateTime());
        pst.setTimestamp(5, startTS); //start

        ZonedDateTime apptEndZDT = ZonedDateTime.of(end.toLocalDateTime().toLocalDate(), end.toLocalDateTime().toLocalTime(), localZoneId);
        ZonedDateTime apptEndToGMT = apptEndZDT.withZoneSameInstant(gmtZoneId);
        Timestamp endTS = Timestamp.valueOf(apptEndToGMT.toLocalDateTime());
        pst.setTimestamp(6, endTS); //end

        pst.setString(7, getUsernameByID(userID)); //last_updated_by
        pst.setInt(8, customerID); //customer_id
        pst.setInt(9, userID); //user_id
        pst.setInt(10, contactID); //contact_id
        pst.setInt(11, apptID);
        System.out.println(pst);
        pst.executeUpdate();
    }

    /**
     * this method queries the database for an appointment with the given {@param id} and deletes it.
     * @param id
     * @return if successful, a confirmation message is returned; if failed, an error message is returned.
     * @throws SQLException
     */
    public static String deleteAppointment(int id) throws SQLException {
        DB.getConnection();
        try {
            String sql = "delete from appointments where appointment_id = ?";
            PreparedStatement ps = DB.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return "Appointment ID " + id + " deleted";
        } catch (SQLIntegrityConstraintViolationException e){
            return "Delete customer's appointments first";
        }
    }

    /**
     * This is an overloaded method for generating a report.
     *
     * This method returns all appointments when no selections are made.
     * @return Integer equal to the number of appointments found in the query.
     * @throws SQLException
     */
    public static int getApptReport() throws SQLException {
        String sql = "select * from appointments";
        int apptReport = 0;

        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            apptReport += 1;
        }
        return apptReport;
    }

    /**
     * This is an overloaded method for generating a report.
     *
     * This method returns all appointments based on a given {@param month}.
     * @return Integer equal to the number of appointments found in the query.
     * @throws SQLException
     */
    public static int getApptReport(int month) throws SQLException {
        String sql = "select * from appointments where month(start) = ?";
        int apptReport = 0;

        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ps.setInt(1, month);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            apptReport += 1;
        }
        return apptReport;
    }

    /**
     * This is an overloaded method for generating a report.
     *
     * This method returns all appointments based on a given {@param type}.
     * @return Integer equal to the number of appointments found in the query.
     * @throws SQLException
     */
    public static int getApptReport(String type) throws SQLException {
        String sql = "select * from appointments where type = ?";
        int apptReport = 0;

        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ps.setString(1, type);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            apptReport += 1;
        }
        return apptReport;
    }

    /**
     * This is an overloaded method for generating a report.
     *
     * This method returns all appointments based on a given {@param contact}.
     * @return Integer equal to the number of appointments found in the query.
     * @throws SQLException
     */
    public static int getApptReport(Contact contact) throws SQLException {
        String sql = "select * from appointments where contact_id = ?";
        int apptReport = 0;

        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        String contactName = contact.contactName;
        ps.setInt(1, getContactIDByName(contactName));
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            apptReport += 1;
        }
        return apptReport;
    }

    /**
     * This is an overloaded method for generating a report.
     *
     * This method returns all appointments based on a given {@param month} and {@param type}.
     * @return Integer equal to the number of appointments found in the query.
     * @throws SQLException
     */
    public static int getApptReport(int month, String type) throws SQLException {
        String sql = "select * from appointments where month(start) = ? and type = ?";
        int apptReport = 0;

        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ps.setInt(1, month);
        ps.setString(2, type);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            apptReport += 1;
        }
        return apptReport;
    }

    /**
     * This is an overloaded method for generating a report.
     *
     * This method returns all appointments based on a given {@param type} and {@param contact}.
     * @return Integer equal to the number of appointments found in the query.
     * @throws SQLException
     */
    public static int getApptReport(String type, Contact contact) throws SQLException {
        String sql = "select * from appointments where type = ? and contact_id = ?";
        int apptReport = 0;

        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ps.setString(1, type);
        String contactName = contact.contactName;
        ps.setInt(2, getContactIDByName(contactName));
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            apptReport += 1;
        }
        return apptReport;
    }

    /**
     * This is an overloaded method for generating a report.
     *
     * This method returns all appointments based on a given {@param month} and {@param contact}.
     * @return Integer equal to the number of appointments found in the query.
     * @throws SQLException
     */
    public static int getApptReport(int month, Contact contact) throws SQLException {
        String sql = "select * from appointments where month(start) = ? and contact_id = ?";
        int apptReport = 0;

        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ps.setInt(1, month);
        String contactName = contact.contactName;
        ps.setInt(2, getContactIDByName(contactName));
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            apptReport += 1;
        }
        return apptReport;
    }

    /**
     * This is an overloaded method for generating a report.
     *
     * This method returns all appointments based on a given {@param month}, {@param type}, and {@param contact}.
     * @return Integer equal to the number of appointments found in the query.
     * @throws SQLException
     */
    public static int getApptReport(int month, String type, Contact contact) throws SQLException {
        String sql = "select * from appointments where month(start) = ? and type = ? and contact_id = ?";
        int apptReport = 0;

        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ps.setInt(1, month);
        ps.setString(2, type);
        String contactName = contact.contactName;
        ps.setInt(3, getContactIDByName(contactName));
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            apptReport += 1;
        }
        return apptReport;
    }

}
