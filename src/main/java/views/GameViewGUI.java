package views;

import controllers.GameController;
import controllers.ScreenController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import network.Client;

import java.net.URL;
import java.util.ResourceBundle;

public class GameViewGUI implements Initializable, GameView {

    @FXML
    private GridPane mainGridPane;
    @FXML
    private Text waitMapLabel;
    @FXML
    private ListView<String> gameStatusListView;

    private GameController gameController;

    public GameViewGUI() {
        this.gameController = new GameController();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Client.getInstance().setCurrentView(this);
        mainGridPane.setStyle(String.format("-fx-background-image: url('img/map-%d.png'); -fx-background-repeat: stretch; -fx-background-size: stretch; -fx-background-position: center center;", Client.getInstance().getMapNum() + 1));
        ScreenController.getInstance().getActualStage().setFullScreen(true);
        Platform.runLater(this::getValidActions);
    }

    public void getValidActions() {
        gameController.getValidActions();
    }

    public void ciao() {
        gameController.drawPowerUp();
    }


    @Override
    public void printError(String error) {

    }

    @Override
    public void showMessage(String message) {
        gameStatusListView.getItems().add(message);
    }

    @Override
    public void chooseSpawnPoint() {
        //TODO
    }
}
