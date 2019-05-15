package views;

import controllers.ScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class StartGUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    /*If you plan on going forward to a different place in the flow of your program (login -> profile, for example) - change the Stage.
    If you are in the same environment (login for the first time -> login after multiple wrong tries) - change the Scene.
    */

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Adrenaline La prova finale evento");

        FXMLLoader loader = new FXMLLoader();

        BorderPane root = loader.load(getClass().getClassLoader().getResource("./Form1.fxml"));
        Scene scene = new Scene(root);

        ScreenController screenController = ScreenController.getInstance(scene);
        screenController.setActualStage(primaryStage);
        screenController.addScreen("registration", FXMLLoader.load(getClass().getClassLoader().getResource("./Form1.fxml")));
        //screenController.addScreen("lobby", FXMLLoader.load(getClass().getResource( "./Lobby.fxml" )));
        //screenController.activate("registration");

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }
}