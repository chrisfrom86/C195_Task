package Appointment;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class ModifyAppointment implements Initializable {
    public TextField apptIDTextField;
    public TextField apptTitleTextField;
    public TextField apptDescriptionTextField;
    public TextField apptTypeTextField;
    public TextField apptCustomerIDTextField;
    public TextField apptUserIDTextField;
    public DatePicker apptStartDatePicker;
    public ComboBox apptICustomerTextField;
    public ComboBox apptStartComboBox;
    public ComboBox apptEndComboBox;
    public Button apptSaveButton;
    public Button apptCancelButton;
    public Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(22, 0);
        while (start.isBefore(end.plusMinutes(-14))) {
            apptStartComboBox.getItems().add(start);
            start = start.plusMinutes(15);
            apptEndComboBox.getItems().add(start);
        }
    }

    public void onApptSaveButton() {
        System.out.println("Modify Appt Save button clicked");
        String sql = "update appointments set where id";
    }

    public void onApptCancelButton(ActionEvent event) {
        System.out.println("Modify Appt Cancel button clicked");
        Stage stage = (Stage) apptCancelButton.getScene().getWindow();
        stage.close();
    }

}
