package Customer;

import Location.Location;
import Main.DB;
import Main.Login;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ResourceBundle;

import static Location.LocDAO.getAllCountries;
import static Location.LocDAO.getCountryDivisions;

public class CustomerAdd implements Initializable {
    public TextField custID;
    public TextField custName;
    public TextField custAddress;
    public TextField custPostalCode;
    public TextField custPhone;
    public ComboBox custCountryComboBox;
    public ComboBox custCountryDivisionComboBox;
    public Label errorLabel;
    public Button customerAddButton;
    public Button customerCancelButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            custCountryComboBox.setItems(getAllCountries());
        } catch (Exception e) {
            e.printStackTrace();
        }
        custCountryDivisionComboBox.setVisibleRowCount(10);

    }

    public void onCustCountryComboBox(ActionEvent event) {
        Location loc = (Location) custCountryComboBox.getSelectionModel().getSelectedItem();
        System.out.println(loc);
        try {
            custCountryDivisionComboBox.getItems().setAll(getCountryDivisions(loc.getCountryID()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onCustomerAddButton(ActionEvent event) throws SQLException, IOException {
        DB.getConnection();
        String sql = "insert into customers(customer_name, address, postal_code, phone, division_id) values(?,?,?,?,?);";
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ps.setString(1, custName.getText());
        ps.setString(2, custAddress.getText());
        ps.setString(3, custPostalCode.getText());
        ps.setString(4, custPhone.getText());
        Location loc = (Location) custCountryDivisionComboBox.getSelectionModel().getSelectedItem();
        ps.setString(5, loc.getDivision());

        ps.executeUpdate();
        errorLabel.setText("New customer added");
        Login.showApptView();
        Stage stage = (Stage) customerAddButton.getScene().getWindow();
        stage.close();
    }

    public void onCustomerCancelButton(ActionEvent event) throws IOException {
        System.out.println("Add Customer Cancel button clicked");
        Login.showApptView();
        Stage stage = (Stage) customerCancelButton.getScene().getWindow();
        stage.close();
    }
}
