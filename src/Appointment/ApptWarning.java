package Appointment;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Chris Sequeira
 *
 * this class defines the ApptWarning form that tells the user about upcoming appointments on login.
 */
public class ApptWarning implements Initializable {
    public Button warningCloseButton;
    public TableView apptWarningTableView;
    public TableColumn apptWarningIDColumn;
    public TableColumn apptWarningTimestampColumn;

    /**
     * this method initializes the columns and populates the rows based on appointments within 15 minutes of login found in {@link ApptDAO}.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("ApptWarning - appointment warning displayed");
        apptWarningTableView.setPlaceholder(new Label("No upcoming appointments."));
        apptWarningIDColumn.setCellValueFactory(new PropertyValueFactory("apptID"));
        apptWarningTimestampColumn.setCellValueFactory(new PropertyValueFactory("apptStart"));

        try {
            ObservableList<Appointment> apptList = ApptDAO.get15MinUpcomingAppts();
            System.out.println("ApptWarning - all appointments added to a list");
            apptWarningTableView.getItems().addAll(apptList);
            System.out.println("ApptWarning - tableview populated with appointments");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * this method closes the stage.
     */
    public void onApptViewButton() {
        Stage stage = (Stage) warningCloseButton.getScene().getWindow();
        stage.close();
    }

}
