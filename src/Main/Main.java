package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Christopher Sequeira
 * @version 1.95
 *
 * This program is for the C195 course at WGU. It is an appointment program.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../Login/Login.fxml"));
        primaryStage.setTitle("Login - Sequeira Scheduler - WGU C195 PA Task");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
