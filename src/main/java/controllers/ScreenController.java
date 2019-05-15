package controllers;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.HashMap;

public class ScreenController {
    private HashMap<String, Pane> screenMap = new HashMap<>();
    private Scene main;
    private static ScreenController instance;
    private Stage actualStage;

    public void setActualStage(Stage actualStage) {
        this.actualStage = actualStage;
    }

    public Stage getActualStage() {
        return actualStage;
    }

    private ScreenController(Scene main) {
        this.main = main;
    }

    public static ScreenController getInstance(Scene main) {
        if (instance==null) {
            instance = new ScreenController(main);
        }
        return instance;
    }

    public static ScreenController getInstance() {
        return instance;
    }

    public void addScreen(String name, Pane pane){
        screenMap.put(name, pane);
    }

    public void removeScreen(String name){
        screenMap.remove(name);
    }

    public void activate(String name){
        main.setRoot( screenMap.get(name) );
    }
}
