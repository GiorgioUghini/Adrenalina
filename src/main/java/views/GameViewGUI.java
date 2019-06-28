package views;

import controllers.GameController;
import controllers.ResourceController;
import controllers.ScreenController;
import errors.PlayerNotOnMapException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import models.card.*;
import models.map.*;
import models.player.Ammo;
import models.player.Player;
import models.turn.ActionType;
import models.turn.TurnEvent;
import network.Client;
import utils.BiMap;

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
    private ImageView firstPlayer0;
    @FXML
    private ImageView firstPlayer1;
    @FXML
    private ImageView firstPlayer2;
    @FXML
    private ImageView firstPlayer3;
    @FXML
    private ImageView firstPlayer4;

    @FXML
    private TabPane tabPane;
    @FXML
    private Button btnEndSelect;

    @FXML
    private AnchorPane anchorPanePlayer0;
    @FXML
    private AnchorPane anchorPanePlayer1;
    @FXML
    private AnchorPane anchorPanePlayer2;
    @FXML
    private AnchorPane anchorPanePlayer3;
    @FXML
    private AnchorPane anchorPanePlayer4;
    @FXML
    private AnchorPane skullPane;

    @FXML
    private Text redAmmoText;
    @FXML
    private Text blueAmmoText;
    @FXML
    private Text yellowAmmoText;
    @FXML
    private Text actualPoints0;
    @FXML
    private Text actualPoints1;
    @FXML
    private Text actualPoints2;
    @FXML
    private Text actualPoints3;
    @FXML
    private Text actualPoints4;

    private List<ImageView> powerUpSpaces = new ArrayList<>();
    private List<ImageView> weaponSpaces = new ArrayList<>();
    private List<ImageView> firstPlayerList = new ArrayList<>();
    private List<AnchorPane> anchorPanePlayers = new ArrayList<>();
    private List<Text> actualPointsList = new ArrayList<>();
    private Map<RoomColor, List<ImageView>> weaponOnSpawnPointMap = new EnumMap<>(RoomColor.class);

    private HashMap<Integer, ActionType> buttonActionTypeMap = new HashMap<>();

    private WeaponCard toDiscard = null;

    private Map<ViewAction, Boolean> canDoActionMap = new EnumMap<>(ViewAction.class);

    private GameController gameController;

    private BiMap<Circle, Player> circlePlayerMap = new BiMap<>();
    private Set<Object> clickableObjects;

    private int maxRunDistance;
    private boolean isShooting;
    private boolean firstTurn = true;

    public GameViewGUI() {
        this.gameController = new GameController();
        clickableObjects = new HashSet<>();
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
        anchorPanePlayers.add(anchorPanePlayer0);
        anchorPanePlayers.add(anchorPanePlayer1);
        anchorPanePlayers.add(anchorPanePlayer2);
        anchorPanePlayers.add(anchorPanePlayer3);
        anchorPanePlayers.add(anchorPanePlayer4);
        firstPlayerList.add(firstPlayer0);
        firstPlayerList.add(firstPlayer1);
        firstPlayerList.add(firstPlayer2);
        firstPlayerList.add(firstPlayer3);
        firstPlayerList.add(firstPlayer4);
        actualPointsList.add(actualPoints0);
        actualPointsList.add(actualPoints1);
        actualPointsList.add(actualPoints2);
        actualPointsList.add(actualPoints3);
        actualPointsList.add(actualPoints4);


        for (int i=0; i<5; i++) {
            String imageName = String.format("tabs/tab%d.png", i);
            Image image = new Image(imageName);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(949);
            imageView.setFitHeight(234);
            AnchorPane anchorPane = anchorPanePlayers.get(i);
            Platform.runLater( () -> anchorPane.getChildren().add(imageView));
        }

        canDoActionMap.put(ViewAction.USEPOWERUP, false);
        canDoActionMap.put(ViewAction.CLICKPOWERUPSPAWN, false);
        canDoActionMap.put(ViewAction.CHOOSESPAWNPOINTWEAPON, false);
        canDoActionMap.put(ViewAction.SHOOT, false);
        canDoActionMap.put(ViewAction.SELECTPLAYER, false);
        canDoActionMap.put(ViewAction.SELECTROOM, false);
        canDoActionMap.put(ViewAction.SELECTSQUARE, false);
        canDoActionMap.put(ViewAction.RUN, false);

        //this.turnEventButtons = new ArrayList<>(Arrays.asList(btnDrawPowerUp, btnGrab, btnSpawn, btnRun, btnShoot, btnReload, btnUsePowerUp));
        this.turnEventButtons = new ArrayList<>(Arrays.asList(btnDrawPowerUp, btnGrab, btnSpawn, btnRun, btnShoot, btnReload));

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
        Image image = new Image("planciaDanni.png");
        Platform.runLater(() -> ((ImageView) skullPane.getChildren().get(0)).setImage(image));
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

    public void removeCardsToHand(List<ImageView> where) {
        Platform.runLater( () -> {
            for(ImageView imageView : where){
                imageView.setImage(null);
            }
        });
    }

    public void drawPowerUp() {
        gameController.drawPowerUp();
        gameController.getValidActions();
    }

    public void spawn() {
        showMessage("Please click on the power up card you wish to DISCARD and spawn accordingly.");
        setBtnEnabled(btnSpawn, false);
        canDoActionMap.put(ViewAction.CLICKPOWERUPSPAWN, true);
    }
    public void runBtnClicked() {
        highlighAllSquaresAtMaxDistance(this.maxRunDistance);
        setBtnEnabled(btnRun, false);
        canDoActionMap.put(ViewAction.RUN, true);
        showMessage("Please click on the map where you want to go.");
    }
    public void run(Square square) {
        TurnEvent te = Client.getInstance().getActions().get(Client.getInstance().getCurrentActionType()).get(0);
        Client.getInstance().getConnection().run(te, square);
        gameController.getValidActions();
    }
    public void grab() {
        disableTurnEventButtons();
        GameMap map = Client.getInstance().getMap();
        Player me = Client.getInstance().getPlayer();
        if (map.getPlayerPosition(me).isSpawnPoint()) {
            //Spawn Point
            showMessage("Please select which weapon you want to draw.");
            canDoActionMap.put(ViewAction.CHOOSESPAWNPOINTWEAPON, true);
        } else {
            //Ammo point
            gameController.grab(null, null, null);
        }
    }

    public void shoot() {
        isShooting = true;
        setBtnEnabled(btnShoot, false);
        showMessage("Select which weapon you'd like to use.");
        canDoActionMap.put(ViewAction.SHOOT, true);
    }
    public void reload() {
        setBtnEnabled(btnReload, false);
        Platform.runLater(() -> {
            Map<WeaponCard, PowerUpCard> reloadingWeapons = new HashMap<>();
            Player me = Client.getInstance().getPlayer();
            List<WeaponCard> reloadableWeapons;
            while( !(reloadableWeapons = getReloadableWeapons()).isEmpty() ){
                WeaponCard toReload = showReloadAlert(reloadableWeapons);
                if(toReload == null) break;
                PowerUpCard powerUpToPay = null;
                if(!me.getPowerUpList().isEmpty()){
                    powerUpToPay = choosePowerUpDialog();
                }
                if(!me.canReloadWeapon(toReload, powerUpToPay)){
                    if(powerUpToPay==null){
                        showMessage("You need to use a powerup to reload this weapon");
                    }else{
                        showMessage("You cannot reload " + toReload.getName() + " with powerup " + powerUpToPay.getFullName());
                    }
                }else{
                    reloadingWeapons.put(toReload, powerUpToPay);
                    me.reloadWeapon(toReload, powerUpToPay);
                }
            }
            Client.getInstance().getConnection().reload(reloadingWeapons);
            for(WeaponCard weaponCard : reloadingWeapons.keySet()){
                showMessage("Successfully reloaded " + weaponCard.getName());
            }
        });
    }

    private List<WeaponCard> getReloadableWeapons(){
        Player me = Client.getInstance().getPlayer();
        List<WeaponCard> toReload = new ArrayList<>();
        for(WeaponCard weaponCard : me.getWeaponList()){
            if(!weaponCard.isLoaded() && me.canReloadWeapon(weaponCard)){
                toReload.add(weaponCard);
            }
        }
        return toReload;
    }

    private WeaponCard showReloadAlert(List<WeaponCard> reloadableWeapons){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Reload");
        alert.setHeaderText("Choose a weapon to reload");
        alert.setContentText("This box only shows the weapons you can actually reload. Please choose one or click x to exit");

        List<ButtonType> btlist = new ArrayList<>();

        for (WeaponCard weaponCard : reloadableWeapons) {
            btlist.add(new ButtonType(weaponCard.name));
        }

        alert.getButtonTypes().setAll(btlist);
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent()){
            int index = btlist.indexOf(result.get());
            return reloadableWeapons.get(index);
        }
        return null;
    }

    public void usePowerUp() {
        isShooting = false;
        setBtnEnabled(btnUsePowerUp, false);
        showMessage("Select which power up you'd like to use.");
        canDoActionMap.put(ViewAction.USEPOWERUP, true);
    }

    public void endTurn() {
        gameController.endTurn();
        setBtnEnabled(btnEndTurn, false);
        setBtnEnabled(btnReload, false);
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

        boolean turnIsEnding = actions.isEmpty() && client.isMyTurn();

        disableTurnEventButtons();
        disableActionGroupButtons();

        setBtnEnabled(btnEndTurn, turnIsEnding);
        //this must be called before setTurnEventButtons or it breaks the frenzy reload
        setBtnEnabled(btnReload, turnIsEnding);

        if(currentActionType==null){
            setActionGroupButtons(actions.keySet());
        }else {
            setTurnEventButtons(actions.get(currentActionType));
        }

        if(actions.isEmpty()) firstTurn = false;
    }

    private void setActionGroupButtons(Set<ActionType> groupActions){
        if(groupActions.size() > 1 && Client.getInstance().getCurrentActionType()==null){
            int i = 0;
            for(ActionType groupAction : groupActions){
                setTextAndEnableBtnActionGroup(groupAction, ++i);
                showMessage("Action group: " + groupAction.name());
            }
        }
    }

    private void disableActionGroupButtons(){
        setBtnEnabled(btnActionGroup1, false);
        setBtnEnabled(btnActionGroup2, false);
        setBtnEnabled(btnActionGroup3, false);
    }

    private void setTurnEventButtons(List<TurnEvent> turnEvents){
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
                    this.maxRunDistance = Character.getNumericValue(turnEvent.toString().charAt(4));
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

    private void highlighAllSquaresAtMaxDistance(int distance){
        Player me = Client.getInstance().getPlayer();
        GameMap gameMap = Client.getInstance().getMap();
        Square mySquare = gameMap.getPlayerPosition(me);
        Set<Square> squares = gameMap.getAllSquaresAtDistanceLessThanOrEquals(mySquare, distance);
        for(Square square : squares){
            highlightSquare(square);
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

    private int getIndex(String color) {
        switch (color){
            case "GREEN": return 0;
            case "BLUE": return 1;
            case "PURPLE": return 2;
            case "WHITE": return 3;
            case "YELLOW": return 4;
        }
        return -1;
    }


    private void drawPlayerToken(GridPane pane, Player p) {
        Circle circle = new Circle(0.0d,0.0d,17.0d);
        int i = Client.getInstance().getPlayers().indexOf(p);
        circle.setFill(p.getPlayerColor());
        final String t = p.getStringColor();
        tabPane.getTabs().get(i).setDisable(false);
        if (i == 0) {
            if (firstPlayerList.get(i).getImage() == null) {
                Image image = new Image("firstPlayer.png");
                Platform.runLater(() -> firstPlayerList.get(i).setImage(image));
            }
        }
        if (Client.getInstance().getPlayers().indexOf(Client.getInstance().getPlayer()) == i) {
            Platform.runLater(() -> {
                Tab tab = tabPane.getTabs().get(i);
                tab.setText("## YOU: Player " + t);
            });
        }
        circle.setOnMouseClicked( e -> {
            playerClicked(p);
            e.consume();
        });
        circle.setStrokeWidth(0d);
        circle.setStroke(Color.BLACK);
        circlePlayerMap.add(circle, p);
        Platform.runLater(() -> addOnPane(pane, circle));
    }

    private void highlightCircle(Circle c) {
        clickableObjects.add(c);
        Platform.runLater( () -> c.setStrokeWidth(7d));
    }

    private void undoHighlightCircle(Circle c) {
        Platform.runLater( () -> c.setStrokeWidth(0d));
    }

    private void highlightGridPane(GridPane gp) {
        clickableObjects.add(gp);
        Platform.runLater( () -> gp.setStyle("-fx-background-color: green; -fx-opacity: 0.5;"));
    }

    private void undoHighlightGridPane(GridPane gp) {
        Platform.runLater( () -> gp.setStyle(""));
    }

    private void undoHighlighAllGridPanes(){
        for (Square sq : Client.getInstance().getMap().getAllSquares()) {
            Coordinate coord = Client.getInstance().getMap().getSquareCoordinates(sq);
            undoHighlightGridPane(paneList.get(coord.getX()).get(coord.getY()));
        }
    }

    private void highlightSquare(Square square){
        Coordinate coord = Client.getInstance().getMap().getSquareCoordinates(square);
        GridPane clickable = paneList.get(coord.getX()).get(coord.getY());
        highlightGridPane(clickable);
    }

    private void playerClicked(Player p) {
        Circle clicked = circlePlayerMap.getSingleKey(p);
        if (canDoActionMap.get(ViewAction.SELECTPLAYER)) {
            if(!clickableObjects.contains(clicked)){
                showMessage("You cannot click this player");
                return;
            }
            for(Circle circle : circlePlayerMap.getKeys()){
                undoHighlightCircle(circle);
            }
            canDoActionMap.put(ViewAction.SELECTPLAYER, false);
            onSelectDone(p);
        } else if (canDoActionMap.get(ViewAction.SELECTSQUARE) || canDoActionMap.get(ViewAction.SELECTROOM)) {
            Square square = Client.getInstance().getMap().getPlayerPosition(p);
            squareClicked(square);
        }
    }

    private void deletePlayerToken(GridPane pane, Player player) {
        Circle circle1 = new Circle(0.0d,0.0d,17.0d);
        circle1.setFill(player.getPlayerColor());
        for (int num = 0; pane.getChildren().size() > num; num++) {
            Node n = pane.getChildren().get(num);
            if (n.getClass() == circle1.getClass()) {
                if (((Circle) n).getFill().equals(circle1.getFill())) {
                    circlePlayerMap.removeByKey((Circle) n);
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
        Client client = Client.getInstance();
        //UPDATE PLAYERS POSITIONS
        for (Player p : client.getPlayers()) {
            try {
                Coordinate c = map.getPlayerCoordinates(p);
                Coordinate oldCoord = client.getPlayerCoordinateMap().get(p);
                if (!c.equals(oldCoord)) {
                    if (oldCoord != null) {
                        deletePlayerToken(paneList.get(oldCoord.getX()).get(oldCoord.getY()), p);
                    }
                    drawPlayerToken(paneList.get(c.getX()).get(c.getY()), p);
                    client.getPlayerCoordinateMap().put(p, c);
                }
            }
            catch (PlayerNotOnMapException e) {
                //Nothing to do, just don't draw it.
            }
        }
        //REMOVE OLD WEAPONS
        for (SpawnPoint sp : client.getMap().getSpawnPoints()) {
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
                    Platform.runLater(() -> addOnPane(panetoadd, imageView));
                }
            }
        }

    }

    @Override
    public void updatePlayerView(Player newPlayer) {
        Player oldPlayer = Client.getInstance().getPlayer();
        Platform.runLater(() -> {

            //UPDATE AMMO
            Ammo myAmmo = newPlayer.getAmmo();
            redAmmoText.setText(Integer.toString(myAmmo.red));
            blueAmmoText.setText(Integer.toString(myAmmo.blue));
            yellowAmmoText.setText(Integer.toString(myAmmo.yellow));
            //REMOVE OLD POWERUP IF ANY
            removeCardsToHand(powerUpSpaces);
            //ADD NEW POWERUP
            for (PowerUpCard powerUpCard : newPlayer.getPowerUpList()) {
                addCardToHand(powerUpCard, powerUpSpaces);
            }
            //REMOVE OLD WEAPONS IF ANY
            removeCardsToHand(weaponSpaces);
            //ADD NEW WEAPONS
            for (WeaponCard weaponCard : newPlayer.getWeaponList()) {
                addCardToHand(weaponCard, weaponSpaces);
            }
        });
    }

    @Override
    public void onMark(Player markedPlayer){
        updateDamagedAndMarkedPlayer(markedPlayer);
    }

    @Override
    public void onDamage(Player damagedPlayer){
        updateDamagedAndMarkedPlayer(damagedPlayer);
        Player me = Client.getInstance().getPlayer();
        if(damagedPlayer.equals(me)){
            List<PowerUpCard> playable = new ArrayList<>();
            for(PowerUpCard powerUpCard : me.getPowerUpList()){
                if(powerUpCard.when.equals("on_damage_received")){
                    playable.add(powerUpCard);
                }
            }
            if(playable.isEmpty()) return;
            if(me.getLastDamager()==null) return;
            Platform.runLater(() -> {
                PowerUpCard powerUpCard = choosePowerUpDialog("You have been damaged by " + me.getLastDamager().getName(), "Would you like to mark him?", playable);
                if(powerUpCard!=null){
                    Client.getInstance().getConnection().playPowerUp(powerUpCard.name, null, null);
                }
            });
        }
    }

    private void updateDamagedAndMarkedPlayer(Player newPlayer) {
        List<Player> players = Client.getInstance().getPlayers();
        Player oldPlayer = players.get(players.indexOf(newPlayer));
        //REMOVE DAMAGE
        if(oldPlayer != null)
            removeAllDamageOnPlayer(oldPlayer);
        //ADD DAMAGE
        int i = 0;
        for (Player from : newPlayer.getDamagedBy()) {
            drawDamageOnPlayer(newPlayer, from, i);
            i++;
        }
        //REMOVE MARKS
        if(oldPlayer != null)
            removeAllMarksOnPlayer(oldPlayer);
        //ADD MARKS
        int j = 0;
        for (Player from : Client.getInstance().getPlayers()) {
            for (int k = 0; k < newPlayer.getMarksFromPlayer(from); k++) {
                drawMarkOnPlayer(newPlayer, from, j);
                j++;
            }
        }
        //UPDATE SKULLS IN MAIN PANE
        removeAllSkullOnMainPane();
        addSkullsOnMainPane(newPlayer);
    }

    public void updatePoints(Map<Player, Integer> map) {
        for (Player p : Client.getInstance().getPlayers()) {
            int points = map.get(p);
            actualPointsList.get(Client.getInstance().getPlayers().indexOf(p)).setText("Actual Points: " + points);
        }
    }

    void addSkullsOnMainPane(Player newPlayer) {
        int skulls = 0;
        if (newPlayer.getTotalDamage() > 10) {
            skulls += 1;
        }
        for (Player p : Client.getInstance().getPlayers()) {
            skulls += p.getDeathCount();
        }
        for (int position=0; position<skulls; position++) {
            Rectangle c = new Rectangle((double) (42 + position * 97), 100d, 60d, 105d);
            c.setFill(Color.rgb(0,0,0));
            Platform.runLater( () -> skullPane.getChildren().add(c));
        }
    }

    void removeAllSkullOnMainPane() {
        for (int i = 0; i<skullPane.getChildren().size(); i++) {
            Node n = skullPane.getChildren().get(i);
            if (n.getClass().equals(Circle.class)) {
                Platform.runLater(() -> skullPane.getChildren().remove(n));
            }
        }
    }

    void removeAllDamageOnPlayer(Player p) {
        if (p.getPlayerColor() != null) {
            int index = getIndex(p.getStringColor());
            AnchorPane anchorPane = anchorPanePlayers.get(index);
            for (int i = 0; i<anchorPane.getChildren().size(); i++) {
                Node n = anchorPane.getChildren().get(i);
                if (n.getClass().equals(Circle.class)) {
                    Platform.runLater(() -> anchorPane.getChildren().remove(n));
                }
            }
        }
    }

    void drawDamageOnPlayer(Player to, Player from, int position) {
        //When drawing a circle, first arg is X, second is Y, third is radius. 138px is the height of where the circle must be placed
        //Then, for every new damage, the circle must be on same height but trasled on X.
        Circle c = new Circle((double) (107 + position * 54), 120d, 17d);
        c.setFill(from.getPlayerColor());
        int index = getIndex(to.getStringColor());
        AnchorPane anchorPane = anchorPanePlayers.get(index);
        Platform.runLater( () -> anchorPane.getChildren().add(c));
    }

    void removeAllMarksOnPlayer(Player p) {
        if (p.getPlayerColor() != null) {
            int index = getIndex(p.getStringColor());
            AnchorPane anchorPane = anchorPanePlayers.get(index);
            for (int i = 0; i<anchorPane.getChildren().size(); i++) {
                Node n = anchorPane.getChildren().get(i);
                if (n.getClass().equals(Rectangle.class)) {
                    Platform.runLater(() -> anchorPane.getChildren().remove(n));
                }
            }
        }
    }

    void drawMarkOnPlayer(Player to, Player from, int position) {
        //When drawing a circle, first arg is X, second is Y, third is radius. 138px is the height of where the circle must be placed
        //Then, for every new damage, the circle must be on same height but trasled on X.
        Rectangle rectangle = new Rectangle((470d+ position * 42),14d,27d,27d); //X,Y,L,H
        rectangle.setFill(from.getPlayerColor());
        int index = getIndex(to.getStringColor());
        AnchorPane anchorPane = anchorPanePlayers.get(index);
        Platform.runLater( () -> anchorPane.getChildren().add(rectangle));
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
        List<PowerUpCard> powerUpCards = Client.getInstance().getPlayer().getPowerUpList();
        if(powerUpCards.isEmpty()) {
            showMessage("Cannot click here");
            return;
        }
        PowerUpCard clickedPowerUp = powerUpCards.get(0);
        powerUpClicked(clickedPowerUp);
    }
    public void powerUp2Clicked() {
        List<PowerUpCard> powerUpCards = Client.getInstance().getPlayer().getPowerUpList();
        if(powerUpCards.size() < 2) {
            showMessage("Cannot click here");
            return;
        }
        PowerUpCard clickedPowerUp = powerUpCards.get(1);
        powerUpClicked(clickedPowerUp);
    }
    public void powerUp3Clicked() {
        List<PowerUpCard> powerUpCards = Client.getInstance().getPlayer().getPowerUpList();
        if(powerUpCards.size() < 3) {
            showMessage("Cannot click here");
            return;
        }
        PowerUpCard clickedPowerUp = powerUpCards.get(2);
        powerUpClicked(clickedPowerUp);
    }
    public void powerUp4Clicked() {
        List<PowerUpCard> powerUpCards = Client.getInstance().getPlayer().getPowerUpList();
        if(powerUpCards.size() < 4) {
            showMessage("Cannot click here");
            return;
        }
        PowerUpCard clickedPowerUp = powerUpCards.get(3);
        powerUpClicked(clickedPowerUp);
    }

    private void powerUpClicked(PowerUpCard powerUpCard){
        if (canDoActionMap.get(ViewAction.CLICKPOWERUPSPAWN)) {
            canDoActionMap.put(ViewAction.CLICKPOWERUPSPAWN, false);
            gameController.spawn(powerUpCard);
        } else if (canDoActionMap.get(ViewAction.USEPOWERUP)) {
            Platform.runLater(() -> {
                setBtnEnabled(btnUsePowerUp, true);
                Ammo ammoToPay = null;
                PowerUpCard powerUpCardToPay = null;
                if(powerUpCard.hasPrice){
                    ammoToPay = chooseAmmoDialog();
                    if(ammoToPay == null || ammoToPay.isEmpty()){
                        powerUpCardToPay = choosePowerUpDialog();
                    }
                }
                gameController.playPowerUp(powerUpCard, ammoToPay, powerUpCardToPay);
            });
        }
    }

    private Ammo chooseAmmoDialog(){
        if(Client.getInstance().getPlayer().getAmmo().isEmpty()) return null;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Pay this powerup");
        alert.setHeaderText("This powerup costs 1 ammo of your choice");
        alert.setContentText("Click on one of the ammo buttons or the powerup button to pay with a powerup");

        List<ButtonType> btlist = new ArrayList<>();
        btlist.add(new ButtonType("Pay with powerup"));
        Player me = Client.getInstance().getPlayer();
        Ammo myAmmos = me.getAmmo();

        if(myAmmos.yellow>0){
            btlist.add(new ButtonType("Yellow"));
        }
        if(myAmmos.red>0){
            btlist.add(new ButtonType("Red"));
        }
        if(myAmmos.blue>0){
            btlist.add(new ButtonType("Blue"));
        }

        alert.getButtonTypes().setAll(btlist);
        Optional<ButtonType> result = alert.showAndWait();
        if(!result.isPresent() || result.get() == btlist.get(0)) return null;

        String stringResult = result.get().getText();

        switch (stringResult){
            case "Yellow": return new Ammo(0,0,1);
            case "Red": return new Ammo(0,1,0);
            case "Blue": return new Ammo(1,0,0);
        }
        return null;
    }

    public void weaponClicked1() {
        if (Client.getInstance().getPlayer().getWeaponList().isEmpty()) {
            showMessage("You cannot shoot.");
        } else if (canDoActionMap.get(ViewAction.SHOOT)) {
            canDoActionMap.put(ViewAction.SHOOT, false);
            WeaponCard we = Client.getInstance().getPlayer().getWeaponList().get(0);
            setActualWC(we);
            gameController.getEffects(we);
        }
    }
    public void weaponClicked2() {
        if (Client.getInstance().getPlayer().getWeaponList().isEmpty()) {
            showMessage("You can't shoot.");
        } else if (canDoActionMap.get(ViewAction.SHOOT) && Client.getInstance().getPlayer().getWeaponList().size() > 1) {
            canDoActionMap.put(ViewAction.SHOOT, false);
            WeaponCard we = Client.getInstance().getPlayer().getWeaponList().get(1);
            setActualWC(we);
            gameController.getEffects(we);
        }
    }
    public void weaponClicked3() {
        if (Client.getInstance().getPlayer().getWeaponList().isEmpty()) {
            showMessage("No, you can't shoot.");
        } else if (canDoActionMap.get(ViewAction.SHOOT) && Client.getInstance().getPlayer().getWeaponList().size() > 2) {
            canDoActionMap.put(ViewAction.SHOOT, false);
            WeaponCard we = Client.getInstance().getPlayer().getWeaponList().get(2);
            setActualWC(we);
            gameController.getEffects(we);
        }
    }

    private void squareClicked(Square s) {
        //check that it was clickable
        Coordinate coord = Client.getInstance().getMap().getSquareCoordinates(s);
        GridPane gridPane = paneList.get(coord.getX()).get(coord.getY());
        if(!clickableObjects.contains(gridPane)){
            showMessage("You cannot click on this pane");
            return;
        }
        clickableObjects.clear();
        //-
        if (canDoActionMap.get(ViewAction.RUN)) {
            canDoActionMap.put(ViewAction.RUN, false);
            run(s);
        } else{
            Taggable tagged = s;
            if(canDoActionMap.get(ViewAction.SELECTROOM)){
                RoomColor rc = s.getColor();
                tagged = rc;
                canDoActionMap.put(ViewAction.SELECTROOM, false);
            }else{
                canDoActionMap.put(ViewAction.SELECTSQUARE, false);
            }
            onSelectDone(tagged);
        }
        undoHighlighAllGridPanes();
    }

    public void pane00Clicked() {
        Square s = Client.getInstance().getMap().getSquareByCoordinate(0, 0);
        squareClicked(s);
    }

    public void pane01Clicked() {
        Square s = Client.getInstance().getMap().getSquareByCoordinate(0, 1);
        squareClicked(s);
    }

    public void pane02Clicked() {
        Square s = Client.getInstance().getMap().getSquareByCoordinate(0, 2);
        squareClicked(s);
    }

    public void pane10Clicked() {
        Square s = Client.getInstance().getMap().getSquareByCoordinate(1, 0);
        squareClicked(s);
    }

    public void pane11Clicked() {
        Square s = Client.getInstance().getMap().getSquareByCoordinate(1, 1);
        squareClicked(s);
    }

    public void pane12Clicked() {
        Square s = Client.getInstance().getMap().getSquareByCoordinate(1, 2);
        squareClicked(s);
    }

    public void pane20Clicked() {
        Square s = Client.getInstance().getMap().getSquareByCoordinate(2, 0);
        squareClicked(s);
    }

    public void pane21Clicked() {
        Square s = Client.getInstance().getMap().getSquareByCoordinate(2, 1);
        squareClicked(s);
    }

    public void pane22Clicked() {
        Square s = Client.getInstance().getMap().getSquareByCoordinate(2, 2);
        squareClicked(s);
    }

    public void pane30Clicked() {
        Square s = Client.getInstance().getMap().getSquareByCoordinate(3, 0);
        squareClicked(s);
    }

    public void pane31Clicked() {
        Square s = Client.getInstance().getMap().getSquareByCoordinate(3, 1);
        squareClicked(s);
    }

    public void pane32Clicked() {
        Square s = Client.getInstance().getMap().getSquareByCoordinate(3, 2);
        squareClicked(s);
    }

    public void weaponOnSpawnPointClicked(RoomColor color, int position) {
        SpawnPoint spawnPoint = (SpawnPoint) Client.getInstance().getMap().getAllSquaresInRoom(color).stream().filter(Square::isSpawnPoint).findFirst().orElse(null);
        Iterator<WeaponCard> it = spawnPoint.showCards().iterator();
        WeaponCard wc = null;
        while (position!=0) {
            wc = it.next();
            position--;
        }
        if (canDoActionMap.get(ViewAction.CHOOSESPAWNPOINTWEAPON)) {
            //START CHOOSE POWER UP DIALOG
            powerUpChoosingDialog(wc);
            //END CHOOSE POWER UP DIALOG
        }
    }

    private void powerUpChoosingDialog(WeaponCard wc) {
        Platform.runLater(() -> {
            Player me = Client.getInstance().getPlayer();
            PowerUpCard toPay = choosePowerUpDialog();
            if (me.getWeaponList().size() == 3)
                discardWeaponChoosingDialog(wc, toPay);
            else
                gameController.grab(wc, toDiscard, toPay);
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
        weaponOnSpawnPointClicked(RoomColor.BLUE, 1);
    }
    public void blueWeaponClicked2() {
        weaponOnSpawnPointClicked(RoomColor.BLUE, 2);
    }
    public void blueWeaponClicked3() {
        weaponOnSpawnPointClicked(RoomColor.BLUE, 3);
    }
    public void redWeaponClicked1() {
        weaponOnSpawnPointClicked(RoomColor.RED, 1);
    }
    public void redWeaponClicked2() {
        weaponOnSpawnPointClicked(RoomColor.RED, 2);
    }
    public void redWeaponClicked3() {
        weaponOnSpawnPointClicked(RoomColor.RED, 3);
    }
    public void yellowWeaponClicked1() {
        weaponOnSpawnPointClicked(RoomColor.YELLOW, 1);
    }
    public void yellowWeaponClicked2() {
        weaponOnSpawnPointClicked(RoomColor.YELLOW, 2);
    }
    public void yellowWeaponClicked3() {
        weaponOnSpawnPointClicked(RoomColor.YELLOW, 3);
    }

    public void btnActionGroup1Clicked() {
        ActionType tipo = buttonActionTypeMap.get(1);
        Client.getInstance().setCurrentActionType(tipo);
        Client.getInstance().getConnection().action(Client.getInstance().getCurrentActionType());
    }
    public void btnActionGroup2Clicked() {
        ActionType tipo = buttonActionTypeMap.get(2);
        Client.getInstance().setCurrentActionType(tipo);
        Client.getInstance().getConnection().action(Client.getInstance().getCurrentActionType());
    }
    public void btnActionGroup3Clicked() {
        ActionType tipo = buttonActionTypeMap.get(3);
        Client.getInstance().setCurrentActionType(tipo);
        Client.getInstance().getConnection().action(Client.getInstance().getCurrentActionType());
    }

    private ImageView getImageView(ActionType actionType) {
        ImageView iv;
        double width = 150d;
        double heigh = 50d;
        switch (actionType) {
            case RUN_NORMAL:
                iv = new ImageView(new Image("actions/RUNNORMAL.png"));
                iv.setFitHeight(heigh);
                iv.setFitWidth(width);
                return iv;
            case SHOOT_NORMAL:
                iv = new ImageView(new Image("actions/SHOOTNORMAL.png"));
                iv.setFitHeight(heigh);
                iv.setFitWidth(width);
                return iv;
            case GRAB_NORMAL:
                iv = new ImageView(new Image("actions/GRABNORMAL.png"));
                iv.setFitHeight(heigh);
                iv.setFitWidth(width);
                return iv;
            case GRAB_LOW_LIFE:
                iv = new ImageView(new Image("actions/GRABLOWLIFE.png"));
                iv.setFitHeight(heigh);
                iv.setFitWidth(width);
                return iv;
            case SHOOT_VERY_LOW_LIFE:
                iv = new ImageView(new Image("actions/SHOOTVERYLOWLIFE.png"));
                iv.setFitHeight(heigh);
                iv.setFitWidth(width);
                return iv;
            case RUN_FRENZY_1:
                iv = new ImageView(new Image("actions/RUNFRENZY1.png"));
                iv.setFitHeight(heigh);
                iv.setFitWidth(width);
                return iv;
            case GRAB_FRENZY_1:
                iv = new ImageView(new Image("actions/GRABFRENZY1.png"));
                iv.setFitHeight(heigh);
                iv.setFitWidth(width);
                return iv;
            case SHOOT_FRENZY_1:
                iv = new ImageView(new Image("actions/SHOOTFRENZY1.png"));
                iv.setFitHeight(heigh);
                iv.setFitWidth(width);
                return iv;
            case GRAB_FRENZY_2:
                iv = new ImageView(new Image("actions/GRABFRENZY2.png"));
                iv.setFitHeight(heigh);
                iv.setFitWidth(width);
                return iv;
            //case SHOOT_FRENZY_2:
            default:
                iv = new ImageView(new Image("actions/SHOOTFRENZY2.png"));
                iv.setFitHeight(heigh);
                iv.setFitWidth(width);
                return iv;
        }
    }

    @Override
    public void setTextAndEnableBtnActionGroup(ActionType actionType, int btnNum) {
        Platform.runLater( () -> {
            switch (btnNum) {
                case 1:
                    btnActionGroup1.setVisible(true);
                    setBtnEnabled(btnActionGroup1, true);
                    btnActionGroup1.setGraphic(getImageView(actionType));
                    btnActionGroup1.setText("");
                    buttonActionTypeMap.put(1, actionType);
                    break;
                case 2:
                    btnActionGroup2.setVisible(true);
                    setBtnEnabled(btnActionGroup2, true);
                    btnActionGroup2.setGraphic(getImageView(actionType));
                    btnActionGroup2.setText("");
                    buttonActionTypeMap.put(2, actionType);
                    break;
                case 3:
                    btnActionGroup3.setVisible(true);
                    setBtnEnabled(btnActionGroup3, true);
                    btnActionGroup3.setGraphic(getImageView(actionType));
                    btnActionGroup3.setText("");
                    buttonActionTypeMap.put(3, actionType);
                    break;
            }
        });
    }

    @Override
    public void effectChoosingDialog(LegitEffects legitEffects) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Ok, let's start...");
            alert.setHeaderText("Now I need to know how to start.");
            alert.setContentText("Which effect do you want to use now?");

            List<ButtonType> btlist = new ArrayList<>();

            for (Effect effect : legitEffects.getLegitEffects()) {
                btlist.add(new ButtonType(effect.name));
            }
            btlist.add(new ButtonType("I don't want to use any effect."));

            alert.getButtonTypes().setAll(btlist);
            Optional<ButtonType> result = alert.showAndWait();

            if (!result.isPresent()) {
                gameController.finishCard();
            } else {
                int index = btlist.indexOf(result.get());
                if (index == btlist.size()-1) {
                    gameController.finishCard();
                } else  {
                    //Would you like to pay with power up?
                    PowerUpCard toPay = choosePowerUpDialog();
                    gameController.playEffect(legitEffects.getLegitEffects().get(index), toPay);
                }
            }
        });
    }

    private PowerUpCard choosePowerUpDialog(){
        return choosePowerUpDialog(null, null, null);
    }

    private PowerUpCard choosePowerUpDialog(String headerText, String contentText, List<PowerUpCard> powerUpCards){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Just a question...");
        if(headerText==null) headerText = "Do you want to use a power up to pay for this action?";
        if(contentText==null) contentText = "If yes, select one of yours, if not, please click no.";
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);


        List<ButtonType> btlist = new ArrayList<>();
        btlist.add(new ButtonType("No, I don't."));
        Player me = Client.getInstance().getPlayer();

        if(powerUpCards == null){
            powerUpCards = me.getPowerUpList();
        }
        if(powerUpCards == null || powerUpCards.isEmpty()) return null;

        for (PowerUpCard powerUpCard : powerUpCards) {
            btlist.add(new ButtonType(powerUpCard.getFullName()));
        }

        alert.getButtonTypes().setAll(btlist);
        Optional<ButtonType> result = alert.showAndWait();
        if(!result.isPresent() || result.get() == btlist.get(0)) return null;

        int index = btlist.indexOf(result.get());
        return powerUpCards.get(index - 1);
    }

    private WeaponCard actualWC = null;
    public void setActualWC(WeaponCard wc) {
        this.actualWC = wc;
    }
    @Override
    public void continueWeapon() {
        if (actualWC!=null) {
            gameController.getEffects(actualWC);
        }
    }

    @Override
    public void onEndWeapon() {
        this.setActualWC(null);
    }

    @Override
    public void selectTag(Selectable selectable) {
        switch (selectable.getType()) {
            case ROOM:
                showMessage("Please click on a ROOM highlighted in green.");
                for (Taggable t : selectable.get()) {
                    RoomColor color = (RoomColor) t;
                    Set<Square> squares = Client.getInstance().getMap().getAllSquaresInRoom(color);
                    for (Square sq : squares) {
                        highlightSquare(sq);
                    }
                }
                canDoActionMap.put(ViewAction.SELECTROOM, true);
                break;
            case PLAYER:
                showMessage("Please click on a PLAYER stroked in black.");
                for (Taggable t : selectable.get()) {
                    Circle clickable = circlePlayerMap.getSingleKey((Player) t);
                    highlightCircle(clickable);
                }
                canDoActionMap.put(ViewAction.SELECTPLAYER, true);
                break;
            case SQUARE:
                showMessage("Please click on a SQUARE highlighted in green.");
                for (Taggable t : selectable.get()) {
                    highlightSquare((Square)t);
                }
                canDoActionMap.put(ViewAction.SELECTSQUARE, true);
                break;
        }

        if(selectable.isOptional()){
            setBtnEnabled(btnEndSelect, true);
        }
    }

    public void endSelectTag() {
        onSelectDone(null);
    }

    private void onSelectDone(Taggable selected){
        clickableObjects.clear();
        setBtnEnabled(btnEndSelect, false);
        gameController.tagElement(selected, isShooting);

    }


}
