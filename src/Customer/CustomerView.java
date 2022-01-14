package Customer;

import Location.Location;
import Main.DB;
import Main.Login;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static Customer.CustomerDAO.getAllCustomers;
import static Location.LocDAO.getAllCountries;
import static Location.LocDAO.getCountryDivisions;

public class CustomerView implements Initializable {
    public TableView customerTableView;
    public Button apptViewButton;
    public TableColumn custIDColumn;
    public TableColumn custNameColumn;
    public TableColumn custAddressColumn;
    public TableColumn custPostalCodeColumn;
    public TableColumn custPhoneColumn;
    public TableColumn custLocationColumn;
    public TextField custIDTextField;
    public TextField custNameTextField;
    public TextField custPhoneTextField;
    public TextField custAddressTextField;
    public TextField custPostalCodeTextField;
    public ComboBox custCountryComboBox;
    public ComboBox custCountryDivisionComboBox;
    public Label errorLabel;
    public Button customerAddButton;
    public Button customerUpdateButton;
    public Button customerDeleteButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("customer view opened");

        custIDColumn.setCellValueFactory(new PropertyValueFactory("customerID"));
        custNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        custAddressColumn.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        custPostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));
        custPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        custLocationColumn.setCellValueFactory(new PropertyValueFactory<>("customerLocation"));
        System.out.println("Customer columns loaded");

        try {
            custCountryComboBox.setItems(getAllCountries());
        } catch (Exception e) {
            e.printStackTrace();
        }
        custCountryDivisionComboBox.setVisibleRowCount(10);

        try {
            customerTableView.setItems(getAllCustomers());
        } catch (Exception e) {
            e.printStackTrace();
        }

        customerUpdateButton.setDisable(true);
        customerDeleteButton.setDisable(true);
        customerTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            DB.getConnection();

        });
    }

    public void customerAdd(ActionEvent event) {
    }

    public void customerModify(ActionEvent event) {
    }

    public void customerDelete(ActionEvent event) {
    }

    public void onApptViewButton(ActionEvent event) throws IOException {
        System.out.println("Add Appt Cancel button clicked");
        Login.showApptView();
        Stage stage = (Stage) apptViewButton.getScene().getWindow();
        stage.close();
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

    public void onCustomerAddButton(ActionEvent event) throws Exception {
        System.out.println("add/update customer button clicked");
        DB.getConnection();
        String sql = "INSERT INTO customers(customer_name, address, postal_code, phone, create_date, created_by, last_update, last_updated_by, division_id)" +
                " VALUES(?,?,?,?,NOW(),?,NOW(),?,?);";
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ps.setString(1, custNameTextField.getText());
        System.out.println(custNameTextField.getText());
        ps.setString(2, custAddressTextField.getText());
        System.out.println(custAddressTextField.getText());
        ps.setString(3, custPostalCodeTextField.getText());
        System.out.println(custPostalCodeTextField.getText());
        ps.setString(4, custPhoneTextField.getText());
        System.out.println(custPhoneTextField.getText());
        ps.setString(5, "user creator");
        System.out.println("user creator");
        ps.setString(6, "last update user");
        System.out.println("last update user");
        Location loc = (Location) custCountryDivisionComboBox.getSelectionModel().getSelectedItem();
        ps.setInt(7, loc.getDivisionID());
        System.out.println(loc.getDivision());

        System.out.println(ps);
        ps.executeUpdate();
        errorLabel.setText("New customer added");
        customerTableView.getItems().setAll(getAllCustomers());
    }

    public void onCustomerUpdateButton(ActionEvent event) {
    }

    public void onCustomerDeleteButton(ActionEvent event) {
        System.out.println("delete customer button clicked");
    }

    public void onClearSelectionButton(ActionEvent event) {
        System.out.println("clear selection button clicked");
        customerTableView.getSelectionModel().clearSelection();
    }
}
