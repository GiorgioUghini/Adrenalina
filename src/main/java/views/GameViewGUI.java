package views;

import controllers.GameController;
import controllers.ResourceController;
import controllers.ScreenController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import models.card.PowerUpCard;
import models.card.WeaponCard;
import models.map.Square;
import models.turn.ActionGroup;
import models.turn.ActionType;
import models.turn.TurnEvent;
import network.Client;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    private Button btnActionGroup1;
    @FXML
    private Button btnActionGroup2;
    @FXML
    private Button btnActionGroup3;
    @FXML
    private Button btnEndTurn;

    @FXML
    private GridPane grid00;


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

    private ArrayList<ImageView> powerUpSpaces = new ArrayList<>();
    private ArrayList<ImageView> weaponSpaces = new ArrayList<>();
    private ArrayList<PowerUpCard> myPowerUpsCardOrdered = new ArrayList<>();
    private ArrayList<PowerUpCard> myWeaponsCardOrdered = new ArrayList<>();

    private HashMap<Integer, ActionType> buttonActionTypeMap = new HashMap<>();

    private boolean canClickOnPowerUps = false;

    private GameController gameController;

    public GameViewGUI() {
        this.gameController = new GameController();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Client.getInstance().setCurrentView(this);
        mainGridPane.setStyle(String.format("-fx-background-image: url('maps/map-%d.png'); -fx-background-repeat: stretch; -fx-background-size: stretch; -fx-background-position: center center;", Client.getInstance().getMapNum() + 1));
        ScreenController.getInstance().getActualStage().setFullScreen(true);
        powerUpSpaces.add(imgYourPowerUpCard1);
        powerUpSpaces.add(imgYourPowerUpCard2);
        powerUpSpaces.add(imgYourPowerUpCard3);
        powerUpSpaces.add(imgYourPowerUpCard4);
        weaponSpaces.add(imgYourWeaponCard1);
        weaponSpaces.add(imgYourWeaponCard2);
        weaponSpaces.add(imgYourWeaponCard3);
        weaponSpaces.add(imgYourWeaponCard4);
        Platform.runLater(this::getValidActions);
    }

    @Override
    public void getValidActions() {
        gameController.getValidActions();
    }



    public void addPowerUpToHand(PowerUpCard card) {
        int i = 0;
        Image img = new Image(ResourceController.getResource("powerupcards/" + card.image).toURI().toString());
        while (powerUpSpaces.get(i).getImage() != null) {
            i++;
        }
        if (i>3) return;
        powerUpSpaces.get(i).setImage(img);
        Client.getInstance().getPlayer().getPowerUpList().add(card);
    }

    public void removePowerUpToHand(PowerUpCard card) {
        int i = 0;
        while (Client.getInstance().getPlayer().getPowerUpList().get(i) != card) {
            i++;
        }
        Client.getInstance().getPlayer().getPowerUpList().remove(card);

        for(;i<3;i++) {
            powerUpSpaces.get(i).setImage(powerUpSpaces.get(i+1).getImage());
        }
        powerUpSpaces.get(3).setImage(null);
    }

    public void addWeaponToHand(WeaponCard card) {
        //TODO: Set image in imageview, on the first space free
    }

    public void drawPowerUp() {
        if(Client.getInstance().getActions().get(Client.getInstance().getCurrentActionType()).stream().filter(t -> t == TurnEvent.DRAW).count() <= 1){
            setBtnDrawPowerUpVisibility(false);
            setBtnSpawnVisibility(true);
        }
        gameController.drawPowerUp();
        gameController.getValidActions();
    }
    public void grabWeapon() {

    }
    public void spawn() {
        setBtnSpawnVisibility(false);
        showMessage("Please click on the power up card you wish to DISCARD and spawn accordingly.");
        canClickOnPowerUps = true;
    }
    public void run() {
        Square square = null; //TODO get square
        Client.getInstance().getConnection().run(Client.getInstance().getActions().get(Client.getInstance().getCurrentActionType()).get(0), square);
        setBtnRunVisibility(false);
        gameController.getValidActions();
    }
    public void grabAmmo() {

    }
    public void shoot() {

    }
    public void reload() {

    }
    public void usePowerUp() {

    }

    public void endTurn() {
        gameController.endTurn();
    }

    @Override
    public void startTurn(String playerName) {
        if (Client.getInstance().getPlayer().getName().equals(playerName)) {
            gameController.getValidActions();
        }
    }

    @Override
    public void printError(String error) {

    }

    @Override
    public void showMessage(String message) {
        if (message.equals("\n")) return;
        Platform.runLater( () -> {
            gameStatusListView.getItems().add(message);
        });
    }

    @Override
    public void chooseSpawnPoint() {
        //TODO: Delete this and use spawn()
    }

    @Override
    public void setBtnDrawPowerUpVisibility(boolean isVisible) {
        Platform.runLater( () -> { btnDrawPowerUp.setDisable(!isVisible); });
    }
    @Override
    public void setBtnGrabWeaponVisibility(boolean isVisible) {
        Platform.runLater( () -> { btnGrabWeapon.setDisable(!isVisible); });
    }
    @Override
    public void setBtnSpawnVisibility(boolean isVisible) {
        Platform.runLater( () -> { btnSpawn.setDisable(!isVisible); });
    }
    @Override
    public void setBtnRunVisibility(boolean isVisible) {
        Platform.runLater( () -> { btnRun.setDisable(!isVisible); });
    }
    @Override
    public void setBtnGrabAmmoVisibility(boolean isVisible) {
        Platform.runLater( () -> { btnGrabAmmo.setDisable(!isVisible); });
    }
    @Override
    public void setBtnShootVisibility(boolean isVisible) {
        Platform.runLater( () -> { btnShoot.setDisable(!isVisible); });
    }
    @Override
    public void setBtnReloadVisibility(boolean isVisible) {
        Platform.runLater( () -> { btnReload.setDisable(!isVisible); });
    }
    @Override
    public void setBtnUsePowerUpVisibility(boolean isVisible) {
        Platform.runLater( () -> { btnUsePowerUp.setDisable(!isVisible); });
    }

    private void addOnPane(GridPane pane, Node node) {
        switch(pane.getChildren().size()) {
            case 0:
                pane.add(node,1,2);
                break;
            case 1:
                pane.add(node,2,2);
                break;
            case 2:
                pane.add(node,1,3);
                break;
            case 3:
                pane.add(node,2,3);
                break;
            case 4:
                pane.add(node,1,1);
                break;
            default:
                pane.add(node,2,1);
                break;
        }
    }

    public void drawPlayerToken() {
        Circle circle = new Circle(0.0d,0.0d,17.0d);
        circle.setFill(Color.RED);
        addOnPane(grid00, circle);
    }

    public void powerUp1Clicked() {
        if (canClickOnPowerUps && (!Client.getInstance().getPlayer().getPowerUpList().isEmpty())) {
            canClickOnPowerUps = false;
            gameController.spawn(Client.getInstance().getPlayer().getPowerUpList().get(0));
            removePowerUpToHand(Client.getInstance().getPlayer().getPowerUpList().get(0));
        }
    }
    public void powerUp2Clicked() {
        if (canClickOnPowerUps && (Client.getInstance().getPlayer().getPowerUpList().size() > 1)) {
            canClickOnPowerUps = false;
            gameController.spawn(Client.getInstance().getPlayer().getPowerUpList().get(1));
            removePowerUpToHand(Client.getInstance().getPlayer().getPowerUpList().get(1));
        }
    }
    public void powerUp3Clicked() {
        if (canClickOnPowerUps && (Client.getInstance().getPlayer().getPowerUpList().size() > 2)) {
            canClickOnPowerUps = false;
            gameController.spawn(Client.getInstance().getPlayer().getPowerUpList().get(2));
            removePowerUpToHand(Client.getInstance().getPlayer().getPowerUpList().get(2));
        }
    }
    public void powerUp4Clicked() {
        if (canClickOnPowerUps && (Client.getInstance().getPlayer().getPowerUpList().size() > 3)) {
            canClickOnPowerUps = false;
            gameController.spawn(Client.getInstance().getPlayer().getPowerUpList().get(3));
            removePowerUpToHand(Client.getInstance().getPlayer().getPowerUpList().get(3));
        }
    }

    public void btnActionGroup1Clicked() {
        Platform.runLater( () -> {
            btnActionGroup1.setVisible(false);
        });
        ActionType tipo = buttonActionTypeMap.get(1);
        Client.getInstance().setCurrentActionType(tipo);
        Client.getInstance().getConnection().action(Client.getInstance().getCurrentActionType());
    }
    public void btnActionGroup2Clicked() {
        Platform.runLater( () -> {
            btnActionGroup2.setVisible(false);
        });
        ActionType tipo = buttonActionTypeMap.get(2);
        Client.getInstance().setCurrentActionType(tipo);
        Client.getInstance().getConnection().action(Client.getInstance().getCurrentActionType());
    }
    public void btnActionGroup3Clicked() {
        Platform.runLater( () -> {
            btnActionGroup3.setVisible(false);
        });
        ActionType tipo = buttonActionTypeMap.get(3);
        Client.getInstance().setCurrentActionType(tipo);
        Client.getInstance().getConnection().action(Client.getInstance().getCurrentActionType());
    }

    @Override
    public void setTextAndEnableBtnActionGroup(ActionType actionType, int btnNum) {
        Platform.runLater( () -> {
            switch (btnNum) {
                case 1:
                    btnActionGroup1.setVisible(true);
                    btnActionGroup1.setText(actionType.name());
                    buttonActionTypeMap.put(1, actionType);
                    break;
                case 2:
                    btnActionGroup2.setVisible(true);
                    btnActionGroup2.setText(actionType.name());
                    buttonActionTypeMap.put(2, actionType);
                    break;
                case 3:
                    btnActionGroup3.setVisible(true);
                    btnActionGroup3.setText(actionType.name());
                    buttonActionTypeMap.put(3, actionType);
                    break;
            }
        });
    }

    public void setEnabledBtnEndTurn(boolean enable) {
        btnEndTurn.setDisable(!enable);
    }


}
