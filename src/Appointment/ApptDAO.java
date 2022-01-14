package Appointment;

import Main.DB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ApptDAO {

    public static ObservableList<Appointment> getAllAppointments() throws Exception {
        String sql = "select * from appointments";
        ObservableList<Appointment> apptList = FXCollections.observableArrayList();
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        System.out.println("result set = " + rs);

        try {
            System.out.println("Select * from appointments command issued to db");
            while (rs.next()) {
                Appointment appt = new Appointment();
                appt.setApptID(rs.getInt("appointment_id"));
                appt.setApptTitle(rs.getString(2));
                appt.setApptDescription(rs.getString(3));
                appt.setApptLocation(rs.getString(4));
                appt.setApptContact("from contact db");
                appt.setApptType(rs.getString(5));
                appt.setApptStart(rs.getTimestamp("start"));
                appt.setApptEnd(rs.getTimestamp("End"));
                appt.setApptCustomerID(rs.getInt("customer_id"));
                appt.setApptUserID(rs.getInt("user_id"));
                apptList.add(appt);
                //System.out.println(rs.getInt(1) + " added to " + apptList);
            }
            //apptList = getApptObjects(rs);
            System.out.println("Appointment objects added to ObservableList<Appointment>");
        } catch (SQLException e) {
            System.out.println("Error querying all appointments");
            e.printStackTrace();
        }
        return apptList;

    }
}
