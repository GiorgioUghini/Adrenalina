package views;

import controllers.GameController;
import controllers.ResourceController;
import controllers.ScreenController;
import errors.PlayerNotOnMapException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import models.card.*;
import models.map.*;
import models.player.Ammo;
import models.player.Player;
import models.turn.ActionType;
import models.turn.TurnEvent;
import network.Client;

import java.net.URL;
import java.util.*;

public class GameViewGUI implements Initializable, GameView {

    @FXML
    private GridPane mainGridPane;
    @FXML
    private ListView<String> gameStatusListView;

    @FXML
    private Button btnDrawPowerUp;
    @FXML
    private Button btnSpawn;
    @FXML
    private Button btnRun;
    @FXML
    private Button btnGrab;
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

    private List<Button> turnEventButtons;

    @FXML
    private GridPane grid00;
    @FXML
    private GridPane grid01;
    @FXML
    private GridPane grid02;
    @FXML
    private GridPane grid10;
    @FXML
    private GridPane grid11;
    @FXML
    private GridPane grid12;
    @FXML
    private GridPane grid20;
    @FXML
    private GridPane grid21;
    @FXML
    private GridPane grid22;
    @FXML
    private GridPane grid30;
    @FXML
    private GridPane grid31;
    @FXML
    private GridPane grid32;

    private ArrayList<ArrayList<GridPane>> paneList = new ArrayList<>();


    @FXML
    private ImageView imgYourWeaponCard1;
    @FXML
    private ImageView imgYourWeaponCard2;
    @FXML
    private ImageView imgYourWeaponCard3;
    @FXML
    private ImageView imgYourPowerUpCard1;
    @FXML
    private ImageView imgYourPowerUpCard2;
    @FXML
    private ImageView imgYourPowerUpCard3;
    @FXML
    private ImageView imgYourPowerUpCard4;
    @FXML
    private ImageView weaponSPBlue1;
    @FXML
    private ImageView weaponSPBlue2;
    @FXML
    private ImageView weaponSPBlue3;
    @FXML
    private ImageView weaponSPRed1;
    @FXML
    private ImageView weaponSPRed2;
    @FXML
    private ImageView weaponSPRed3;
    @FXML
    private ImageView weaponSPYellow1;
    @FXML
    private ImageView weaponSPYellow2;
    @FXML
    private ImageView weaponSPYellow3;

    @FXML
    private Text redAmmoText;
    @FXML
    private Text blueAmmoText;
    @FXML
    private Text yellowAmmoText;

    private ArrayList<ImageView> powerUpSpaces = new ArrayList<>();
    private ArrayList<ImageView> weaponSpaces = new ArrayList<>();
    private HashMap<RoomColor, List<ImageView>> weaponOnSpawnPointMap = new HashMap<>();

    private HashMap<Integer, ActionType> buttonActionTypeMap = new HashMap<>();

    private WeaponCard toDiscard = null;

    private boolean canClickOnPowerUps = false;
    private boolean canDiscardHisOwnWeapons = false;
    private boolean canChooseSpawnPointWeapon = false;

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

        this.turnEventButtons = new ArrayList<>(Arrays.asList(btnDrawPowerUp, btnGrab, btnSpawn, btnRun, btnShoot, btnReload, btnUsePowerUp));

        ArrayList<GridPane> x0 = new ArrayList<>();
        x0.add(grid00);
        x0.add(grid01);
        x0.add(grid02);
        ArrayList<GridPane> x1 = new ArrayList<>();
        x1.add(grid10);
        x1.add(grid11);
        x1.add(grid12);
        ArrayList<GridPane> x2 = new ArrayList<>();
        x2.add(grid20);
        x2.add(grid21);
        x2.add(grid22);
        ArrayList<GridPane> x3 = new ArrayList<>();
        x3.add(grid30);
        x3.add(grid31);
        x3.add(grid32);
        paneList.add(x0);
        paneList.add(x1);
        paneList.add(x2);
        paneList.add(x3);

        ArrayList<ImageView> redSPImages = new ArrayList<ImageView>() {
            {
                add(weaponSPRed1);
                add(weaponSPRed2);
                add(weaponSPRed3);
            }
        };
        ArrayList<ImageView> blueSPImages = new ArrayList<ImageView>() {
            {
                add(weaponSPBlue1);
                add(weaponSPBlue2);
                add(weaponSPBlue3);
            }
        };
        ArrayList<ImageView> yellowSPImages = new ArrayList<ImageView>() {
            {
                add(weaponSPYellow1);
                add(weaponSPYellow2);
                add(weaponSPYellow3);
            }
        };
        weaponOnSpawnPointMap.put(RoomColor.RED, redSPImages);
        weaponOnSpawnPointMap.put(RoomColor.BLUE, blueSPImages);
        weaponOnSpawnPointMap.put(RoomColor.YELLOW, yellowSPImages);

        if(Client.getInstance().isReconnecting()){
            Client.getInstance().setReconnecting(false);
            Platform.runLater(this::reconnect);
        }
        Platform.runLater(this::getValidActions);
    }

    @Override
    public void reconnect() {
        gameController.reconnect();
    }

    @Override
    public void getValidActions() {
        gameController.getValidActions();
    }

    private void addWeaponOnMapSpawnPoint(WeaponCard card, RoomColor color) {
        int i = 0;
        Image img = new Image(ResourceController.getResource("weaponcards/" + card.image).toURI().toString());
        while (weaponOnSpawnPointMap.get(color).get(i).getImage() != null) {
            i++;
        }
        if (i>2) return;
        weaponOnSpawnPointMap.get(color).get(i).setImage(img);
    }

    private void removeWeaponOnMapSpawnPoint(WeaponCard card, RoomColor color) {
        int i = 0;
        try {
            while (weaponOnSpawnPointMap.get(color).get(i).getImage() != null && !weaponOnSpawnPointMap.get(color).get(i).getImage().getUrl().contains(card.image)) {
                i++;
            }
        } catch (NullPointerException e) {
            return;
        }

        for(;i<2;i++) {
            weaponOnSpawnPointMap.get(color).get(i).setImage(weaponOnSpawnPointMap.get(color).get(i+1).getImage());
        }
        weaponOnSpawnPointMap.get(color).get(2).setImage(null);
    }

    public void addCardToHand(EffectCard card, List<ImageView> where) {
        Platform.runLater( () -> {
            int i = 0;
            Image img = new Image(ResourceController.getResource((card.getClass().equals(WeaponCard.class) ? "weaponcards/" : "powerupcards/")  + card.image).toURI().toString());
            while (where.get(i).getImage() != null) {
                i++;
            }
            if (i > 3) return;
            where.get(i).setImage(img);
        });
    }

    public void removeCardToHand(EffectCard card, List<ImageView> where) {
        Platform.runLater( () -> {
            int i = 0;
            while (where.get(i).getImage() != null && !where.get(i).getImage().getUrl().contains(card.image)) {
                i++;
            }

            for (; i < (card.getClass().equals(WeaponCard.class) ? 2 : 3); i++) {
                where.get(i).setImage(where.get(i + 1).getImage());
            }
            where.get((card.getClass().equals(WeaponCard.class) ? 2 : 3)).setImage(null);
        });
    }

    public void drawPowerUp() {
        gameController.drawPowerUp();
        gameController.getValidActions();
    }
    public void grabWeapon() {

    }
    public void spawn() {
        showMessage("Please click on the power up card you wish to DISCARD and spawn accordingly.");
        setBtnEnabled(btnSpawn, false);
        canClickOnPowerUps = true;
    }
    public void runBtnClicked() {
        setBtnEnabled(btnRun, false);
        showMessage("Please click on the map where you want to go.");
    }
    public void run(Square square) {
        TurnEvent te = Client.getInstance().getActions().get(Client.getInstance().getCurrentActionType()).get(0);
        Client.getInstance().getConnection().run(te, square);
        gameController.getValidActions();
    }
    public void grab() {
        setBtnEnabled(btnGrab, false);
        gameController.grab(null, null, null);
        GameMap map = Client.getInstance().getMap();
        Player me = Client.getInstance().getPlayer();
        if (map.getPlayerPosition(me).isSpawnPoint()) {
            //Spawn Point
            if (me.getWeaponList().size() > 2) {
                showMessage("YOU'VE 3 WEAPONS! Please select a weapon to discard first.");
                canDiscardHisOwnWeapons = true;
            } else {
                showMessage("Please select which weapon you want to draw.");
                canChooseSpawnPointWeapon = true;
            }
        } else {
            //Ammo point
            gameController.grab(null, null, null);
        }
    }

    public void shoot() {

    }
    public void reload() {

    }
    public void usePowerUp() {

    }

    public void endTurn() {
        gameController.endTurn();
        setBtnEnabled(btnEndTurn, false);
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
    public void updateActions(Map<ActionType, List<TurnEvent>> actions){
        Client client = Client.getInstance();
        ActionType currentActionType = client.getCurrentActionType();

        setBtnEnabled(btnEndTurn, (actions.isEmpty() && client.isMyTurn()));

        if(currentActionType==null){
            disableTurnEventButtons();
            setActionGroupButtons(actions.keySet());
        }else {
            setTurnEventButtons(actions.get(currentActionType));
        }
    }

    private void setActionGroupButtons(Set<ActionType> groupActions){
        setBtnEnabled(btnActionGroup1, false);
        setBtnEnabled(btnActionGroup2, false);
        setBtnEnabled(btnActionGroup3, false);

        if(groupActions.size() > 1 && Client.getInstance().getCurrentActionType()==null){
            int i = 0;
            for(ActionType groupAction : groupActions){
                setTextAndEnableBtnActionGroup(groupAction, ++i);
                showMessage("Action group: " + groupAction.name());
            }
        }
    }

    private void setTurnEventButtons(List<TurnEvent> turnEvents){
        disableTurnEventButtons();
        for (TurnEvent turnEvent : turnEvents) {
            Button buttonToShow = null;
            switch (turnEvent) {
                case DRAW:
                    buttonToShow = btnDrawPowerUp;
                    break;
                case RUN_1:
                case RUN_2:
                case RUN_3:
                case RUN_4:
                    buttonToShow = btnRun;
                    break;
                case SHOOT:
                    buttonToShow = btnShoot;
                    break;
                case RELOAD:
                    buttonToShow = btnReload;
                    break;
                case SPAWN:
                    if(turnEvents.size()==1){
                        buttonToShow = btnSpawn;
                    }
                    break;
                case GRAB:
                    buttonToShow = btnGrab;
                    break;
                //NE MANCANO ALCUNI! TIPO I VARI USEPOWERUP
            }
            setBtnEnabled(buttonToShow, true);
        }
    }

    private void disableTurnEventButtons(){
        for(Button button : turnEventButtons){
            setBtnEnabled(button, false);
        }
    }

    private void setBtnEnabled(Button button, boolean isVisible){
        if(button!=null){
            Platform.runLater( () -> { button.setDisable(!isVisible); });
        }
    }

    private void addOnPane(GridPane pane, Node node) {
        Platform.runLater( () -> {
            switch (pane.getChildren().size()) {
                case 0:
                    pane.add(node, 1, 2);
                    break;
                case 1:
                    pane.add(node, 2, 2);
                    break;
                case 2:
                    pane.add(node, 1, 3);
                    break;
                case 3:
                    pane.add(node, 2, 3);
                    break;
                case 4:
                    pane.add(node, 1, 1);
                    break;
                default:
                    pane.add(node, 2, 1);
                    break;
            }
        });
    }

    private void removeFromPane(GridPane pane, Node node) {
        Platform.runLater( () -> {
            pane.getChildren().remove(node);
            ArrayList<Node> arr = new ArrayList<>(pane.getChildren());
            pane.getChildren().clear();
            for (Node n : arr) {
                addOnPane(pane, n);
            }
        });
    }

    private Color getColor(int i) {
        switch (i) {
            case 1:
                return Color.rgb(153, 255, 153); //LIGHT GREEN
            case 2:
                return Color.rgb(0, 204, 255); //LIGHT BLUE
            case 3:
                return Color.rgb(102, 0, 204); //DARK VIOLET
            case 4:
                return Color.rgb(255, 153, 204); //PINK
            default:
                return Color.rgb(204, 51, 0); //DARK RED
        }
    }

    private void drawPlayerToken(GridPane pane, int i) {
        Circle circle = new Circle(0.0d,0.0d,17.0d);
        circle.setFill(getColor(i));
        addOnPane(pane, circle);
    }

    private void deletePlayerToken(GridPane pane, int i) {
        Circle circle1 = new Circle(0.0d,0.0d,17.0d);
        circle1.setFill(getColor(i));
        for (int num = 0; pane.getChildren().size() > num; num++) {
            Node n = pane.getChildren().get(num);
            if (n.getClass() == circle1.getClass()) {
                if (((Circle) n).getFill().equals(circle1.getFill())) {
                    removeFromPane(pane, n);
                }
            }
        }
    }

    private void deleteAllAmmoOnPane(GridPane pane) { //To be used when updating map
        for (int num = 0; pane.getChildren().size() > num; num++) {
            Node n = pane.getChildren().get(num);
            if (n.getClass().equals(ImageView.class)) {
                removeFromPane(pane, n);
            }
        }
    }


    @Override
    public void updateMapView(GameMap map) {
        this.gameMap = map;
        //UPDATE PLAYERS POSITIONS
        for (Player p : Client.getInstance().getPlayers()) {
            try {
                Coordinate c = map.getPlayerCoordinates(p);
                Coordinate oldCoord = Client.getInstance().getPlayerCoordinateMap().get(p);
                if (!c.equals(oldCoord)) {
                    if (oldCoord != null) {
                        deletePlayerToken(paneList.get(oldCoord.getX()).get(oldCoord.getY()), Client.getInstance().getPlayers().indexOf(p));
                    }
                    drawPlayerToken(paneList.get(c.getX()).get(c.getY()), Client.getInstance().getPlayers().indexOf(p));
                    Client.getInstance().getPlayerCoordinateMap().put(p, c);
                }
            }
            catch (PlayerNotOnMapException e) {
                //Nothing to do, just don't draw it.
            }
        }
        //REMOVE OLD WEAPONS
        for (SpawnPoint sp : Client.getInstance().getMap().getSpawnPoints()) {
            for (WeaponCard cardOld : sp.showCards()) {
                removeWeaponOnMapSpawnPoint(cardOld, sp.getColor());
            }
        }
        //REMOVE OLD AMMO
        for (int x = 0; x<4; x++) {
            for (int y = 0; y < 3; y++) {
                Square square = map.getSquareByCoordinate(x, y);
                if (square == null) continue;
                if (square.isSpawnPoint()) continue;
                GridPane panetoremove = paneList.get(x).get(y);
                deleteAllAmmoOnPane(panetoremove);
            }
        }
        //UPDATE AMMO'S AND WEAPONS ON MAP
        for (int x = 0; x<4; x++) {
            for (int y=0; y<3; y++) {
                Square square = map.getSquareByCoordinate(x, y);
                if (square == null) continue;
                if (square.isSpawnPoint()) {
                    SpawnPoint spawnPoint = (SpawnPoint) square;
                    //ADD NEW WEAPONS
                    for (WeaponCard card : spawnPoint.showCards()) {
                        addWeaponOnMapSpawnPoint(card, spawnPoint.getColor());
                    }
                } else {
                    AmmoPoint ammoPoint = (AmmoPoint) square;
                    AmmoCard ammoCard = ammoPoint.showCard();
                    if (ammoCard==null) continue;
                    String imageName = String.format("ammo/%d%d%d%s.png", ammoCard.getRed(), ammoCard.getBlue(), ammoCard.getYellow(), ammoCard.hasPowerup() ? "y" : "n");
                    Image image = new Image(imageName);
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(45);
                    imageView.setFitHeight(45);
                    GridPane panetoadd = paneList.get(x).get(y);
                    addOnPane(panetoadd, imageView);
                }
            }
        }

    }

    @Override
    public void updatePlayerView(Player newPlayer) {
        this.me = newPlayer;
        Player oldPlayer = Client.getInstance().getPlayer();
        Platform.runLater(() -> {
            //UPDATE AMMO
            Ammo myAmmo = newPlayer.getAmmo();
            redAmmoText.setText(Integer.toString(myAmmo.red));
            blueAmmoText.setText(Integer.toString(myAmmo.blue));
            yellowAmmoText.setText(Integer.toString(myAmmo.yellow));
            //REMOVE OLD POWERUP IF ANY
            if(oldPlayer != null)
                for (PowerUpCard powerUpCard : oldPlayer.getPowerUpList()) {
                    removeCardToHand(powerUpCard, powerUpSpaces);
                }
            //ADD NEW POWERUP
            for (PowerUpCard powerUpCard : newPlayer.getPowerUpList()) {
                addCardToHand(powerUpCard, powerUpSpaces);
            }
            //REMOVE OLD WEAPONS IF ANY
            if(oldPlayer != null)
                for (WeaponCard weaponCard : oldPlayer.getWeaponList()) {
                    removeCardToHand(weaponCard, weaponSpaces);
                }
            //ADD NEW WEAPONS
            for (WeaponCard weaponCard : newPlayer.getWeaponList()) {
                addCardToHand(weaponCard, weaponSpaces);
            }
        });
    }

    @Override
    public void onNewPlayer(String playerName) {
        showMessage("Player " + playerName + " connected!");
    }

    @Override
    public void onPlayerDisconnected(String name) {
        showMessage("Player " + name + " disconnected!");
    }

    public void powerUp1Clicked() {
        if (canClickOnPowerUps && (!Client.getInstance().getPlayer().getPowerUpList().isEmpty())) {
            canClickOnPowerUps = false;
            gameController.spawn(Client.getInstance().getPlayer().getPowerUpList().get(0));
        }
    }
    public void powerUp2Clicked() {
        if (canClickOnPowerUps && (Client.getInstance().getPlayer().getPowerUpList().size() > 1)) {
            canClickOnPowerUps = false;
            gameController.spawn(Client.getInstance().getPlayer().getPowerUpList().get(1));
        }
    }
    public void powerUp3Clicked() {
        if (canClickOnPowerUps && (Client.getInstance().getPlayer().getPowerUpList().size() > 2)) {
            canClickOnPowerUps = false;
            gameController.spawn(Client.getInstance().getPlayer().getPowerUpList().get(2));
        }
    }
    public void powerUp4Clicked() {
        if (canClickOnPowerUps && (Client.getInstance().getPlayer().getPowerUpList().size() > 3)) {
            canClickOnPowerUps = false;
            gameController.spawn(Client.getInstance().getPlayer().getPowerUpList().get(3));
        }
    }

    public void pane00Clicked() {
        run(Client.getInstance().getMap().getSquareByCoordinate(0, 0));
    }
    public void pane01Clicked() {
        run(Client.getInstance().getMap().getSquareByCoordinate(0, 1));
    }
    public void pane02Clicked() {
        run(Client.getInstance().getMap().getSquareByCoordinate(0, 2));
    }
    public void pane10Clicked() {
        run(Client.getInstance().getMap().getSquareByCoordinate(1, 0));
    }
    public void pane11Clicked() {
        run(Client.getInstance().getMap().getSquareByCoordinate(1, 1));
    }
    public void pane12Clicked() {
        run(Client.getInstance().getMap().getSquareByCoordinate(1, 2));
    }
    public void pane20Clicked() {
        run(Client.getInstance().getMap().getSquareByCoordinate(2, 0));
    }
    public void pane21Clicked() {
        run(Client.getInstance().getMap().getSquareByCoordinate(2, 1));
    }
    public void pane22Clicked() {
        run(Client.getInstance().getMap().getSquareByCoordinate(2, 2));
    }
    public void pane30Clicked() {
        run(Client.getInstance().getMap().getSquareByCoordinate(3, 0));
    }
    public void pane31Clicked() {
        run(Client.getInstance().getMap().getSquareByCoordinate(3, 1));
    }
    public void pane32Clicked() {
        run(Client.getInstance().getMap().getSquareByCoordinate(3, 2));
    }

    public void weaponClicked(RoomColor color, int position) {
        SpawnPoint spawnPoint = (SpawnPoint) Client.getInstance().getMap().getAllSquaresInRoom(color).stream().filter(Square::isSpawnPoint).findFirst().orElse(null);
        Iterator<WeaponCard> it = spawnPoint.showCards().iterator();
        WeaponCard wc = null;
        while (position!=0) {
            wc = it.next();
            position--;
        }
        if (canChooseSpawnPointWeapon) {

            //START CHOOSE POWER UP DIALOG
            powerUpChoosingDialog(wc);
            //END CHOOSE POWER UP DIALOG

        }
    }

    private void powerUpChoosingDialog(WeaponCard wc) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Just a question...");
            alert.setHeaderText("Do you want to use a power up to pay for this action?");
            alert.setContentText("If yes, select one of yours, if not, please click no.");

            List<ButtonType> btlist = new ArrayList<>();
            btlist.add(new ButtonType("No, I don't."));
            Player me = Client.getInstance().getPlayer();
            List<PowerUpCard> powerUpCards = me.getPowerUpList();

            for (PowerUpCard powerUpCard : powerUpCards) {
                btlist.add(new ButtonType(powerUpCard.name));
            }

            alert.getButtonTypes().setAll(btlist);
            Optional<ButtonType> result = alert.showAndWait();
            if (!result.isPresent()) {
                if (me.getWeaponList().size() == 3)
                    discardWeaponChoosingDialog(wc, null);
                else
                    gameController.grab(wc, toDiscard, null);
            } else {
                if (result.get() == btlist.get(0)) {
                    if (me.getWeaponList().size() == 3)
                        discardWeaponChoosingDialog(wc, null);
                    else
                        gameController.grab(wc, toDiscard, null);
                } else {
                    int index = btlist.indexOf(result.get());
                    if (me.getWeaponList().size() == 3)
                        discardWeaponChoosingDialog(wc, powerUpCards.get(index - 1));
                    else
                        gameController.grab(wc, toDiscard, powerUpCards.get(index - 1));
                }
            }
        });
    }

    private void discardWeaponChoosingDialog(WeaponCard wc, PowerUpCard payWith) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("AAAALT!!!");
            alert.setHeaderText("You need to discard a weapon in order to draw this one.");
            alert.setContentText("Select one weapon to discard from yours.");

            List<ButtonType> btlist = new ArrayList<>();
            Player me = Client.getInstance().getPlayer();
            List<WeaponCard> weaponCards = me.getWeaponList();

            for (WeaponCard weaponCard : weaponCards) {
                btlist.add(new ButtonType(weaponCard.name));
            }

            alert.getButtonTypes().setAll(btlist);
            Optional<ButtonType> result = alert.showAndWait();

            if (!result.isPresent()) {
                gameController.grab(wc, weaponCards.get(0), null);
            } else {
                int index = btlist.indexOf(result.get());
                gameController.grab(wc, weaponCards.get(index), payWith);
            }
        });
    }

    public void blueWeaponClicked1() {
        weaponClicked(RoomColor.BLUE, 1);
    }
    public void blueWeaponClicked2() {
        weaponClicked(RoomColor.BLUE, 2);
    }
    public void blueWeaponClicked3() {
        weaponClicked(RoomColor.BLUE, 3);
    }
    public void redWeaponClicked1() {
        weaponClicked(RoomColor.RED, 1);
    }
    public void redWeaponClicked2() {
        weaponClicked(RoomColor.RED, 2);
    }
    public void redWeaponClicked3() {
        weaponClicked(RoomColor.RED, 3);
    }
    public void yellowWeaponClicked1() {
        weaponClicked(RoomColor.YELLOW, 1);
    }
    public void yellowWeaponClicked2() {
        weaponClicked(RoomColor.YELLOW, 2);
    }
    public void yellowWeaponClicked3() {
        weaponClicked(RoomColor.YELLOW, 3);
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
                    setBtnEnabled(btnActionGroup1, true);
                    btnActionGroup1.setText(actionType.name());
                    buttonActionTypeMap.put(1, actionType);
                    break;
                case 2:
                    btnActionGroup2.setVisible(true);
                    setBtnEnabled(btnActionGroup2, true);
                    btnActionGroup2.setText(actionType.name());
                    buttonActionTypeMap.put(2, actionType);
                    break;
                case 3:
                    btnActionGroup3.setVisible(true);
                    setBtnEnabled(btnActionGroup3, true);
                    btnActionGroup3.setText(actionType.name());
                    buttonActionTypeMap.put(3, actionType);
                    break;
            }
        });
    }


}
