package Appointment;

import javafx.collections.FXCollections;
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
import java.util.Objects;
import java.util.ResourceBundle;

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
    public static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
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
    public Button contactViewButton;
    public Button userViewButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("AppointmentView opened");

        apptIDColumn.setCellValueFactory(new PropertyValueFactory("apptID"));
        apptTitleColumn.setCellValueFactory(new PropertyValueFactory("apptTitle"));
        apptDescriptionColumn.setCellValueFactory(new PropertyValueFactory("apptDescription"));
        apptLocationColumn.setCellValueFactory(new PropertyValueFactory("apptLocation"));
        apptContactColumn.setCellValueFactory(new PropertyValueFactory("apptContact"));
        apptTypeColumn.setCellValueFactory(new PropertyValueFactory("apptType"));
        apptStartColumn.setCellValueFactory(new PropertyValueFactory("apptStart"));
        apptEndColumn.setCellValueFactory(new PropertyValueFactory("apptEnd"));
        apptCustomerIDColumn.setCellValueFactory(new PropertyValueFactory("apptCustomerID"));
        apptUserIDColumn.setCellValueFactory(new PropertyValueFactory("apptUserID"));
        System.out.println("Appt columns loaded");
        try {
            ObservableList<Appointment> apptList = ApptDAO.getAllAppointments();
            System.out.println("appointment list created from DB.getAllAppointments()");
            populateAppointments(apptList);
            System.out.println("tableview populated with appointments");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * //Appointment database columns and index//
     * 1, Appointment_ID (null)
     * 2, Title (string)
     * 3, Description (string)
     * 4, Location (string)
     * 5, Type (string)
     * 6, Start (localtime)
     * 7, End (localtime)
     * 8, Create_Date (localtime)
     * 9, Created_By (user)
     * 10, Last_Update (localtime)
     * 11, Last_Updated_By (user)
     * 12, Customer_ID (customer)
     * 13, User_ID (user)
     * 14, Contact_ID (customer)
     *
     */

    public void populateAppointments(ObservableList<Appointment> apptList) {
        apptTableView.setItems(apptList);
    }

    public void onApptMonthViewRadioButton(ActionEvent event) throws Exception {
        System.out.println("Month view radio button selected");
        System.out.println(getAllCountries());
    }

    public void onApptWeekViewRadioButton(ActionEvent event) throws Exception {
        System.out.println("Week view radio button selected");
        System.out.println(getAllDivisions());
    }

    public void onApptViewAllButton(ActionEvent event) throws Exception {
        System.out.println("View all appointments button clicked");
        System.out.println(getCountryDivisions(1));
    }

    public void onAddApptButton(ActionEvent event) throws IOException {
        System.out.println("Appointment add button clicked");

        Parent root = FXMLLoader.load(getClass().getResource("/Appointment/AddAppointment.fxml"));
        Stage addAppt = new Stage();
        addAppt.setTitle("Add Appointment - Sequeira Scheduler - WGU C195 PA Task");
        addAppt.setScene(new Scene(root));
        addAppt.show();

        Stage stage = (Stage) addApptButton.getScene().getWindow();
        stage.close();
    }

    public void onModifyApptButton() throws IOException {
        System.out.println("Modify appointment button clicked");

        Parent root = FXMLLoader.load(getClass().getResource("/Appointment/ModifyAppointment.fxml"));
        Stage modAppt = new Stage();
        modAppt.setTitle("Modify Appointment - Sequeira Scheduler - WGU C195 PA Task");
        modAppt.setScene(new Scene(root));
        modAppt.show();
    }

    public void onDeleteApptButton(ActionEvent event) {
        System.out.println("Delete appointment button clicked");
    }

    public void onLogoutButton(ActionEvent event) throws IOException {
        System.out.println("Logout button clicked");
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
        
        Parent root = FXMLLoader.load(getClass().getResource("/Main/Login.fxml"));
        Stage login = new Stage();
        login.setTitle("Sequeira Scheduler - WGU C195 PA Task");
        login.setScene(new Scene(root));
        login.show();

    }

    public void onCalcTotalApptsButton(ActionEvent event) {
        System.out.println("Total appointments calculated (button clicked)");
    }

    public void onClearSelectionsButton(ActionEvent event) {
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

    public void onContactViewButton(ActionEvent event) {
    }

    public void onUserViewButton(ActionEvent event) {
    }
}
