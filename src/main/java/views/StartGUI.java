package views;

import controllers.ScreenController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartGUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }   //DO NOT DIRECTLY START MAIN, PLEASE START StartClient!

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent register = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/WelcomeView.fxml"));
        primaryStage.setTitle("Adrenaline La prova finale evento");
        Scene registerScene = new Scene(register);
        primaryStage.setScene(registerScene);
        primaryStage.setResizable(false);

        ScreenController screenController = ScreenController.getInstance(primaryStage);
        screenController.setActualScene(registerScene);

        primaryStage.show();
    }

    @Override
    public void stop() {
        Platform.exit();
        System.exit(0);
    }
}