package views;

import controllers.GameController;
import controllers.ResourceController;
import controllers.ScreenController;
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
        mainGridPane.setStyle("-fx-background-image: url('img/map-1.png'); -fx-background-repeat: stretch; -fx-background-size: stretch; -fx-background-position: center center;");
        ScreenController.getInstance().getActualStage().setFullScreen(true);
    }





    @Override
    public void printError(String error) {

    }

    @Override
    public void showMessage(String message) {

    }
}
