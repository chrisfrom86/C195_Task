package Appointment;

import Contact.Contact;
import Customer.Customer;
import Location.Country;
import Location.CountryDivision;
import Login.Login;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;
import java.util.TimeZone;

import static Appointment.AppointmentView.selectedAppointment;
import static Appointment.ApptDAO.*;
import static Contact.ContactDAO.getAllContacts;
import static Contact.ContactDAO.getContactByID;
import static Customer.CustomerDAO.getAllCustomers;
import static Location.LocDAO.*;
import static Login.Login.loggedInUserID;

public class ModifyAppointment implements Initializable {
    public TextField apptIDTextField;
    public TextField apptTitleTextField;
    public TextField apptDescriptionTextField;
    public ComboBox apptTypeComboBox;
    public TextField apptUserIDTextField;
    public ComboBox apptStartComboBox;
    public ComboBox apptEndComboBox;
    public Button apptSaveButton;
    public Button apptCancelButton;
    public Label errorLabel;
    public ComboBox apptCustomerComboBox;
    public ComboBox apptCountryComboBox;
    public ComboBox apptCountryDivisionComboBox;
    public DatePicker apptDatePicker;
    public ComboBox apptContactComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("ModifyAppointment - initialized");

        try { apptCustomerComboBox.setItems(getAllCustomers());
        } catch (Exception e) { e.printStackTrace(); }
        try { apptCountryComboBox.setItems(getAllCountries());
        } catch (Exception e) { e.printStackTrace(); }
        try { apptContactComboBox.setItems(getAllContacts());
        } catch (Exception e) { e.printStackTrace(); }
        apptCountryDivisionComboBox.setVisibleRowCount(10);
        populateTimeComboBoxes();
        populateTypeComboBox();

        Appointment sAppt = selectedAppointment;

        apptIDTextField.setText(String.valueOf(sAppt.getApptID()));
        apptTitleTextField.setText(sAppt.apptTitle);
        apptDescriptionTextField.setText(sAppt.apptDescription);

        try { apptContactComboBox.getSelectionModel().select(getContactByID(sAppt.getApptContactID()));
        } catch (SQLException throwables) { throwables.printStackTrace(); }

        apptTypeComboBox.getSelectionModel().select(sAppt.apptType);
        apptCountryComboBox.setPromptText("Select country");
        apptCountryComboBox.getSelectionModel().select(sAppt.getApptCountry());

        try { apptCountryDivisionComboBox.getItems().setAll(getAllDivisionsByCountryID(sAppt.getApptCountryID()));
        } catch (Exception e) { e.printStackTrace(); }

        apptCountryDivisionComboBox.setPromptText("Select location");
        apptCountryDivisionComboBox.getSelectionModel().select(sAppt.getApptCountryDivision());
        apptDatePicker.valueProperty().setValue(sAppt.apptStart.toLocalDateTime().toLocalDate());
        LocalTime start = sAppt.apptStart.toLocalDateTime().toLocalTime();
        apptStartComboBox.getSelectionModel().select(start);
        LocalTime end = LocalTime.of(22, 0);
        apptEndComboBox.getItems().clear();
        while (start.isBefore(end.plusMinutes(-14))) {
            start = start.plusMinutes(15);
            apptEndComboBox.getItems().add(start);
        }
        apptEndComboBox.getSelectionModel().select(sAppt.apptEnd.toLocalDateTime().toLocalTime());
        apptCustomerComboBox.setValue(sAppt.apptCustomer);
        apptUserIDTextField.setText(String.valueOf(sAppt.getApptUserID())); //user_id

        errorLabel.setText("NOTE: If no new date is selected, the original date will be saved when you click Save Appointment.");

        System.out.println("ModifyAppointment - sAppt           = " + sAppt);
        System.out.println("ModifyAppointment - apptID          = " + sAppt.apptID);
        System.out.println("ModifyAppointment - apptTitle       = " + sAppt.apptTitle);
        System.out.println("ModifyAppointment - apptDescription = " + sAppt.apptDescription);
        System.out.println("ModifyAppointment - apptContact     = " + sAppt.apptContactID);
        System.out.println("ModifyAppointment - apptType        = " + sAppt.apptType);
        System.out.println("ModifyAppointment - apptLocation    = " + sAppt.apptLocation);
        System.out.println("ModifyAppointment - apptCountry     = " + sAppt.apptCountry.getCountry());
        System.out.println("ModifyAppointment - apptCountryDiv  = " + sAppt.apptCountryDivision.getDivision());
        System.out.println("ModifyAppointment - apptCustomer    = " + sAppt.apptCustomer.getCustomerName());
        System.out.println("ModifyAppointment - apptCustomerID  = " + sAppt.apptCustomer.getCustomerID());
        System.out.println("ModifyAppointment - apptUserID      = " + sAppt.apptUserID.get());
    }

    public void populateTypeComboBox() {
        apptTypeComboBox.getItems().addAll("Work", "Personal");
    }

    public void populateTimeComboBoxes() {
        ZoneId eastern = ZoneId.of("America/New_York");  //eastern
        ZonedDateTime startZDT = ZonedDateTime.of(LocalDate.now(), LocalTime.of(8,0), eastern);
        ZonedDateTime endZDT = ZonedDateTime.of(LocalDate.now(), LocalTime.of(22,0), eastern);
        ZoneId localZoneId = TimeZone.getDefault().toZoneId(); //local
        ZonedDateTime localStartZDT = startZDT.withZoneSameInstant(localZoneId);
        ZonedDateTime localEndZDT = endZDT.withZoneSameInstant(localZoneId);

        while (localStartZDT.isBefore(localEndZDT.plusMinutes(-14))) {
            apptStartComboBox.getItems().add(localStartZDT.toLocalTime());
            localStartZDT = localStartZDT.plusMinutes(15);
            apptEndComboBox.getItems().add(localStartZDT.toLocalTime());
        }
    }

    public void onApptSaveButton() throws IOException, SQLException {
        int apptID = Integer.parseInt(apptIDTextField.getText());

        errorLabel.setText("");
        boolean addConfirm = true;

        String title = apptTitleTextField.getText();
        if (title == null || apptTitleTextField.getText().trim().isEmpty()) {
            errorLabel.setText(errorLabel.getText() + "Enter a title. ");
            addConfirm = false;
        }

        String description = apptDescriptionTextField.getText();
        if (description == null || apptDescriptionTextField.getText().trim().isEmpty()) {
            errorLabel.setText(errorLabel.getText() + "Enter a description. ");
            addConfirm = false;
        }

        Contact contact = (Contact) apptContactComboBox.getSelectionModel().getSelectedItem();
        int contactID = contact.contactID;

        String type = (String) apptTypeComboBox.getSelectionModel().getSelectedItem();
        if (type == null || apptTypeComboBox.getSelectionModel().getSelectedItem() == null) {
            errorLabel.setText(errorLabel.getText() + "Enter a type. ");
            addConfirm = false;
        }

        CountryDivision div = (CountryDivision) apptCountryDivisionComboBox.getSelectionModel().getSelectedItem();
        String location = div.getDivision();

        LocalDate apptDate = null;
        if (apptDatePicker.valueProperty().getValue() == null) {
            errorLabel.setText(errorLabel.getText() + "Select a date. ");
            addConfirm = false;
        } else {
            apptDate = apptDatePicker.getValue();
        }

        LocalTime startTime = (LocalTime) apptStartComboBox.getValue();
        ZonedDateTime startTimeZDT = null;
        ZoneId localZoneId = TimeZone.getDefault().toZoneId(); //local
        if (apptStartComboBox.getSelectionModel().isEmpty()) {
            errorLabel.setText(errorLabel.getText() + "Select a start time. ");
            addConfirm = false;
        } else {
            try {
                startTimeZDT = ZonedDateTime.of(apptDate, startTime, localZoneId);
            } catch (Exception e) {
            }
        }

        LocalTime endTime = (LocalTime) apptEndComboBox.getValue();
        ZonedDateTime endTimeZDT = null;
        if (apptEndComboBox.getSelectionModel().isEmpty()) {
            errorLabel.setText(errorLabel.getText() + "Select an end time. ");
            addConfirm = false;
        } else {
            try {
                endTimeZDT = ZonedDateTime.of(apptDate, endTime, localZoneId);
            } catch (Exception e) {
            }
        }

        Customer cust = (Customer) apptCustomerComboBox.getSelectionModel().getSelectedItem();
        int customerID = 0;
        if (apptCustomerComboBox.getSelectionModel().isEmpty()) {
            errorLabel.setText(errorLabel.getText() + "Select a customer. ");
            addConfirm = false;
        } else {
            customerID = cust.getCustomerID();
        }

        if (addConfirm) {
            if (!checkApptOverlapByCustomer(customerID, startTimeZDT, endTimeZDT)) {
                errorLabel.setText(errorLabel.getText() + "Selected time overlaps another appointment. ");
            }
        }

        if (addConfirm && checkApptOverlapByCustomer(customerID, startTimeZDT, endTimeZDT)) {
            updateAppointment(apptID, title, description, contactID, location, type, startTimeZDT, endTimeZDT, customerID, loggedInUserID);
            errorLabel.setText("Appointment ID " + apptID + " updated");
            Login.showApptView();
            Stage stage = (Stage) apptSaveButton.getScene().getWindow();
            stage.close();
        }
    }

    public void onApptCancelButton(ActionEvent event) throws IOException {
        System.out.println("ModifyAppointment - Modify Appt Cancel button clicked");
        Stage stage = (Stage) apptCancelButton.getScene().getWindow();
        stage.close();
        Login.showApptView();
    }

    public void onApptStartTimeComboBox(ActionEvent event) {
        populateTimeComboBoxes();

        LocalTime start = (LocalTime) apptStartComboBox.getSelectionModel().getSelectedItem();

        LocalTime end = LocalTime.of(22, 0);

        apptEndComboBox.getItems().clear();
        while (start.isBefore(end.plusMinutes(-14))) {
            start = start.plusMinutes(15);
            apptEndComboBox.getItems().add(start);
        }
    }

    public void onApptCountryComboBox(ActionEvent event) {
        Country loc = (Country) apptCountryComboBox.getSelectionModel().getSelectedItem();
        System.out.println(loc);
        try {
            apptCountryDivisionComboBox.getItems().setAll(getAllDivisionsByCountryID(loc.getCountryID()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
