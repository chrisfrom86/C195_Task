package Customer;

import Location.Country;
import Location.CountryDivision;
import Login.Login;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static Customer.CustomerDAO.*;
import static Location.LocDAO.getAllCountries;
import static Location.LocDAO.getAllDivisionsByCountryID;

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
    public Button editSelectionButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("CustomerView - initialized");
        customerTableView.setPlaceholder(new Label("No customers."));


        custIDColumn.setCellValueFactory(new PropertyValueFactory("customerID"));
        custNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        custAddressColumn.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        custPostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));
        custPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        custLocationColumn.setCellValueFactory(new PropertyValueFactory<>("customerCountryDivision"));
        System.out.println("CustomerView - Customer columns loaded");

        setCustomerTableView();
        setCustCountryComboBox();
        custCountryDivisionComboBox.setVisibleRowCount(10);
        disableUpdateDeleteButtons();
        errorLabel.setText(defaultErrorLabelText());
    }

    public void setCustomerTableView() {
        try {
            customerTableView.setItems(getAllCustomers());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void populateTextFields(Customer cust) throws Exception {
        //System.out.println("Selected Customer - ID: " + cust.getCustomerID() + "; Name: " + cust.customerName);
        custIDTextField.setText(cust.customerID.getValue().toString());
        custNameTextField.setText(cust.getCustomerName());
        custPhoneTextField.setText(cust.getCustomerPhone());
        custAddressTextField.setText(cust.getCustomerAddress());
        custPostalCodeTextField.setText(cust.getCustomerPostalCode());
        custCountryComboBox.getSelectionModel().select(cust.getCustomerCountry());
        custCountryDivisionComboBox.getItems().setAll(getAllDivisionsByCountryID(cust.getCustomerCountryID()));
        custCountryDivisionComboBox.getSelectionModel().select(cust.getCustomerCountryDivision());
    }

    public void clearTextFields() {
        custIDTextField.clear();
        custNameTextField.clear();
        custPhoneTextField.clear();
        custAddressTextField.clear();
        custPostalCodeTextField.clear();

        EventHandler<ActionEvent> handler = custCountryComboBox.getOnAction();
        custCountryComboBox.setOnAction(null);
        custCountryComboBox.setValue(null);
        custCountryComboBox.setPromptText("Select country");
        custCountryComboBox.setOnAction(handler);

        custCountryDivisionComboBox.setValue(null);
        custCountryDivisionComboBox.getItems().clear();
        custCountryDivisionComboBox.setPromptText("Select location");
    }

    public String defaultErrorLabelText() {
        return "Select a customer and click Select Customer.\nOr enter customer info and click Add Customer.";
    }

    public void disableUpdateDeleteButtons() {
        customerAddButton.setDisable(false);
        customerUpdateButton.setDisable(true);
        customerDeleteButton.setDisable(true);
    }

    public void disableAddButton() {
        customerAddButton.setDisable(true);
        customerUpdateButton.setDisable(false);
        customerDeleteButton.setDisable(false);
    }

    public void onApptViewButton(ActionEvent event) throws IOException {
        System.out.println("CustomerView - Add Appt Cancel button clicked");
        Login.showApptView();
        Stage stage = (Stage) apptViewButton.getScene().getWindow();
        stage.close();
    }

    public void setCustCountryComboBox() {
        try {
            custCountryComboBox.setItems(getAllCountries());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onCustCountryComboBox() {
        Country loc = new Country();
        try {
            loc = (Country) custCountryComboBox.getSelectionModel().getSelectedItem();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(loc + " selected in countryComboBox, loc.getCountryID = " + loc.getCountryID());
        try {
            custCountryDivisionComboBox.getItems().setAll(getAllDivisionsByCountryID(loc.getCountryID()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onCustomerAddButton() throws Exception {
        System.out.println("CustomerView - add customer button clicked");
        errorLabel.setText("");
        boolean addConfirm = true;

        String name = custNameTextField.getText();
        if (name == null || custNameTextField.getText().trim().isEmpty()) {
            errorLabel.setText(errorLabel.getText() + "Enter a name. ");
            addConfirm = false;
        }

        String phone = custPhoneTextField.getText();
        if (phone == null || custPhoneTextField.getText().trim().isEmpty()) {
            errorLabel.setText(errorLabel.getText() + "Enter a phone number. ");
            addConfirm = false;
        }

        String address = custAddressTextField.getText();
        if (address == null || custAddressTextField.getText().trim().isEmpty()) {
            errorLabel.setText(errorLabel.getText() + "Enter an address. ");
            addConfirm = false;
        }

        String postalCode = custPostalCodeTextField.getText();
        if (postalCode == null || custPostalCodeTextField.getText().trim().isEmpty()) {
            errorLabel.setText(errorLabel.getText() + "Enter a postal code. ");
            addConfirm = false;
        }

        //need to get logged in user and pass as last_update_user
        CountryDivision div = (CountryDivision) custCountryDivisionComboBox.getSelectionModel().getSelectedItem();
        if (div == null) {
            addConfirm = false;
            errorLabel.setText(errorLabel.getText() + "Please select a location. ");
        }

        if (addConfirm) {
            addCustomer(name, address, postalCode, phone, div);
            errorLabel.setText("New customer added.\n \n" + defaultErrorLabelText());
            customerTableView.getItems().setAll(getAllCustomers());
            clearTextFields();
        }
    }

    public void onCustomerUpdateButton() throws Exception {
        System.out.println("CustomerView - update customer button clicked");
        int id = Integer.parseInt(custIDTextField.getText());
        String name = custNameTextField.getText();
        String address = custAddressTextField.getText();
        String postalCode = custPostalCodeTextField.getText();
        String phone = custPhoneTextField.getText();
        //need to get logged in user and pass as last_update_user
        CountryDivision div = (CountryDivision) custCountryDivisionComboBox.getSelectionModel().getSelectedItem();

        updateCustomer(id, name, address, postalCode, phone, div);
        errorLabel.setText(name + " customer updated");
        customerTableView.getItems().setAll(getAllCustomers());
        clearTextFields();
        disableUpdateDeleteButtons();
    }

    public void onCustomerDeleteButton(ActionEvent event) throws Exception {
        System.out.println("CustomerView - delete customer button clicked");
        int id = Integer.parseInt(custIDTextField.getText());
        deleteCustomer(id);
        errorLabel.setText(deleteCustomer(id));
        customerTableView.getItems().setAll(getAllCustomers());
        clearTextFields();
        disableUpdateDeleteButtons();
    }

    public void onClearSelectionButton(ActionEvent event) {
        System.out.println("CustomerView - clear selection button clicked");
        customerTableView.getSelectionModel().clearSelection();
        clearTextFields();
        disableUpdateDeleteButtons();
        errorLabel.setText(defaultErrorLabelText());
    }

    public void onEditSelectionButton() {
        try {
            Customer cust = (Customer) customerTableView.getSelectionModel().getSelectedItem();
            populateTextFields(cust);
            disableAddButton();
            errorLabel.setText("Update customer info above and click Update Customer.\nOr click Delete Customer below.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
