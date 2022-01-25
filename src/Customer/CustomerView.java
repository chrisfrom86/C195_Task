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

/**
 * @author Chris Sequeira
 * The CustomerView class utilizes the CustomerView.fxml to display Customer information from the customers table.
 */
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

    /**
     * this method populates the columns, and then populates the rows. it also populates the Country ComboBox with countries from the database.
     * @param url
     * @param resourceBundle
     */
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

    /**
     * this method populates the TableView with all customers, and is called any time the table needs to be updated.
     */
    public void setCustomerTableView() {
        try {
            customerTableView.setItems(getAllCustomers());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called whenever a customer is selected using the Select Customer button.
     * @param cust
     * @throws Exception
     */
    public void populateTextFields(Customer cust) throws Exception {
        custIDTextField.setText(cust.customerID.getValue().toString());
        custNameTextField.setText(cust.getCustomerName());
        custPhoneTextField.setText(cust.getCustomerPhone());
        custAddressTextField.setText(cust.getCustomerAddress());
        custPostalCodeTextField.setText(cust.getCustomerPostalCode());
        custCountryComboBox.getSelectionModel().select(cust.getCustomerCountry());
        custCountryDivisionComboBox.getItems().setAll(getAllDivisionsByCountryID(cust.getCustomerCountryID()));
        custCountryDivisionComboBox.getSelectionModel().select(cust.getCustomerCountryDivision());
    }

    /**
     * this method clears all the text fields in the stage.
     */
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

    /**
     * this method provides the instructions for the form. it is called whenever there is nothing else in the errorLabel so the user knows how to use the form.
     * @return
     */
    public String defaultErrorLabelText() {
        return "Select a customer and click Select Customer.\nOr enter customer info and click Add Customer.";
    }

    /**
     * This method disables the update/delete buttons. it is called if no customer is selected, so that users cannot accidentally try to update/delete a null selection.
     */
    public void disableUpdateDeleteButtons() {
        customerAddButton.setDisable(false);
        customerUpdateButton.setDisable(true);
        customerDeleteButton.setDisable(true);
    }

    /**
     * this method disables the add button. it is called when a customer is selected, so that a customer is not accidentally duplicated after being selected.
     */
    public void disableAddButton() {
        customerAddButton.setDisable(true);
        customerUpdateButton.setDisable(false);
        customerDeleteButton.setDisable(false);
    }

    /**
     * this method closes the current stage and opens the AppointmentView form.
     * @param event
     * @throws IOException
     */
    public void onApptViewButton(ActionEvent event) throws IOException {
        System.out.println("CustomerView - Add Appt Cancel button clicked");
        Login.showApptView();
        Stage stage = (Stage) apptViewButton.getScene().getWindow();
        stage.close();
    }

    /**
     * this method populates all countries from the database into the country ComboBox.
     */
    public void setCustCountryComboBox() {
        try {
            custCountryComboBox.setItems(getAllCountries());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * this method populates all first level divisions from the database based on the countryComboBox selection.
     */
    public void onCustCountryComboBox() {
        Country loc = new Country();
        try {
            loc = (Country) custCountryComboBox.getSelectionModel().getSelectedItem();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            custCountryDivisionComboBox.getItems().setAll(getAllDivisionsByCountryID(loc.getCountryID()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * this method validates the input is not null or empty and then adds the validated customer info to the database by calling addCustomer from {@link CustomerDAO}.
     * @throws Exception
     */
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

    /**
     * this method validates the input from the form and passes the information to updateCustomer from {@link CustomerDAO}.
     * @throws Exception
     */
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

    /**
     * this method gets the selected customer from the form and passes the ID to deleteCustomer from {@link CustomerDAO}.
     * @throws Exception
     */
    public void onCustomerDeleteButton(ActionEvent event) throws Exception {
        System.out.println("CustomerView - delete customer button clicked");
        int id = Integer.parseInt(custIDTextField.getText());
        deleteCustomer(id);
        errorLabel.setText(deleteCustomer(id));
        customerTableView.getItems().setAll(getAllCustomers());
        clearTextFields();
        disableUpdateDeleteButtons();
    }

    /**
     * This method clears all TextFields in the form
     * @param event
     */
    public void onClearSelectionButton(ActionEvent event) {
        System.out.println("CustomerView - clear selection button clicked");
        customerTableView.getSelectionModel().clearSelection();
        clearTextFields();
        disableUpdateDeleteButtons();
        errorLabel.setText(defaultErrorLabelText());
    }

    /**
     * this method pulls all info from the row selected in the TableView and populates the TextFields with the selected Customer's information.
     */
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
