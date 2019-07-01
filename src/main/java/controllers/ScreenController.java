package controllers;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import views.StartGUI;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;


public class ScreenController {
    private Stage actualStage;
    private static ScreenController instance;
    private Scene actualScene;

    public void setActualScene(Scene actualScene) {
        this.actualScene = actualScene;
    }

    public Scene getActualScene() {
        return actualScene;
    }

    public Stage getActualStage() {
        return actualStage;
    }

    private ScreenController(Stage main) {
        this.actualStage = main;
    }

    public static ScreenController getInstance(Stage main) {
        if (instance==null) {
            instance = new ScreenController(main);
        }
        return instance;
    }

    public static ScreenController getInstance() {
        return instance;
    }

    public void activate(String name) {
        Platform.runLater( () -> {
                    actualStage.setTitle(name); //Set the title you like most
                    Parent newParent = null;
                    try {
                        URL url = getClass().getClassLoader().getResource("fxml/" + name);
                        newParent = FXMLLoader.load(url);
                    } catch (IOException e) {
                        Logger.getAnonymousLogger().info(e.toString());
                    }
                    Scene newParentScene = new Scene(newParent);
                    actualStage.setScene(newParentScene);
                    actualStage.setResizable(true);
                }
            );
    }
}
