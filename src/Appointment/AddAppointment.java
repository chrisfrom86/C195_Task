package Appointment;

import Contact.Contact;
import Customer.Customer;
import Location.Country;
import Location.CountryDivision;
import Login.Login;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.ResourceBundle;
import java.util.TimeZone;

import static Appointment.ApptDAO.*;
import static Contact.ContactDAO.getAllContacts;
import static Customer.CustomerDAO.getAllCustomers;
import static Location.LocDAO.*;
import static Login.Login.loggedInUserID;

/**
 * @author Chris Sequeira
 *
 * this class controls the AddAppointment.fxml form.
 *
 * It allows users to add new appointments based on validated inputs that will be passed to {@link ApptDAO}.
 */
public class AddAppointment implements Initializable {
    public TextField apptIDTextField;
    public TextField apptTitleTextField;
    public TextField apptDescriptionTextField;
    public ComboBox apptTypeComboBox;
    public TextField apptUserIDTextField;
    public DatePicker apptDatePicker;
    public ComboBox apptCustomerComboBox;
    public ComboBox apptStartComboBox;
    public ComboBox apptEndComboBox;
    public Button apptSaveButton;
    public Button apptCancelButton;
    public Label errorLabel;
    public ComboBox apptCountryComboBox;
    public ComboBox apptCountryDivisionComboBox;
    public ComboBox apptContactComboBox;

    /**
     * this method populates the Customer, Country, and Contact ComboBoxes with all relevant items as an ObservableList.
     *
     * the country division (first level division) ComboBox is limited to show 10 items at a time.
     *
     * The appointment type ComboBox is populated with the options Work and Personal.
     *
     * the cancel button onAction is handled by a lambda function that shows {@link AppointmentView} and closes the current stage.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("AddAppointment - initialized");

        try {
            apptContactComboBox.setItems(getAllContacts());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            apptCustomerComboBox.setItems(getAllCustomers());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            apptCountryComboBox.setItems(getAllCountries());
        } catch (Exception e) {
            e.printStackTrace();
        }
        apptCountryDivisionComboBox.setVisibleRowCount(10);

        populateTimeComboBoxes();
        populateTypeComboBox();

        apptUserIDTextField.setText(String.valueOf(loggedInUserID));

        apptCancelButton.setOnAction(e-> {
            System.out.println("AddAppointment - Add Appt Cancel button clicked");
            try {
                Login.showApptView();
            } catch (IOException ioException) {
                System.out.println("AddAppointment - IOException on cancel button click");
            }
            Stage stage = (Stage) apptCancelButton.getScene().getWindow();
            stage.close();
        });
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
     * this method validates all information provided for the appointment to create, then passes the information to {@link ApptDAO}.
     */
    public void onApptSaveButton() throws Exception {
        System.out.println("AddAppointment - appt save button clicked");
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
        int contactID = 0;
        if (apptContactComboBox.getSelectionModel().isEmpty()) {
            errorLabel.setText(errorLabel.getText() + "Select a contact. ");
            addConfirm = false;
        } else {
            contactID = contact.contactID;
        }

        String type = (String) apptTypeComboBox.getSelectionModel().getSelectedItem();
        if (type == null || apptTypeComboBox.getSelectionModel().getSelectedItem() == null) {
            errorLabel.setText(errorLabel.getText() + "Enter a type. ");
            addConfirm = false;
        }

        CountryDivision div = (CountryDivision) apptCountryDivisionComboBox.getSelectionModel().getSelectedItem();
        String location = null;
        if (apptCountryDivisionComboBox.getSelectionModel().isEmpty()) {
            errorLabel.setText(errorLabel.getText() + "Select a location. ");
            addConfirm = false;
        } else {
            location  = div.getDivision();
        }

        LocalDate apptDate = apptDatePicker.getValue();
        if (apptDate == null) {
            errorLabel.setText(errorLabel.getText() + "Select a date. ");
            addConfirm = false;
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
                System.out.println(e);
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
                System.out.println(e);
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
            if (!checkApptOverlapByCustomer(startTimeZDT, endTimeZDT)) {
                errorLabel.setText(errorLabel.getText() + "Selected time overlaps another appointment. ");
            }
        }

        if (addConfirm && checkApptOverlapByCustomer(startTimeZDT, endTimeZDT)) {
            addAppointment(title, description, contactID, location, type, startTimeZDT, endTimeZDT, customerID, loggedInUserID);
            errorLabel.setText("New appointment added");
            Login.showApptView();
            Stage stage = (Stage) apptSaveButton.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * @deprecated this onAction method was previously used to handle clicks of the button, but this is now handled in a lambda expression in initialize.
     */
   public void onApptCancelButton() {}
//        System.out.println("AddAppointment - Add Appt Cancel button clicked");
//        Login.showApptView();
//        Stage stage = (Stage) apptCancelButton.getScene().getWindow();
//        stage.close();

    /**
     * this onAction method dynamically updates the country division ComboBox based on the country ComboBox selection.
     */
    public void onApptCountryComboBox() throws Exception {
        Country loc = (Country) apptCountryComboBox.getSelectionModel().getSelectedItem();
        System.out.println(loc);
        try {
           apptCountryDivisionComboBox.getItems().setAll(getAllDivisionsByCountryID(loc.getCountryID()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * this onAction method dynamically updates the end time ComboBox based on the selection of the start time ComboBox.
     */
    public void onApptStartTimeComboBox() {
        //populateTimeComboBoxes();

        ZoneId eastern = ZoneId.of("America/New_York");  //eastern
        ZoneId localZoneId = TimeZone.getDefault().toZoneId(); //local
//

//        apptEndComboBox.getItems().clear(); //clears items in end time combobox

        //only works while all hours are in the same day
//            LocalTime endLT = startLT.plusHours(8);
//        while (startLT.isBefore(endLT)) {
//            startLT = startLT.plusMinutes(15);
//            apptEndComboBox.getItems().add(startLT);
//        }

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

        /**
         * @deprecated this section attempted to calculate day changes based on the appointment start hour selected.
         *
         * the final solution converts the business opening hour to local ZonedDateTime, converts to the local ZoneId,
         * then uses plusHours to add to the business opening hours (in local time), then adds 15 minute increments to the selected time and populates each increment into the apptEndComboBox.
         *
         * This ensures that if the end time goes into the next day (based on local time zone), it is calculated correctly when populating the apptEndComboBox.
         */
//        ZonedDateTime localStartZDT = ZonedDateTime.of(localZDT.toLocalDate(), startLT, localZoneId); //creates a ZDT of the local date of 0800 eastern, selected start time, and in the local time zone
//        ZonedDateTime endZDT = ZonedDateTime.of(LocalDate.now(), LocalTime.of(22,0), eastern); //gets 2200 eastern time
//        ZonedDateTime localEndZDT = endZDT.withZoneSameInstant(localZoneId); //converts 2200 eastern to local time

//        if ((startLT.isAfter(LocalTime.of(0,0)) && (startLT.isBefore(LocalTime.of(1,0)))) || startLT.equals(LocalTime.of(0,0))) {
//            localStartZDT = localStartZDT.plusDays(1);
//            while (localStartZDT.toLocalTime().isBefore(localEndZDT.toLocalTime().plusMinutes(-14))) {
//                localStartZDT = localStartZDT.plusMinutes(15);
//                apptEndComboBox.getItems().add(localStartZDT.toLocalTime());
//            }
//        } else {
//                while (localStartZDT.isBefore(localEndZDT.plusMinutes(-14))) {
//                    localStartZDT = localStartZDT.plusMinutes(15);
//                    apptEndComboBox.getItems().add(localStartZDT.toLocalTime());
//            }
//        }
    }
}
