package Appointment;

import Contact.Contact;
import Customer.Customer;
import Customer.CustomerView;
import Location.Country;
import Location.CountryDivision;
import Main.DB;
import javafx.collections.ObservableList;
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

/**
 * @author Chris Sequeira
 *
 * this method is the bread and butter, the crown jewel, the pièce de résistance of this project.
 *
 * This form calls on AppointmentView.fxml and has the following functions (listed from top left to bottom right, going across):
 *      - Radio buttons to select 1 Month or 1 Week views.
 *      - Button to View All Appointments in the TableView.
 *      - ComboBox populated with Customers to filter appointments in the TableView.
 *      - Report Viewer with three ComboBoxes (month, type, contact) that can be used in any order or no selection to generate a report.
 *      - Clear Selections button for the report ComboBoxes.
 *      - TableView that shows appointments based on the selections from the top left. Shows all appointments on initial form load.
 *      - Add, Modify, and Delete Appointment buttons. Add/Modify buttons close the current stage and open a new one.
 *      - Delete Appointment returns a message at the bottom of the screen based on the appointment deleted.
 *      - Customer Viewer button closes the stage and opens a new stage {@link CustomerView}.
 *      - Logout button returns the user to the login screen.
 */
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

    /**
     * this method initializes the TableView columns, populates all appointments as rows, and populates the report and customer ComboBoxes.
     *
     * the logout button uses a lambda expression to execute the following onAction.
     *      1. closes the current stage.
     *      2. loads the Login.fxml as a Parent to pass into the scene.
     *      3. closes the database connection.
     *      4. creates a new stage, loads a scene with the Login.fxml passed as a parameter, and then shows the stage.
     * @param url
     * @param resourceBundle
     */
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

        logoutButton.setOnAction(e-> {
            System.out.println("AppointmentView - Logout button clicked");
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.close();

            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/Login/Login.fxml"));
            } catch (IOException ioException) {
                System.out.println("AppointmentView - IOException on loading login screen from logout button click");
            }
            try {
                DB.closeConnection();
                System.out.println("AppointmentView - database connection closed on logout button click");
            } catch (Exception exception) {
                System.out.println("AppointmentView - Exception on DB.closeConnection of logout button");
            }
            Stage login = new Stage();
            login.setTitle("Sequeira Scheduler - WGU C195 PA Task");
            login.setScene(new Scene(root));
            login.show();
        });
    }

    /**
     * this method populates the appointment TableView with a given {@param ApptList}.
     *
     * it is called with getALlAppointments on initialize, and then passed various ObservableLists from {@link ApptDAO}.
     * @param apptList
     */
    public void populateAppointments(ObservableList<Appointment> apptList) {
        apptTableView.setItems(apptList);
    }

    /**
     * this method populates the report ComboBoxes. it is called in initialize.
     * @throws Exception
     */
    public void populateReportComboBoxes() throws Exception {
        for (Month m : Month.values()) {
            apptMonthReportComboBox.getItems().add(m.toString());
        }

        apptTypeReportComboBox.getItems().addAll("Work", "Personal");

        apptContactReportComboBox.getItems().addAll(getAllContacts());
    }

    /**
     * this method populates the customer ComboBox in AppointmentView so that users can view appointments by selected customer.
     */
    public void populateCustomerComboBox() {
        try {
            apptViewByCustomerComboBox.setItems(getAllCustomers());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * this method is the onAction for the Month view RadioButton.
     *
     * it calls the get1MonthUpcomingAppointments() from {@link ApptDAO} and populates the Tableview with the resulting ObservableList.
     * @throws Exception
     */
    public void onApptMonthViewRadioButton() throws Exception {
        System.out.println("AppointmentView - Month view radio button selected");
        populateAppointments(get1MonthUpcomingAppts());
    }

    /**
     * this method is the onAction for the Week view RadioButton.
     *
     * it calls the get1WeekUpcomingAppointments() from {@link ApptDAO} and populates the Tableview with the resulting ObservableList.
     * @throws Exception
     */
    public void onApptWeekViewRadioButton() throws Exception {
        System.out.println("AppointmentView - Week view radio button selected");
        populateAppointments(get1WeekUpcomingAppts());
    }

    /**
     * this method is the onAction for the View All Appointments button.
     *
     * it loads all appointments from getAllAppointments() from {@link ApptDAO}.
     * @throws Exception
     */
    public void onApptViewAllButton() throws Exception {
        System.out.println("AppointmentView - View all appointments button clicked");
        populateAppointments(getAllAppointments());
        apptMonthViewRadioButton.setSelected(false);
        apptWeekViewRadioButton.setSelected(false);
    }

    /**
     * this method closes the current stage and opens {@link AddAppointment}.
     * @throws IOException
     */
    public void onAddApptButton() throws IOException {
        System.out.println("AppointmentView - Appointment add button clicked");

        Parent root = FXMLLoader.load(getClass().getResource("/Appointment/AddAppointment.fxml"));
        Stage addAppt = new Stage();
        addAppt.setTitle("Add Appointment - Sequeira Scheduler - WGU C195 PA Task");
        addAppt.setScene(new Scene(root));
        addAppt.show();

        Stage stage = (Stage) addApptButton.getScene().getWindow();
        stage.close();
    }

    /**
     * this method closes the current stage and opens {@link ModifyAppointment}.
     * @throws IOException
     */
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
            //e.printStackTrace();
            errorLabel.setText("Select an appointment.");
        }
    }

    /**
     * this method deletes the selected appointment from the TableView by passing its ID to {@link ApptDAO}.
     *
     * If no row is selected in the TableView, an error is output to errorLabel.
     *
     * @throws Exception
     */
    public void onDeleteApptButton() throws Exception {
        System.out.println("AppointmentView - Delete appointment button clicked");
        Appointment appt = (Appointment) apptTableView.getSelectionModel().getSelectedItem();
        deleteAppointment(appt.getApptID());
        populateAppointments(getAllAppointments());
        errorLabel.setText("ID: " + appt.getApptID() + ", Type: " + appt.apptType + "\nAppointment deleted.");
    }

    /**
     * @deprecated this method was used to define the onAction for the Logout Button, but is now handled in a lambda expression in initialize.
     * @throws IOException
     */
    public void onLogoutButton() throws IOException {
//        System.out.println("AppointmentView - Logout button clicked");
//        Stage stage = (Stage) logoutButton.getScene().getWindow();
//        stage.close();
//
//        Parent root = FXMLLoader.load(getClass().getResource("/Login/Login.fxml"));
//        Stage login = new Stage();
//        login.setTitle("Sequeira Scheduler - WGU C195 PA Task");
//        login.setScene(new Scene(root));
//        login.show();

    }

    /**
     * this is the onAction for generating reports in the top right of AppointmentView.
     *
     * if the button is clicked with no ComboBox selections, it returns the total number of appointments in a message Label.
     *
     * if any combination of ComboBox selections are selected, a version of the overloaded method from {@link ApptDAO} is called.
     * @throws SQLException
     */
    public void onCalcTotalApptsButton() throws SQLException {
        System.out.println("AppointmentView - Total appointments calculated (button clicked)");
        System.out.println("AppointmentView - month selected: " + apptMonthReportComboBox.getSelectionModel().getSelectedItem());
        String month = (String) apptMonthReportComboBox.getSelectionModel().getSelectedItem();
        int monthNumber = 0;
        if (month != null) {
            Month monthName = Month.valueOf(month);
            monthNumber = monthName.getValue();
        }
        System.out.println("AppointmentView - type selected: " + apptTypeReportComboBox.getSelectionModel().getSelectedItem());
        String type = (String) apptTypeReportComboBox.getSelectionModel().getSelectedItem();
        System.out.println("AppointmentView - contact selected: " + apptContactReportComboBox.getSelectionModel().getSelectedItem());
        Contact contact = (Contact) apptContactReportComboBox.getSelectionModel().getSelectedItem();

        if (monthNumber == 0 && type == null && contact == null)
            totalApptsLabel.setText("Total appointments = " + getApptReport());
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

    /**
     * this method clears the report ComboBoxes in case the user wants to select a different combination of ComboBox options.
     */
    public void onClearSelectionsButton() {
        System.out.println("AppointmentView - Report selections cleared");
        apptMonthReportComboBox.setValue(null);
        apptTypeReportComboBox.setValue(null);
        apptContactReportComboBox.setValue(null);
        totalApptsLabel.setText("");
    }

    /**
     * this method closes the current stage and opens {@link CustomerView}.
     * @throws IOException
     */
    public void onCustomerViewButton() throws IOException {
        Stage stage = (Stage) customerViewButton.getScene().getWindow();
        stage.close();

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Customer/CustomerView.fxml")));
        Stage stage2 = new Stage();
        stage2.setTitle("Customer Viewer - Sequeira Scheduler - WGU C195 PA Task");
        stage2.setScene(new Scene(root));
        stage2.show();
    }

    /**
     * this method is the onAction for the customer ComboBox. it gets all customers based on a given name and populates them into the TableView.
     *
     * @throws SQLException
     */
    public void onViewApptByCustomerComboBox() throws SQLException {
        System.out.println("AppointmentView - view appointments by selected customer: " + apptViewByCustomerComboBox.getSelectionModel().getSelectedItem());
        Customer selectedCustomer = (Customer) apptViewByCustomerComboBox.getSelectionModel().getSelectedItem();
        populateAppointments(getApptsByCustomer(selectedCustomer.getCustomerName()));
    }
}
