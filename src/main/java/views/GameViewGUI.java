package views;

import controllers.GameController;
import controllers.ScreenController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import models.card.PowerUpCard;
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

    @FXML
    private Button btnDrawPowerUp;
    @FXML
    private Button btnGrabWeapon;
    @FXML
    private Button btnSpawn;
    @FXML
    private Button btnRun;
    @FXML
    private Button btnGrabAmmo;
    @FXML
    private Button btnShoot;
    @FXML
    private Button btnReload;
    @FXML
    private Button btnUsePowerUp;

    @FXML
    private ImageView imgYourWeaponCard1;
    @FXML
    private ImageView imgYourWeaponCard2;
    @FXML
    private ImageView imgYourWeaponCard3;
    @FXML
    private ImageView imgYourWeaponCard4;
    @FXML
    private ImageView imgYourPowerUpCard1;
    @FXML
    private ImageView imgYourPowerUpCard2;
    @FXML
    private ImageView imgYourPowerUpCard3;
    @FXML
    private ImageView imgYourPowerUpCard4;

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

    public void drawPowerUp() {
        setBtnDrawPowerUpVisibility(false);
        gameController.drawPowerUp();
        gameController.getValidActions();
    }

    public void addPowerUpToHand(PowerUpCard card) {
        //TODO: Set image in imageview, on the first space free
    }
    public void addWeaponToHand(PowerUpCard card) {
        //TODO: Set image in imageview, on the first space free
    }

    public void grabWeapon() {

    }
    public void spawn() {

    }
    public void run() {

    }
    public void grabAmmo() {

    }
    public void shoot() {

    }
    public void reload() {

    }
    public void usePowerUp() {

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
        //TODO: Delete this and use spawn()
    }
    @Override
    public void setBtnDrawPowerUpVisibility(boolean isVisible) {
        btnDrawPowerUp.setDisable(!isVisible);
    }
    @Override
    public void setBtnGrabWeapon(boolean isVisible) {
        btnGrabWeapon.setDisable(!isVisible);
    }
    @Override
    public void setBtnSpawn(boolean isVisible) {
        btnSpawn.setDisable(!isVisible);
    }
    @Override
    public void setBtnRun(boolean isVisible) {
        btnRun.setDisable(!isVisible);
    }
    @Override
    public void setBtnGrabAmmo(boolean isVisible) {
        btnGrabAmmo.setDisable(!isVisible);
    }
    @Override
    public void setBtnShoot(boolean isVisible) {
        btnShoot.setDisable(!isVisible);
    }
    @Override
    public void setBtnReload(boolean isVisible) {
        btnReload.setDisable(!isVisible);
    }
    @Override
    public void setBtnUsePowerUp(boolean isVisible) {
        btnUsePowerUp.setDisable(!isVisible);
    }


}
