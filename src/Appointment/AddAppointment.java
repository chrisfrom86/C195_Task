package Appointment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Date;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AddAppointment {
    public TextField apptIDTextField;
    public TextField apptTitleTextField;
    public TextField apptDescriptionTextField;
    public TextField apptTypeTextField;
    public TextField apptCustomerIDTextField;
    public TextField apptUserIDTextField;
    public DatePicker apptStartDatePicker;
    public ComboBox apptIDCustomerTextField;
    public ComboBox apptStartHourComboBox;
    public ComboBox apptStartMinComboBox;
    public ComboBox apptEndHourComboBox;
    public ComboBox apptEndMinComboBox;
    public Button apptSaveButton;
    public Button apptCancelButton;
    public Label errorLabel;
    public TextField apptLocationTextField;

    public void start() {
        ObservableList<String> customerList = FXCollections.observableArrayList("Customer 1");
        apptIDCustomerTextField.setItems(customerList);
        apptStartHourComboBox.getItems().addAll("08", "09", "10", "11", "12", "13");
        apptStartMinComboBox.getItems().addAll("00", "15", "30", "45");
        apptEndHourComboBox.getItems().addAll("08", "09", "10", "11", "12");
        apptEndMinComboBox.getItems().addAll("00", "15", "30", "45");
    }

    public void onApptSaveButton(ActionEvent event) {

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

    public void onApptCancelButton(ActionEvent event) {
        System.out.println("Add Appt Cancel button clicked");
        Stage stage = (Stage) apptCancelButton.getScene().getWindow();
        stage.close();
    }
}
