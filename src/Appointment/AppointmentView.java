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
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("AppointmentView opened");
        getAppointments();
    }

    public TableView<Appointment> getAppointments() {
        apptTableView.setItems(allAppointments);
            apptIDColumn.setCellValueFactory(new PropertyValueFactory<>("apptID"));
            apptTitleColumn.setCellValueFactory(new PropertyValueFactory<>("apptTitle"));
            apptDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("apptDescription"));
            apptLocationColumn.setCellValueFactory(new PropertyValueFactory<>("apptLocation"));
            apptContactColumn.setCellValueFactory(new PropertyValueFactory<>("apptContact"));
            apptTypeColumn.setCellValueFactory(new PropertyValueFactory<>("apptType"));
            apptStartColumn.setCellValueFactory(new PropertyValueFactory<>("apptStart"));
            apptEndColumn.setCellValueFactory(new PropertyValueFactory<>("apptEnd"));
            apptCustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("apptCustomerID"));
            apptUserIDColumn.setCellValueFactory(new PropertyValueFactory<>("apptUserID"));
        return apptTableView;
    }

    public void onApptMonthViewRadioButton(ActionEvent event) {
        System.out.println("Month view radio button selected");
    }

    public void onApptWeekViewRadioButton(ActionEvent event) {
        System.out.println("Week view radio button selected");
    }

    public void onAddApptButton(ActionEvent event) throws IOException {
        System.out.println("Appointment add button clicked");
        Appointment appt1 = new Appointment(1,
                "Appt 1",
                "Appt Description",
                "My House", "Me", "Video Game Break",
                LocalDateTime.of(2020, 01, 30, 00,00),
                LocalDateTime.of(2020, 01, 31, 00,00),
                123, 1);
        allAppointments.add(appt1);
        getAppointments();

        Parent root = FXMLLoader.load(getClass().getResource("/Appointment/AddAppointment.fxml"));
        Stage addAppt = new Stage();
        addAppt.setTitle("Add Appointment - Sequeira Scheduler - WGU C195 PA Task");
        addAppt.setScene(new Scene(root));
        addAppt.show();
    }

    public void onModifyApptButton(ActionEvent event) {
        System.out.println("Modify appointment button clicked");
    }

    public void onDeleteApptButton(ActionEvent event) {
        System.out.println("Delete appointment button clicked");
        Appointment removeAppt = (Appointment) apptTableView.getSelectionModel().getSelectedItem();
        allAppointments.remove(removeAppt);
    }

    public void onApptViewAllButton(ActionEvent event) {
        System.out.println("View all appointments button clicked");
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

}
