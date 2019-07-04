package controllers;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;


public class ScreenController {
    private static ScreenController instance;
    private Stage actualStage;
    private Scene actualScene;

    private ScreenController(Stage main) {
        this.actualStage = main;
    }

    public static ScreenController getInstance(Stage main) {
        if (instance == null) {
            instance = new ScreenController(main);
        }
        return instance;
    }

    public static ScreenController getInstance() {
        return instance;
    }

    /**
     * returns the actual screen scene
     *
     * @return
     */
    public Scene getActualScene() {
        return actualScene;
    }

    /**
     * sets the actual screen scene
     *
     * @param actualScene
     */
    public void setActualScene(Scene actualScene) {
        this.actualScene = actualScene;
    }

    public Stage getActualStage() {
        return actualStage;
    }

    /**
     * activates the selected scene
     *
     * @param name the name of the scene, as is in resources/fxml
     */
    public void activate(String name) {
        Platform.runLater(() -> {
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
