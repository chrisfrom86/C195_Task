package Appointment;

import Contact.Contact;
import Customer.Customer;
import Location.Country;
import Location.CountryDivision;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Month;
import java.util.Objects;
import java.util.ResourceBundle;

import static Appointment.ApptDAO.*;
import static Contact.ContactDAO.getAllContacts;
import static Customer.CustomerDAO.getAllCustomers;
import static Customer.CustomerDAO.getCustomerByID;
import static Location.LocDAO.*;

public class AppointmentView implements Initializable {
    public RadioButton apptMonthViewRadioButton;
    public RadioButton apptWeekViewRadioButton;
    public Button addApptButton;
    public Button modifyApptButton;
    public Button deleteApptButton;
    public Label errorLabel;
    public Button apptViewAllButton;
    public Label totalApptsLabel;
    public Button logoutButton;
    public Button calcTotalApptsButton;
    public TableView apptTableView;
    public TableColumn apptIDColumn;
    public TableColumn apptTitleColumn;
    public TableColumn apptDescriptionColumn;
    public TableColumn apptLocationColumn;
    public TableColumn apptContactColumn;
    public TableColumn apptTypeColumn;
    public TableColumn apptStartColumn;
    public TableColumn apptEndColumn;
    public TableColumn apptCustomerIDColumn;
    public TableColumn apptUserIDColumn;
    public Button clearSelectionsButton;
    public Button customerViewButton;
    public static Appointment selectedAppointment;
    public ComboBox apptMonthReportComboBox;
    public ComboBox apptTypeReportComboBox;
    public ComboBox apptContactReportComboBox;
    public ComboBox apptViewByCustomerComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("AppointmentView - AppointmentView opened");
        apptTableView.setPlaceholder(new Label("No appointments."));

        apptIDColumn.setCellValueFactory(new PropertyValueFactory("apptID"));
        apptTitleColumn.setCellValueFactory(new PropertyValueFactory("apptTitle"));
        apptDescriptionColumn.setCellValueFactory(new PropertyValueFactory("apptDescription"));
        apptLocationColumn.setCellValueFactory(new PropertyValueFactory("apptLocation"));
        apptContactColumn.setCellValueFactory(new PropertyValueFactory("apptContactID"));
        apptTypeColumn.setCellValueFactory(new PropertyValueFactory("apptType"));
        apptStartColumn.setCellValueFactory(new PropertyValueFactory("apptStart"));
        apptEndColumn.setCellValueFactory(new PropertyValueFactory("apptEnd"));
        apptCustomerIDColumn.setCellValueFactory(new PropertyValueFactory("apptCustomerID"));
        apptUserIDColumn.setCellValueFactory(new PropertyValueFactory("apptUserID"));
        System.out.println("AppointmentView - Appt columns loaded");
        try {
            ObservableList<Appointment> apptList = ApptDAO.getAllAppointments();
            System.out.println("AppointmentView - appointment list created from DB.getAllAppointments()");
            populateAppointments(apptList);
            System.out.println("AppointmentView - tableview populated with appointments");
        } catch (Exception e) {
            e.printStackTrace();
        }

        populateCustomerComboBox();

        try {
            populateReportComboBoxes();
        } catch (Exception e) {
            System.out.println("AppointmentView - couldn't populate report combo boxes");
            e.printStackTrace();
        }
    }

    public void populateAppointments(ObservableList<Appointment> apptList) {
        apptTableView.setItems(apptList);
    }

    public void populateReportComboBoxes() throws Exception {
        for (Month m : Month.values()) {
            apptMonthReportComboBox.getItems().add(m.toString());
        }

        apptTypeReportComboBox.getItems().addAll("Work", "Personal");

        apptContactReportComboBox.getItems().addAll(getAllContacts());
    }

    public void populateCustomerComboBox() {
        try {
            apptViewByCustomerComboBox.setItems(getAllCustomers());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onApptMonthViewRadioButton(ActionEvent event) throws Exception {
        System.out.println("AppointmentView - Month view radio button selected");
        populateAppointments(get1MonthUpcomingAppts());
    }

    public void onApptWeekViewRadioButton(ActionEvent event) throws Exception {
        System.out.println("AppointmentView - Week view radio button selected");
        populateAppointments(get1WeekUpcomingAppts());
    }

    public void onApptViewAllButton(ActionEvent event) throws Exception {
        System.out.println("AppointmentView - View all appointments button clicked");
        populateAppointments(getAllAppointments());
        apptMonthViewRadioButton.setSelected(false);
        apptWeekViewRadioButton.setSelected(false);
    }

    public void onAddApptButton(ActionEvent event) throws IOException {
        System.out.println("AppointmentView - Appointment add button clicked");

        Parent root = FXMLLoader.load(getClass().getResource("/Appointment/AddAppointment.fxml"));
        Stage addAppt = new Stage();
        addAppt.setTitle("Add Appointment - Sequeira Scheduler - WGU C195 PA Task");
        addAppt.setScene(new Scene(root));
        addAppt.show();

        Stage stage = (Stage) addApptButton.getScene().getWindow();
        stage.close();
    }

    public void onModifyApptButton() throws IOException {
        System.out.println("AppointmentView - Modify appointment button clicked");
        try {
            selectedAppointment = (Appointment) apptTableView.getSelectionModel().getSelectedItem();
            selectedAppointment.setApptCustomer(getCustomerByID(selectedAppointment.getApptCustomerID()));
            CountryDivision countryDivision = getDivisionByName(selectedAppointment.apptLocation);
            Country country = getCountryByID(countryDivision.countryID);
            selectedAppointment.setApptCountry(country);
            selectedAppointment.setApptCountryDivision(countryDivision);
            selectedAppointment.setApptCustomer(getCustomerByID(selectedAppointment.getApptCustomerID()));

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Appointment/ModifyAppointment.fxml")));
            Stage modAppt = new Stage();
            modAppt.setTitle("Modify Appointment - Sequeira Scheduler - WGU C195 PA Task");
            modAppt.setScene(new Scene(root));
            modAppt.show();
            Stage stage = (Stage) modifyApptButton.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.out.println("AppointmentView - NPE no appointment selected");
            e.printStackTrace();
            errorLabel.setText("Select an appointment.");
        }
    }

    public void onDeleteApptButton(ActionEvent event) throws Exception {
        System.out.println("AppointmentView - Delete appointment button clicked");
        Appointment appt = (Appointment) apptTableView.getSelectionModel().getSelectedItem();
        deleteAppointment(appt.getApptID());
        populateAppointments(getAllAppointments());
        errorLabel.setText("ID: " + appt.getApptID() + ", Type: " + appt.apptType + "\nAppointment deleted.");
    }

    public void onLogoutButton() throws IOException {
        System.out.println("AppointmentView - Logout button clicked");
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
        
        Parent root = FXMLLoader.load(getClass().getResource("/Login/Login.fxml"));
        Stage login = new Stage();
        login.setTitle("Sequeira Scheduler - WGU C195 PA Task");
        login.setScene(new Scene(root));
        login.show();

    }

    public void onCalcTotalApptsButton(ActionEvent event) throws SQLException {
        System.out.println("AppointmentView - Total appointments calculated (button clicked)");
        System.out.println(apptMonthReportComboBox.getSelectionModel().getSelectedItem());
        String month = (String) apptMonthReportComboBox.getSelectionModel().getSelectedItem();
        int monthNumber = 0;
        if (month != null) {
            Month monthName = Month.valueOf(month);
            monthNumber = monthName.getValue();
        }
        System.out.println(apptTypeReportComboBox.getSelectionModel().getSelectedItem());
        String type = (String) apptTypeReportComboBox.getSelectionModel().getSelectedItem();
        System.out.println(apptContactReportComboBox.getSelectionModel().getSelectedItem());
        Contact contact = (Contact) apptContactReportComboBox.getSelectionModel().getSelectedItem();

        if (monthNumber == 0 && type == null && contact == null)
            totalApptsLabel.setText("Selection total = " + getApptReport());
        if (monthNumber != 0 && type == null && contact == null)
            totalApptsLabel.setText("Selection total = " + getApptReport(monthNumber));
        if (monthNumber == 0 && type != null && contact == null)
            totalApptsLabel.setText("Selection total = " + getApptReport(type));
        if (monthNumber == 0 && type == null && contact != null)
            totalApptsLabel.setText("Selection total = " + getApptReport(contact));
        if (monthNumber != 0 && type != null && contact == null)
            totalApptsLabel.setText("Selection total = " + getApptReport(monthNumber, type));
        if (monthNumber == 0 && type != null && contact != null)
            totalApptsLabel.setText("Selection total = " + getApptReport(type, contact));
        if (monthNumber != 0 && type == null && contact != null)
            totalApptsLabel.setText("Selection total = " + getApptReport(monthNumber, contact));
        if (monthNumber != 0 && type != null && contact != null)
            totalApptsLabel.setText("Selection total = " + getApptReport(monthNumber, type, contact));
    }

    public void onClearSelectionsButton(ActionEvent event) {
        System.out.println("AppointmentView - Report selections cleared");
        apptMonthReportComboBox.setValue(null);
        apptTypeReportComboBox.setValue(null);
        apptContactReportComboBox.setValue(null);
        totalApptsLabel.setText("");
    }

    public void onCustomerViewButton(ActionEvent event) throws IOException {
        Stage stage = (Stage) customerViewButton.getScene().getWindow();
        stage.close();

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Customer/CustomerView.fxml")));
        Stage stage2 = new Stage();
        stage2.setTitle("Customer Viewer - Sequeira Scheduler - WGU C195 PA Task");
        stage2.setScene(new Scene(root));
        stage2.show();
    }

    public void onViewApptByCustomerComboBox() throws SQLException {
        System.out.println("AppointmentView - view appointments by selected customer: " + apptViewByCustomerComboBox.getSelectionModel().getSelectedItem());
        Customer selectedCustomer = (Customer) apptViewByCustomerComboBox.getSelectionModel().getSelectedItem();
        populateAppointments(getApptsByCustomer(selectedCustomer.getCustomerName()));
    }
}
