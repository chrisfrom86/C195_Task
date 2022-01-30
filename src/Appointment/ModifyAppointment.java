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
import java.time.*;
import java.util.ResourceBundle;
import java.util.TimeZone;

import static Appointment.AppointmentView.selectedAppointment;
import static Appointment.ApptDAO.*;
import static Contact.ContactDAO.getAllContacts;
import static Contact.ContactDAO.getContactByID;
import static Customer.CustomerDAO.getAllCustomers;
import static Location.LocDAO.*;
import static Login.Login.loggedInUserID;

/**
 * @author Chris Sequeira
 *
 * this class controls the ModifyAppointment.fxml form.
 *
 * It allows users to update appointments based on validated inputs that will be passed to {@link ApptDAO}.
 */
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

    /**
     * this method populates the Customer, Country, and Contact ComboBoxes with all relevant items as an ObservableList.
     *
     * the country division (first level division) ComboBox is limited to show 10 items at a time.
     *
     * The appointment type ComboBox is populated with the options Work and Personal.
     *
     * The selected appointment is passed from {@link AppointmentView} as an {@link Appointment} object.
     *
     * All text fields and ComboBoxes are prepopulated with the passed Appointment information.
     * @param url
     * @param resourceBundle
     */
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
        populateTimeComboBoxes();

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

    /**
     * this method populates the appointment type ComboBox with two predetermined options.
     */
    public void populateTypeComboBox() {
        apptTypeComboBox.getItems().addAll("Work", "Personal");
    }

    /**
     * this method populates the time ComboBoxes with the company hours from 0800-2200 EST, converted to system local hours.
     */
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

    /**
     * this method validates all information provided for the appointment to modify, then passes the information to {@link ApptDAO}.
     * @throws IOException
     * @throws SQLException
     */
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
                if (endTimeZDT.isBefore(startTimeZDT)) {
                    endTimeZDT = endTimeZDT.plusDays(1);
                }
            } catch (Exception e) {
            }
        }

        Customer cust = (Customer) apptCustomerComboBox.getSelectionModel().getSelectedItem();
        int customerID = 0;
        if (!apptCustomerComboBox.getSelectionModel().isEmpty()) {
            errorLabel.setText(errorLabel.getText() + "Select a customer. ");
            addConfirm = false;
        } else {
            customerID = cust.getCustomerID();
        }

        if (addConfirm) {
            if (!checkApptOverlapByCustomer(apptID, startTimeZDT, endTimeZDT)) {
                errorLabel.setText(errorLabel.getText() + "Selected time overlaps another appointment. ");
            }
        }


        if (addConfirm && checkApptOverlapByCustomer(apptID, startTimeZDT, endTimeZDT)) {
            updateAppointment(apptID, title, description, contactID, location, type, startTimeZDT, endTimeZDT, customerID, loggedInUserID);
            errorLabel.setText("Appointment ID " + apptID + " updated");
            Login.showApptView();
            Stage stage = (Stage) apptSaveButton.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * this method closes the current stage and opens a new stage for {@link AppointmentView}.
     * @throws IOException
     */
    public void onApptCancelButton() throws IOException {
        System.out.println("ModifyAppointment - Modify Appt Cancel button clicked");
        Stage stage = (Stage) apptCancelButton.getScene().getWindow();
        stage.close();
        Login.showApptView();
    }

    /**
     * this onAction method dynamically updates the end time ComboBox based on the selection of the start time ComboBox.
     */
    public void onApptStartTimeComboBox() {

        ZoneId eastern = ZoneId.of("America/New_York");  //eastern
        ZoneId localZoneId = TimeZone.getDefault().toZoneId(); //local

        LocalTime startLT = (LocalTime) apptStartComboBox.getSelectionModel().getSelectedItem(); //gets the selected time and casts to LocalTime object, will always be a local time
        LocalDateTime startLDT = LocalDateTime.of(LocalDate.now(), startLT); //converts the selected LocalTime into a LocalDateTime
        ZonedDateTime startZDT = ZonedDateTime.of(LocalDate.now(), LocalTime.of(8,0), eastern); //gets 0800 eastern time
        ZonedDateTime localZDT = startZDT.withZoneSameInstant(localZoneId); //converts 0800 eastern to local time
        ZonedDateTime localEnd = localZDT.plusHours(14); //adds 14 hours to localized start time. by doing this in ZonedDateTime, it accounts for the business hours going into the next day

        LocalDateTime endLDT = LocalDateTime.of(localEnd.toLocalDate(), localEnd.toLocalTime()); //converts the end time ZonedDateTime into a LocalDateTime for comparison to the selected start appointment start time

        apptEndComboBox.getItems().clear(); //clears items in end time combobox

        while (startLDT.isBefore(endLDT)) {
            startLDT = startLDT.plusMinutes(15);
            apptEndComboBox.getItems().add(startLDT.toLocalTime());
        }

//        ZonedDateTime startZDT = ZonedDateTime.of(LocalDate.now(), LocalTime.of(8,0), eastern);
//        LocalTime startLT = (LocalTime) apptStartComboBox.getSelectionModel().getSelectedItem();
//        ZonedDateTime localZDT = startZDT.withZoneSameInstant(localZoneId);
//        ZonedDateTime localStartZDT = ZonedDateTime.of(localZDT.toLocalDate(), startLT, localZoneId);
//        ZonedDateTime endZDT = ZonedDateTime.of(LocalDate.now(), LocalTime.of(22,0), eastern);
//        ZonedDateTime localEndZDT = endZDT.withZoneSameInstant(localZoneId);
//        apptEndComboBox.setValue(null);
//        apptEndComboBox.getItems().clear();
//
//        if ((startLT.isAfter(LocalTime.of(0,0)) && (startLT.isBefore(LocalTime.of(14,0)))) || startLT.equals(LocalTime.of(0,0))) {
//            localStartZDT = localStartZDT.plusDays(1);
//            while (localStartZDT.toLocalTime().isBefore(localEndZDT.toLocalTime().plusMinutes(-14))) {
//                localStartZDT = localStartZDT.plusMinutes(15);
//                apptEndComboBox.getItems().add(localStartZDT.toLocalTime());
//            }
//        } else {
//            while (localStartZDT.isBefore(localEndZDT.plusMinutes(-14))) {
//                localStartZDT = localStartZDT.plusMinutes(15);
//                apptEndComboBox.getItems().add(localStartZDT.toLocalTime());
//            }
//        }

//        populateTimeComboBoxes();
//
//        LocalTime start = (LocalTime) apptStartComboBox.getSelectionModel().getSelectedItem();
//
//        LocalTime end = LocalTime.of(22, 0);
//
//        apptEndComboBox.getItems().clear();
//        while (start.isBefore(end.plusMinutes(-14))) {
//            start = start.plusMinutes(15);
//            apptEndComboBox.getItems().add(start);
//        }
    }

    /**
     * this onAction method dynamically updates the country division ComboBox based on the country ComboBox selection.
     */
    public void onApptCountryComboBox() {
        Country loc = (Country) apptCountryComboBox.getSelectionModel().getSelectedItem();
        System.out.println(loc);
        try {
            apptCountryDivisionComboBox.getItems().setAll(getAllDivisionsByCountryID(loc.getCountryID()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
