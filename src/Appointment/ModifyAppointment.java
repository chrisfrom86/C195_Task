package Appointment;

import javafx.event.ActionEvent;
import javafx.scene.control.*;

public class ModifyAppointment {
    public TextField apptIDTextField;
    public TextField apptTitleTextField;
    public TextField apptDescriptionTextField;
    public TextField apptTypeTextField;
    public TextField apptCustomerIDTextField;
    public TextField apptUserIDTextField;
    public DatePicker apptStartDatePicker;
    public ComboBox apptICustomerTextField;
    public ComboBox apptStartHourComboBox;
    public ComboBox apptStartMinComboBox;
    public ComboBox apptLengthHourComboBox;
    public ComboBox apptLengthMinComboBox;
    public Button apptSaveButton;
    public Button apptCancelButton;
    public Label errorLabel;

    public void onApptSaveButton(ActionEvent event) {
    }

    public void onApptCancelButton(ActionEvent event) {
    }
}
