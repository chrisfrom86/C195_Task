package Appointment;

import Location.Location;
import Main.DB;
import Main.Login;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import static Location.LocDAO.*;

public class AddAppointment implements Initializable {
    public TextField apptIDTextField;
    public TextField apptTitleTextField;
    public TextField apptDescriptionTextField;
    public TextField apptTypeTextField;
    public TextField apptCustomerIDTextField;
    public TextField apptUserIDTextField;
    public DatePicker apptDatePicker;
    public ComboBox apptIDCustomerTextField;
    public ComboBox apptStartComboBox;
    public ComboBox apptEndComboBox;
    public Button apptSaveButton;
    public Button apptCancelButton;
    public Label errorLabel;
    public TextField apptLocationTextField;
    public ComboBox apptCountryComboBox;
    public ComboBox apptCountryDivisionComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> customerList = FXCollections.observableArrayList("Customer 1");
        apptIDCustomerTextField.setItems(customerList);

        try {
            apptCountryComboBox.setItems(getAllCountries());
        } catch (Exception e) {
            e.printStackTrace();
        }
        apptCountryDivisionComboBox.setVisibleRowCount(10);

        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(22, 0);
        while (start.isBefore(end.plusMinutes(-14))) {
            apptStartComboBox.getItems().add(start);
            start = start.plusMinutes(15);
            apptEndComboBox.getItems().add(start);
        }
    }

    /*
    public void onApptSaveButton(ActionEvent event) {

         // let mysql handle new IDs... just get from java side when needed
        int x = 0;

        for (Appointment appt : AppointmentView.allAppointments)
            if (appt == null) {}
            else if (Appointment.getApptID() > x)
                x = Appointment.getApptID();
        x += 1;
        int id = x;



        errorLabel.setText("");
        boolean createAppt = true;

        String title = apptTitleTextField.getText();
        if (title == null || apptTitleTextField.getText().trim().isEmpty()) {
            errorLabel.setText(errorLabel.getText() + "Enter a title.\n");
            createAppt = false;
        }

        String description = apptDescriptionTextField.getText();
        if (description == null || apptDescriptionTextField.getText().trim().isEmpty()) {
            errorLabel.setText(errorLabel.getText() + "Enter a description.\n");
            createAppt = false;
        }

        String location = apptLocationTextField.getText();
        if (location == null || apptLocationTextField.getText().trim().isEmpty()) {
            errorLabel.setText(errorLabel.getText() + "Enter a location.\n");
            createAppt = false;
        }

        //add Customer validation here
        String contact = "Customer";

        String type = apptTypeTextField.getText();
        if (type == null || apptTypeTextField.getText().trim().isEmpty()) {
            errorLabel.setText(errorLabel.getText() + "Enter a type.\n");
            createAppt = false;
        }

        LocalDateTime apptStart = LocalDateTime.of(2020, 01, 30, 8,00);
        LocalDateTime apptEnd = LocalDateTime.of(2020, 01, 30, 12,00);

        //Need to make sure date is not in the past
        LocalDate date = null;
        try {
            date = apptStartDatePicker.getValue();
        } catch (DateTimeException e) {
            errorLabel.setText(errorLabel.getText() + "DateTime Exception.\n");
            createAppt = false;
        } catch (Exception e) {
            System.out.println("LocalDate exception: " + e);
            errorLabel.setText(errorLabel.getText() + "DateTime Exception.\n");
            createAppt = false;
        }

        int startHour = 12; //initialize start hour
        try {
            int apptStartHour = startHour; // ensure start hour is between 08-21
        } catch (NumberFormatException e) {
            errorLabel.setText(errorLabel.getText() + "Min must be a number.\n");
            createAppt = false;
        } catch (Exception e) {
            System.out.println("min exception: " + e);
            errorLabel.setText(errorLabel.getText() + "Min must be a number.\n");
            createAppt = false;
        }

        int startMin = 00; //initialize start minute
        try {
            int apptStartMin = startMin; // ensure start minute is 00, 15, 30, or 45
        } catch (NumberFormatException e) {
            errorLabel.setText(errorLabel.getText() + "Min must be a number.\n");
            createAppt = false;
        } catch (Exception e) {
            System.out.println("min exception: " + e);
            errorLabel.setText(errorLabel.getText() + "Min must be a number.\n");
            createAppt = false;
        }

        int endHour = 12; //initialize end hour
        try {
            endHour = Integer.parseInt((String) apptEndHourComboBox.getSelectionModel().getSelectedItem()); // ensure start hour is between 08-21
        } catch (NumberFormatException e) {
            errorLabel.setText(errorLabel.getText() + "Appt end Hour must be a number.\n");
            createAppt = false;
        } catch (Exception e) {
            System.out.println("min exception: " + e);
            errorLabel.setText(errorLabel.getText() + "Appt end Hour must be a number.\n");
            createAppt = false;
        }

        int endMin = 00; //initialize end minute
        try {
            endMin = Integer.parseInt((String) apptEndMinComboBox.getSelectionModel().getSelectedItem()); // ensure start minute is 00, 15, 30, or 45
        } catch (NumberFormatException e) {
            errorLabel.setText(errorLabel.getText() + "Appt end Min must be a number.\n");
            createAppt = false;
        } catch (Exception e) {
            System.out.println("min exception: " + e);
            errorLabel.setText(errorLabel.getText() + "Appt end Min must be a number.\n");
            createAppt = false;
        }

        if (startHour > endHour || startHour == -1 || endHour == -2) {
            errorLabel.setText(errorLabel.getText() + "Start hour must be earlier than end.\n");
            createAppt = false;
        }
        else if (startHour == endHour) {
            if (startMin >= endMin) {
                errorLabel.setText(errorLabel.getText() + "Start minutes must be earlier than end.");
            }
        }
        int customerID = -1;
        try {
            customerID = Integer.parseInt(apptCustomerIDTextField.getText());
        } catch (NumberFormatException e) {
            errorLabel.setText(errorLabel.getText() + "Customer ID must be a number.\n");
            createAppt = false;
        } catch (Exception e) {
            System.out.println("custID exception: " + e);
            errorLabel.setText(errorLabel.getText() + "Customer ID must be a number.\n");
            createAppt = false;
        }

        int userID = -1;
        try {
            userID = Integer.parseInt(apptUserIDTextField.getText());
        } catch (NumberFormatException e) {
            errorLabel.setText(errorLabel.getText() + "User ID must be a number.\n");
            createAppt = false;
        } catch (Exception e) {
            System.out.println("UserID exception: " + e);
            errorLabel.setText(errorLabel.getText() + "User ID must be a number.\n");
            createAppt = false;
        }


        if (createAppt) {
            System.out.println("New Appointment saved");
            Appointment appointment = new Appointment(id, title, description, location, contact, type, apptStart, apptEnd, customerID, userID);
            AppointmentView.allAppointments.add(appointment);
            Stage stage = (Stage) apptSaveButton.getScene().getWindow();
            stage.close();
        }
        //Appointment newAppt = new Appointment();


    }

         */

    public void onApptSaveButton() throws Exception {
        DB.getConnection();
        String sql = "INSERT INTO appointments(title, description, location, type, start, end, create_date, created_by, last_update, last_updated_by, customer_id, user_id, contact_id)" +
                " VALUES(?,?,?,?,?,?,NOW(),?,NOW(),?,?,?,?);";
        PreparedStatement pst = DB.getConnection().prepareStatement(sql);
        pst.setString(1, apptTitleTextField.getText());
            System.out.println(apptTitleTextField.getText());
        pst.setString(2, apptDescriptionTextField.getText());
            System.out.println(apptDescriptionTextField.getText());

        Location loc = (Location) apptCountryDivisionComboBox.getSelectionModel().getSelectedItem();
        pst.setString(3, loc.getDivision());
            System.out.println(loc.getDivision());
        pst.setString(4, apptTypeTextField.getText());
            System.out.println(apptTypeTextField.getText());

        LocalDate apptDate = apptDatePicker.getValue();
        LocalTime startTime = (LocalTime) apptStartComboBox.getValue();
        Timestamp startTimestamp = Timestamp.valueOf(apptDate.atTime(startTime));
            System.out.println(startTimestamp);
        pst.setTimestamp(5, (startTimestamp));

        LocalTime endTime = (LocalTime) apptEndComboBox.getValue();
        Timestamp endTimestamp = Timestamp.valueOf(apptDate.atTime(endTime));
            System.out.println(endTimestamp);
        pst.setTimestamp(6, (endTimestamp));

        pst.setString(7, "java");
            System.out.println("java");
        pst.setString(8, "java");
            System.out.println("java");
        pst.setInt(9, 1);
            System.out.println("1");
        pst.setInt(10, 2);
            System.out.println("2");
        pst.setInt(11, 3);
            System.out.println("3");
        System.out.println(pst);
        pst.executeUpdate();
        errorLabel.setText("New appointment added");
        Login.showApptView();
        Stage stage = (Stage) apptSaveButton.getScene().getWindow();
        stage.close();
    }

    public void onApptCancelButton(ActionEvent event) throws IOException {
        System.out.println("Add Appt Cancel button clicked");
        Login.showApptView();
        Stage stage = (Stage) apptCancelButton.getScene().getWindow();
        stage.close();
    }


    public void onApptCountryComboBox(ActionEvent event) throws Exception {
        Location loc = (Location) apptCountryComboBox.getSelectionModel().getSelectedItem();
        System.out.println(loc);
        try {
           apptCountryDivisionComboBox.getItems().setAll(getCountryDivisions(loc.getCountryID()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
