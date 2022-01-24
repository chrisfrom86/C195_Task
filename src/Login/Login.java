package Login;

import Main.DB;
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
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

import static Login.LoginDAO.*;

public class Login implements Initializable {
    public TextField usernameTextField;
    public TextField passwordTextField;
    public Button loginButton;
    public Label errorLabel;
    public Label userLocaleLabel;
    public static int loggedInUserID;
    public Label usernameLabel;
    public Label passwordLabel;
    public Label loginTimeZoneLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Login - Initialized");
        usernameTextField.setText("test");
        passwordTextField.setText("test");
        loginTimeZoneLabel.setText("Time Zone: " + TimeZone.getDefault().getDisplayName());

        Locale locale = new Locale("es", "ES");
        ResourceBundle rb = ResourceBundle.getBundle("Login/Locale_es", locale);

        if (Locale.getDefault().getLanguage().equals("es")) {
            loginButton.setText(rb.getString("Login"));
            usernameLabel.setText(rb.getString("Username"));
            passwordLabel.setText(rb.getString("Password"));
            usernameTextField.setPromptText(rb.getString("promptUN"));
            passwordTextField.setPromptText(rb.getString("promptPW"));
            errorLabel.setText(rb.getString("Guide"));
            userLocaleLabel.setText(rb.getString("Language"));
            loginTimeZoneLabel.setText(rb.getString("TimeZone") + TimeZone.getDefault().getDisplayName());
        }
    }

    public void onLoginButton() throws IOException, SQLException {
        DB.makeConnection();
        ResourceBundle rb = null;
        if (Locale.getDefault().getLanguage().equals("es")) {
            Locale locale = new Locale("es", "ES");
            rb = ResourceBundle.getBundle("Login/Locale_es", locale);
        } else {
            rb = ResourceBundle.getBundle("Login/Locale", Locale.ENGLISH);
        }
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        if (tryLogin(username, password)) {
            System.out.println("Login - logged in as " + username);
            errorLabel.setText("Login - Successful login \n Welcome " + username);
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();

            showApptView();
            showApptWarning();
        } else {
            errorLabel.setText(rb.getString("incorrectLoginInfo"));
        }
        if (usernameTextField.getText().isEmpty()) {
            System.out.println("Login - missing username");
            errorLabel.setText(errorLabel.getText() + rb.getString("noUsername"));
        } if (passwordTextField.getText().isEmpty()) {
            System.out.println("Login - missing password");
            errorLabel.setText(errorLabel.getText() + rb.getString("noPassword"));
            }
        }

    public static void showApptView() throws IOException {
            Parent root = FXMLLoader.load(Login.class.getResource("/Appointment/AppointmentView.fxml"));
            Stage apptView = new Stage();
            apptView.setTitle("Appointment Viewer - Sequeira Scheduler - WGU C195 PA Task");
            apptView.setScene(new Scene(root));
            apptView.show();
        }

    public static void showApptWarning() throws IOException {
        Parent root = FXMLLoader.load(Login.class.getResource("/Appointment/ApptWarning.fxml"));
        Stage apptWarning = new Stage();
        apptWarning.setTitle("Appointment Warning - Sequeira Scheduler - WGU C195 PA Task");
        apptWarning.setScene(new Scene(root));
        apptWarning.show();
    }
    }
