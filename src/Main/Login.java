package Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Login implements Initializable {
    public TextField usernameTextField;
    public TextField passwordTextField;
    public Button loginButton;
    public Label errorLabel;
    public Label userLocation;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Initialized");


    }

    public void onLoginButton() {

    }

    public void onLoginButton(ActionEvent event) throws IOException {
        DB.makeConnection();
        String username = usernameTextField.getText();
        System.out.println(username);
        String password = passwordTextField.getText();
        System.out.println(password);
        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("logged in as " + username);
            errorLabel.setOpacity(1.0);
            errorLabel.setText("Successful login \n Welcome " + username);
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();

            showApptView();
        } else if (username.equals("chris")) {
            System.out.println("missing username or password");
            errorLabel.setOpacity(1.0);
            errorLabel.setText("Please enter both a username and password");
        } else {
            System.out.println("Incorrect username or password");
            errorLabel.setOpacity(1.0);
            errorLabel.setText("Incorrect username or password");
            }
        }

        public static void showApptView() throws IOException {
            Parent root = FXMLLoader.load(Login.class.getResource("/Appointment/AppointmentView.fxml"));
            Stage apptView = new Stage();
            apptView.setTitle("Sequeira Scheduler - WGU C195 PA Task");
            apptView.setScene(new Scene(root));
            apptView.show();
        }

    }
