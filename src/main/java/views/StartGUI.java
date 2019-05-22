package views;

import controllers.ScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class StartGUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    /*If you plan on going forward to a different place in the flow of your program (login -> profile, for example) - change the Stage.
    If you are in the same environment (login for the first time -> login after multiple wrong tries) - change the Scene.
    */

    /*@Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Adrenaline La prova finale evento");

        FXMLLoader loaderForm1 = new FXMLLoader();

        BorderPane root = loaderForm1.load(getClass().getResource("./Form1.fxml"));
        Scene scene = new Scene(root);

        ScreenController screenController = ScreenController.getInstance(scene);
        screenController.setActualStage(primaryStage);
        screenController.addScreen("registration", FXMLLoader.load(getClass().getClassLoader().getResource("./Form1.fxml")));
        BorderPane root1 = loaderForm1.load(getClass().getClassLoader().getResource("./WaitingRoom.fxml"));
        screenController.addScreen("waitingroom", FXMLLoader.load(getClass().getClassLoader().getResource( "./WaitingRoom.fxml")));
        //screenController.activate("registration");

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }*/

    @Override
    public void start(Stage primaryStage) throws IOException {
        URL url = getClass().getClassLoader().getResource("fxml/Form1.fxml");
        Parent register = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/Form1.fxml"));
        primaryStage.setTitle("Adrenaline La prova finale evento");
        Scene registerScene = new Scene(register);
        primaryStage.setScene(registerScene);
        primaryStage.setResizable(false);

        ScreenController screenController = ScreenController.getInstance(primaryStage);
        screenController.setActualScene(registerScene);

        primaryStage.show();
    }
}