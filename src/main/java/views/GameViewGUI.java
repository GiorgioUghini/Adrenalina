package views;

import controllers.GameController;
import controllers.ResourceController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class GameViewGUI implements Initializable, GameView {
    @FXML
    private GridPane mainGridPane;

    private GameController gameController;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File lolo = ResourceController.getResource("img/map-1.png");
        mainGridPane.setStyle("-fx-background-image: url('img/map-1.png');");
        System.out.println("OK");
    }





    @Override
    public void printError(String error) {

    }

    @Override
    public void showMessage(String message) {

    }
}
