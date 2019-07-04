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
import javafx.scene.shape.Ellipse;
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
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

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

    private ArrayList<ArrayList<GridPane>> paneList;


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
    private Semaphore fxSemaphore = new Semaphore(1);
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
    private WeaponCard actualWC = null;

    public GameViewGUI() {
        this.gameController = new GameController();
        clickableObjects = new HashSet<>();
    }

    @Override
    /**
     * {@inheritDoc}
     */
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


        for (int i = 0; i < 5; i++) {
            String imageName = String.format("tabs/tab%d.png", i);
            Image image = new Image(imageName);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(949);
            imageView.setFitHeight(234);
            AnchorPane anchorPane = anchorPanePlayers.get(i);
            Platform.runLater(() -> anchorPane.getChildren().add(imageView));
        }

        canDoActionMap.put(ViewAction.USEPOWERUP, false);
        canDoActionMap.put(ViewAction.CLICKPOWERUPSPAWN, false);
        canDoActionMap.put(ViewAction.CHOOSESPAWNPOINTWEAPON, false);
        canDoActionMap.put(ViewAction.SHOOT, false);
        canDoActionMap.put(ViewAction.SELECTPLAYER, false);
        canDoActionMap.put(ViewAction.SELECTROOM, false);
        canDoActionMap.put(ViewAction.SELECTSQUARE, false);
        canDoActionMap.put(ViewAction.RUN, false);

        this.turnEventButtons = new ArrayList<>(Arrays.asList(btnDrawPowerUp, btnGrab, btnSpawn, btnRun, btnShoot, btnReload, btnUsePowerUp));

        ArrayList<GridPane> x0 = new ArrayList<>(Arrays.asList(grid00, grid01, grid02));
        ArrayList<GridPane> x1 = new ArrayList<>(Arrays.asList(grid10, grid11, grid12));
        ArrayList<GridPane> x2 = new ArrayList<>(Arrays.asList(grid20, grid21, grid22));
        ArrayList<GridPane> x3 = new ArrayList<>(Arrays.asList(grid30, grid31, grid32));
        paneList = new ArrayList<>(Arrays.asList(x0, x1, x2, x3));

        ArrayList<ImageView> redSPImages = new ArrayList<>(Arrays.asList(weaponSPRed1, weaponSPRed2, weaponSPRed3));
        ArrayList<ImageView> blueSPImages = new ArrayList<>(Arrays.asList(weaponSPBlue1, weaponSPBlue2, weaponSPBlue3));
        ArrayList<ImageView> yellowSPImages = new ArrayList<>(Arrays.asList(weaponSPYellow1, weaponSPYellow2, weaponSPYellow3));
        weaponOnSpawnPointMap.put(RoomColor.RED, redSPImages);
        weaponOnSpawnPointMap.put(RoomColor.BLUE, blueSPImages);
        weaponOnSpawnPointMap.put(RoomColor.YELLOW, yellowSPImages);

        if (Client.getInstance().isReconnecting()) {
            Client.getInstance().setReconnecting(false);
            Platform.runLater(this::reconnect);
        }
        Image image = new Image("planciaDanni.png");
        Platform.runLater(() -> ((ImageView) skullPane.getChildren().get(0)).setImage(image));
        Platform.runLater(this::getValidActions);
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void reconnect() {
        gameController.reconnect();
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void getValidActions() {
        gameController.getValidActions();
    }

    /**
     * adds a weapon to the spawnpoint of the given color
     * @param card the weapon to add
     * @param color the color of the spawnPoint
     */
    private void addWeaponOnMapSpawnPoint(WeaponCard card, RoomColor color) {
        int i = 0;
        Image img = new Image(ResourceController.getResource("weaponcards/" + card.image));
        for (i = 0; i < weaponOnSpawnPointMap.get(color).size() && weaponOnSpawnPointMap.get(color).get(i).getImage() != null; i++) {

        }
        if (i > 2) return;
        final int j = i;
        Platform.runLater(() -> {
            weaponOnSpawnPointMap.get(color).get(j).setImage(img);
            fxSemaphore.release();
        });
    }

    /**
     * Removes all weapons on the spawnpoint of the given color
     * @param color the color of the spawnpoint
     */
    private void removeWeaponOnMapSpawnPoint(RoomColor color) {
        Platform.runLater(() -> {
            for (int i = 0; i < 3; i++) {
                weaponOnSpawnPointMap.get(color).get(i).setImage(null);
            }
            fxSemaphore.release();
        });

    }

    /**
     * adds a weapon or a powerup card to the hand
     * @param card the card to add
     * @param where in which list the card will be added (powerups or weapons)
     */
    private void addCardToHand(EffectCard card, List<ImageView> where) {
        Platform.runLater(() -> {
            boolean isWeapon = card.getClass().equals(WeaponCard.class);
            int i = 0;
            Image img = new Image(ResourceController.getResource((isWeapon ? "weaponcards/" + card.image : "powerupcards/" + card.image)));
            while (where.get(i).getImage() != null) {
                i++;
            }
            if (i > 3) {
                fxSemaphore.release();
                return;
            }
            where.get(i).setImage(img);
            if (isWeapon) {
                WeaponCard weaponCard = (WeaponCard) card;
                if (!weaponCard.isLoaded()) {
                    where.get(i).setStyle("-fx-opacity: 0.5;");
                } else {
                    where.get(i).setStyle("-fx-opacity: 1;");
                }
            }
            fxSemaphore.release();
        });
    }

    /**
     * Removes all cards from the player hand
     * @param where from which list the cards will be removed
     */
    private void removeCardsToHand(List<ImageView> where) {
        Platform.runLater(() -> {
            for (ImageView imageView : where) {
                imageView.setImage(null);
            }
            fxSemaphore.release();
        });
    }

    /**
     * clicked button to draw a powerUp, calls the server and updates the possible actions
     */
    public void drawPowerUp() {
        gameController.drawPowerUp();
        gameController.getValidActions();
    }

    /**
     * clicked button to spawn, enables the click on a powerup card
     */
    public void spawn() {
        showMessage("Please click on the power up card you wish to DISCARD and spawn accordingly.");
        setBtnEnabled(btnSpawn, false);
        canDoActionMap.put(ViewAction.CLICKPOWERUPSPAWN, true);
    }

    /**
     * Clicked button to run, highlights all square in which the user can run
     */
    public void runBtnClicked() {
        highlighAllSquaresAtMaxDistance(this.maxRunDistance);
        setBtnEnabled(btnRun, false);
        canDoActionMap.put(ViewAction.RUN, true);
        showMessage("Please click on the map where you want to go.");
    }

    /**
     * called when a square is clicked, contains the square to run to and sends it to the server
     * @param square the square to run to
     */
    public void run(Square square) {
        TurnEvent te = Client.getInstance().getActions().get(Client.getInstance().getCurrentActionType()).get(0);
        Client.getInstance().getConnection().run(te, square);
        gameController.getValidActions();
    }

    /**
     * called when the button grab is clicked, recognizes of you are on a spawnpoint or on an ammo point
     * case ammo point: simply calls the server
     * case spawn point: asks with a dialog which weapon you want to draw, how you want to pay and which weapon you want to release
     * and send the info to the server
     */
    public void grab() {
        disableTurnEventButtons();
        GameMap map = Client.getInstance().getMap();
        Player me = Client.getInstance().getPlayer();
        Square myPosition = map.getPlayerPosition(me);
        if (myPosition.isSpawnPoint()) {
            //Spawn Point
            SpawnPoint spawnPoint = (SpawnPoint) myPosition;
            if (spawnPoint.showCards().isEmpty()) {
                showMessage("Nothing to grab here");
                gameController.grab(null, null, null);
            } else {
                showMessage("Please select which weapon you want to draw.");
                canDoActionMap.put(ViewAction.CHOOSESPAWNPOINTWEAPON, true);
            }
        } else {
            //Ammo point
            gameController.grab(null, null, null);
        }
    }

    /**
     * the user clicked the shoot button: enables click on weapons
     */
    public void shoot() {
        isShooting = true;
        setBtnEnabled(btnShoot, false);
        showMessage("Select which weapon you'd like to use.");
        canDoActionMap.put(ViewAction.SHOOT, true);
    }

    /**
     * the user clicked on the reload button: shows an alert with the weapons you can reload
     */
    public void reload() {
        setBtnEnabled(btnReload, false);
        Platform.runLater(() -> {
            Map<WeaponCard, PowerUpCard> reloadingWeapons = new HashMap<>();
            Player me = Client.getInstance().getPlayer();
            List<WeaponCard> reloadableWeapons;
            while (!(reloadableWeapons = getReloadableWeapons()).isEmpty()) {
                WeaponCard toReload = showReloadAlert(reloadableWeapons);
                if (toReload == null) break;
                PowerUpCard powerUpToPay = null;
                if (!me.getPowerUpList().isEmpty()) {
                    powerUpToPay = choosePowerUpDialog();
                }
                if (!me.canReloadWeapon(toReload, powerUpToPay)) {
                    if (powerUpToPay == null) {
                        showMessage("You need to use a powerup to reload this weapon");
                    } else {
                        showMessage("You cannot reload " + toReload.getName() + " with powerup " + powerUpToPay.getFullName());
                    }
                } else {
                    reloadingWeapons.put(toReload, powerUpToPay);
                    me.reloadWeapon(toReload, powerUpToPay);
                }
            }
            Client.getInstance().getConnection().reload(reloadingWeapons);
            for (WeaponCard weaponCard : reloadingWeapons.keySet()) {
                showMessage("Successfully reloaded " + weaponCard.getName());
            }
        });
    }

    /**
     * @return a list of the weapons unloaded and that you have enough money to reload
     */
    private List<WeaponCard> getReloadableWeapons() {
        Player me = Client.getInstance().getPlayer();
        List<WeaponCard> toReload = new ArrayList<>();
        for (WeaponCard weaponCard : me.getWeaponList()) {
            if (!weaponCard.isLoaded() && me.canReloadWeapon(weaponCard)) {
                toReload.add(weaponCard);
            }
        }
        return toReload;
    }

    /**
     * Shows the alert containing the weapons you can reload, the user should select one
     * @param reloadableWeapons the weapons the user can reload
     * @return the chosen weapon
     */
    private WeaponCard showReloadAlert(List<WeaponCard> reloadableWeapons) {
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

        if (result.isPresent()) {
            int index = btlist.indexOf(result.get());
            return reloadableWeapons.get(index);
        }
        return null;
    }

    /**
     * button use powerup clicked: enables click on powerups
     */
    public void usePowerUp() {
        isShooting = false;
        setBtnEnabled(btnUsePowerUp, false);
        Player me = Client.getInstance().getPlayer();

        if (!me.getPowerUpList().isEmpty()) {
            showMessage("Select which power up you'd like to use.");
            canDoActionMap.put(ViewAction.USEPOWERUP, true);
        } else {
            showMessage("You do not have any powerUp");
        }
    }

    /**
     * Button end turn clicked: the request to end the turn is sent to the server
     */
    public void endTurn() {
        gameController.endTurn();
        setBtnEnabled(btnEndTurn, false);
        setBtnEnabled(btnReload, false);
        setBtnEnabled(btnUsePowerUp, false);
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void startTurn(String playerName) {
        if (Client.getInstance().getPlayer().getName().equals(playerName)) {
            gameController.getValidActions();
        }
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void printError(String error) {

    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void showMessage(String message) {
        if (message.equals("\n")) return;
        Platform.runLater(() -> {
            gameStatusListView.getItems().add(message);
        });
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void updateActions(Map<ActionType, List<TurnEvent>> actions) {
        Client client = Client.getInstance();
        ActionType currentActionType = client.getCurrentActionType();

        boolean turnIsEnding = actions.isEmpty() && client.isMyTurn();

        disableTurnEventButtons();
        disableActionGroupButtons();

        setBtnEnabled(btnEndTurn, turnIsEnding);
        //this must be called before setTurnEventButtons or it breaks the frenzy reload
        setBtnEnabled(btnReload, !firstTurn && turnIsEnding);

        if (currentActionType == null) {
            setActionGroupButtons(actions.keySet());
            if (client.isMyTurn() && !firstTurn) {
                setBtnEnabled(btnUsePowerUp, true);
            }
        } else {
            setTurnEventButtons(actions.get(currentActionType));
        }

        if (actions.isEmpty()) firstTurn = false;
    }

    /**
     * Set the buttons on top of the screen, those with an image that decide the main action
     * @param groupActions the list of the actions the user can do
     */
    private void setActionGroupButtons(Set<ActionType> groupActions) {
        if (groupActions.size() > 1 && Client.getInstance().getCurrentActionType() == null) {
            int i = 0;
            for (ActionType groupAction : groupActions) {
                setTextAndEnableBtnActionGroup(groupAction, ++i);
            }
        }
    }

    /**
     * disables the main action group button
     */
    private void disableActionGroupButtons() {
        setBtnEnabled(btnActionGroup1, false);
        setBtnEnabled(btnActionGroup2, false);
        setBtnEnabled(btnActionGroup3, false);
    }

    /**
     * sets the turn event button, the ones enabled after you clicked on an action group button
     * @param turnEvents
     */
    private void setTurnEventButtons(List<TurnEvent> turnEvents) {
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
                    if (turnEvents.size() == 1) {
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

    /**
     * color in green all the squares on map at the given max distance
     * @param distance the max distance to color
     */
    private void highlighAllSquaresAtMaxDistance(int distance) {
        Player me = Client.getInstance().getPlayer();
        GameMap gameMap = Client.getInstance().getMap();
        Square mySquare = gameMap.getPlayerPosition(me);
        Set<Square> squares = gameMap.getAllSquaresAtDistanceLessThanOrEquals(mySquare, distance);
        for (Square square : squares) {
            highlightSquare(square);
        }
    }

    /**
     * disable all turn event buttons
     */
    private void disableTurnEventButtons() {
        for (Button button : turnEventButtons) {
            setBtnEnabled(button, false);
        }
    }

    /**
     * wrapper to enable or disable a button
     * @param button the button to enable/disable
     * @param isVisible if true the button is enabled
     */
    private void setBtnEnabled(Button button, boolean isVisible) {
        if (button != null) {
            Platform.runLater(() -> {
                button.setDisable(!isVisible);
            });
        }
    }

    /**
     * Adds a node on the pane with the map
     * @param pane the pane that contains the map
     * @param node the node to add
     */
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

    /**
     * Given a player color returns its index
     * @param color
     * @return the index of that player
     */
    private int getIndex(String color) {
        switch (color) {
            case "GREEN":
                return 0;
            case "BLUE":
                return 1;
            case "PURPLE":
                return 2;
            case "WHITE":
                return 3;
            case "YELLOW":
                return 4;
        }
        return -1;
    }

    /**
     * Draws the token of the player on the map
     * @param pane the pane containing the map
     * @param p the player to draw
     */
    private void drawPlayerToken(GridPane pane, Player p) {
        Circle circle = new Circle(0.0d, 0.0d, 17.0d);
        int i = Client.getInstance().getPlayers().indexOf(p);
        circle.setFill(getPlayerColor(p.getStringColor()));
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
        circle.setOnMouseClicked(e -> {
            playerClicked(p);
            e.consume();
        });
        circle.setStrokeWidth(0d);
        circle.setStroke(Color.BLACK);
        circlePlayerMap.add(circle, p);
        Platform.runLater(() -> {
            addOnPane(pane, circle);
            fxSemaphore.release();
        });
    }

    /**
     * highlights the circle of a clickable player
     * @param c the circle to highlight
     */
    private void highlightCircle(Circle c) {
        clickableObjects.add(c);
        Platform.runLater(() -> c.setStrokeWidth(7d));
    }

    /**
     * Removes highlight from the circle
     * @param c the circle to remove the highlight from
     */
    private void undoHighlightCircle(Circle c) {
        Platform.runLater(() -> c.setStrokeWidth(0d));
    }

    /**
     * Colors in red a grid pane to indicate that it is clickable
     * @param gp the pane to highlight
     */
    private void highlightGridPane(GridPane gp) {
        clickableObjects.add(gp);
        Platform.runLater(() -> gp.setStyle("-fx-background-color: green; -fx-opacity: 0.5;"));
    }

    /**
     * Removes the color layer from the given grid pane
     * @param gp
     */
    private void undoHighlightGridPane(GridPane gp) {
        Platform.runLater(() -> gp.setStyle(""));
    }

    /**
     * Removes the color layer from all grid panes
     */
    private void undoHighlighAllGridPanes() {
        for (Square sq : Client.getInstance().getMap().getAllSquares()) {
            Coordinate coord = Client.getInstance().getMap().getSquareCoordinates(sq);
            undoHighlightGridPane(paneList.get(coord.getX()).get(coord.getY()));
        }
    }

    /**
     * colors a square in green to show that it can be clicked
     * @param square the square to color
     */
    private void highlightSquare(Square square) {
        Coordinate coord = Client.getInstance().getMap().getSquareCoordinates(square);
        GridPane clickable = paneList.get(coord.getX()).get(coord.getY());
        highlightGridPane(clickable);
    }

    /**
     * event thrown when a player is clicked
     * @param p the clicked player
     */
    private void playerClicked(Player p) {
        Circle clicked = circlePlayerMap.getSingleKey(p);
        if (canDoActionMap.get(ViewAction.SELECTPLAYER)) {
            if (!clickableObjects.contains(clicked)) {
                showMessage("You cannot click this player");
                return;
            }
            for (Circle circle : circlePlayerMap.getKeys()) {
                undoHighlightCircle(circle);
            }
            canDoActionMap.put(ViewAction.SELECTPLAYER, false);
            onSelectDone(p);
        } else if (canDoActionMap.get(ViewAction.SELECTSQUARE) || canDoActionMap.get(ViewAction.SELECTROOM)) {
            Square square = Client.getInstance().getMap().getPlayerPosition(p);
            squareClicked(square);
        }
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public synchronized void updateMapView(GameMap map) {
        Client client = Client.getInstance();
        //delete everything on map
        for (List<GridPane> paneRow : paneList) {
            for (GridPane gridPane : paneRow) {
                acquireLock();
                Platform.runLater(() -> {
                    gridPane.getChildren().clear();
                    fxSemaphore.release();
                });
            }
        }
        circlePlayerMap.clear();
        client.getPlayerCoordinateMap().clear();

        //UPDATE PLAYERS POSITIONS
        for (Player p : client.getPlayers()) {
            try {
                acquireLock();
                Coordinate c = map.getPlayerCoordinates(p);
                drawPlayerToken(paneList.get(c.getX()).get(c.getY()), p);   //INSIDE IT RELEASE LOCKS
                client.getPlayerCoordinateMap().put(p, c);
            } catch (PlayerNotOnMapException e) {
                fxSemaphore.release();
                Logger.getAnonymousLogger().info("Player " + p.getName() + " is not on map");
                client.getPlayerCoordinateMap().put(p, null);
            }
        }
        //REMOVE OLD WEAPONS
        for (SpawnPoint sp : client.getMap().getSpawnPoints()) {
            acquireLock();
            removeWeaponOnMapSpawnPoint(sp.getColor());    //INSIDE IT RELEASE LOCKS
        }
        //UPDATE AMMO'S AND WEAPONS ON MAP
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 3; y++) {
                Square square = map.getSquareByCoordinate(x, y);
                if (square == null) continue;
                if (square.isSpawnPoint()) {
                    SpawnPoint spawnPoint = (SpawnPoint) square;
                    //ADD NEW WEAPONS
                    for (WeaponCard card : spawnPoint.showCards()) {
                        acquireLock();
                        addWeaponOnMapSpawnPoint(card, spawnPoint.getColor());    //INSIDE IT RELEASE LOCKS
                    }
                } else {
                    AmmoPoint ammoPoint = (AmmoPoint) square;
                    AmmoCard ammoCard = ammoPoint.showCard();
                    if (ammoCard == null) continue;
                    acquireLock();
                    String imageName = String.format("ammo/%d%d%d%s.png", ammoCard.getRed(), ammoCard.getBlue(), ammoCard.getYellow(), ammoCard.hasPowerup() ? "y" : "n");
                    Image image = new Image(imageName);
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(45);
                    imageView.setFitHeight(45);
                    GridPane panetoadd = paneList.get(x).get(y);
                    Platform.runLater(() -> {
                        addOnPane(panetoadd, imageView);
                        fxSemaphore.release();
                    });
                }
            }
        }
    }

    /**
     * acquires lock on the semaphore and catches the exception
     */
    private void acquireLock() {
        try {
            fxSemaphore.acquire();
        } catch (InterruptedException e) {
            Logger.getAnonymousLogger().info(e.toString());
            Thread.currentThread().interrupt();
        }
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public synchronized void updatePlayerView(Player newPlayer) {
        acquireLock();
        Platform.runLater(() -> {
            //UPDATE AMMO
            Ammo myAmmo = newPlayer.getAmmo();
            redAmmoText.setText(Integer.toString(myAmmo.red));
            blueAmmoText.setText(Integer.toString(myAmmo.blue));
            yellowAmmoText.setText(Integer.toString(myAmmo.yellow));
            fxSemaphore.release();
        });

        //REMOVE OLD POWERUP IF ANY
        acquireLock();
        removeCardsToHand(powerUpSpaces);
        //ADD NEW POWERUP
        for (PowerUpCard powerUpCard : newPlayer.getPowerUpList()) {
            acquireLock();
            addCardToHand(powerUpCard, powerUpSpaces);
        }
        //REMOVE OLD WEAPONS IF ANY
        acquireLock();
        removeCardsToHand(weaponSpaces);
        //ADD NEW WEAPONS
        for (WeaponCard weaponCard : newPlayer.getWeaponList()) {
            acquireLock();
            addCardToHand(weaponCard, weaponSpaces);
        }

    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void onMark(Player markedPlayer) {
        if (markedPlayer.hasMarks())
            Platform.runLater(() -> showMessage(markedPlayer.getName() + " has been marked"));
        updateDamagedAndMarkedPlayer(markedPlayer);
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void onDamage(Player damagedPlayer) {
        showMessage(damagedPlayer.getName() + " has been damaged");
        updateDamagedAndMarkedPlayer(damagedPlayer);

        Player me = Client.getInstance().getPlayer();
        if (damagedPlayer.equals(me)) {
            List<PowerUpCard> playable = new ArrayList<>();
            for (PowerUpCard powerUpCard : me.getPowerUpList()) {
                if (powerUpCard.when.equals("on_damage_received")) {
                    playable.add(powerUpCard);
                }
            }
            if (playable.isEmpty()) return;
            if (me.getLastDamager() == null) return;
            Platform.runLater(() -> {
                PowerUpCard powerUpCard = choosePowerUpDialog("You have been damaged by " + me.getLastDamager().getName(), "Would you like to mark him?", playable);
                if (powerUpCard != null) {
                    Client.getInstance().getConnection().playPowerUp(powerUpCard.name, null, null);
                }
            });
        }
    }

    /**
     * when a damage or a mark is received, update the player tab by adding it: first remove everything and then add
     * it back
     * @param newPlayer
     */
    private void updateDamagedAndMarkedPlayer(Player newPlayer) {
        List<Player> players = Client.getInstance().getPlayers();
        Player oldPlayer = players.get(players.indexOf(newPlayer));
        //REMOVE DAMAGE
        if (oldPlayer != null) {
            removeAllDamageOnPlayer(oldPlayer);
        }
        //ADD DAMAGE
        int i = 0;
        for (Player from : newPlayer.getDamagedBy()) {
            drawDamageOnPlayer(newPlayer, from, i);
            i++;
        }
        //REMOVE MARKS
        if (oldPlayer != null) {
            removeAllMarksOnPlayer(oldPlayer);
        }
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
        //UPDATE SKULLS IN PLAYER PANE
        removeAllSkullsOnPlayerPane(newPlayer);
        addSkullsOnPlayerPane(newPlayer);
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void updatePoints(Map<Player, Integer> map) {
        for (Player p : Client.getInstance().getPlayers()) {
            int points = map.get(p);
            actualPointsList.get(Client.getInstance().getPlayers().indexOf(p)).setText("Actual Points: " + points);
        }
    }

    /**
     * adds skulls on the player that has been killed, now every kill gives less points
     * @param who
     */
    void addSkullsOnPlayerPane(Player who) {
        int numberOfDeath = 0;
        if (who.getTotalDamage() > 10) {
            numberOfDeath += 1;
        }
        numberOfDeath += who.getDeathCount();
        for (int i = 0; i < numberOfDeath; i++) {
            Ellipse ellipse = new Ellipse(222d + i * 51, 197, 20, 25);
            ellipse.setFill(Color.rgb(0, 0, 0));
            int index = getIndex(who.getStringColor());
            AnchorPane anchorPane = anchorPanePlayers.get(index);
            Platform.runLater(() -> anchorPane.getChildren().add(ellipse));
        }
    }

    /**
     * Removes all the skulls on the pane of a player
     * @param who the player whose skulls will be removed
     */
    void removeAllSkullsOnPlayerPane(Player who) {
        AnchorPane anchorPane = anchorPanePlayers.get(getIndex(who.getStringColor()));
        for (int i = 0; i < anchorPane.getChildren().size(); i++) {
            Node n = anchorPane.getChildren().get(i);
            if (n.getClass().equals(Ellipse.class)) {
                Platform.runLater(() -> anchorPane.getChildren().remove(n));
            }
        }
    }

    /**
     * adds black squares on the skulls of the pane that contains the remaining skulls, one is added for each death
     * @param newPlayer
     */
    void addSkullsOnMainPane(Player newPlayer) {
        int skulls = 0;
        if (newPlayer.getTotalDamage() > 10) {
            skulls += 1;
        }
        skulls += newPlayer.getSkullCount(Client.getInstance().getPlayers());
        for (int position = 0; position < skulls; position++) {
            Rectangle c = new Rectangle((double) (42 + position * 97), 100d, 60d, 105d);
            c.setFill(Color.rgb(0, 0, 0));
            Platform.runLater(() -> skullPane.getChildren().add(c));
        }
    }

    /**
     * Removes all the black squares from the main pane
     */
    void removeAllSkullOnMainPane() {
        for (int i = 0; i < skullPane.getChildren().size(); i++) {
            Node n = skullPane.getChildren().get(i);
            if (n.getClass().equals(Circle.class)) {
                Platform.runLater(() -> skullPane.getChildren().remove(n));
            }
        }
    }

    /**
     * Removes all damages on a player's board
     * @param p
     */
    void removeAllDamageOnPlayer(Player p) {
        if (getPlayerColor(p.getStringColor()) != null) {
            int index = getIndex(p.getStringColor());
            AnchorPane anchorPane = anchorPanePlayers.get(index);
            for (int i = 0; i < anchorPane.getChildren().size(); i++) {
                Node n = anchorPane.getChildren().get(i);
                if (n.getClass().equals(Circle.class)) {
                    Platform.runLater(() -> anchorPane.getChildren().remove(n));
                }
            }
        }
    }

    /**
     * draws new damage on a player board
     * @param to the damaged players, signals will go on its board
     * @param from the player who damages, the damage color will be his color
     * @param position the offset from which the damage must be drawn
     */
    void drawDamageOnPlayer(Player to, Player from, int position) {
        //When drawing a circle, first arg is X, second is Y, third is radius. 138px is the height of where the circle must be placed
        //Then, for every new damage, the circle must be on same height but trasled on X.
        Circle c = new Circle((double) (107 + position * 54), 120d, 17d);
        c.setFill(getPlayerColor(from.getStringColor()));
        int index = getIndex(to.getStringColor());
        AnchorPane anchorPane = anchorPanePlayers.get(index);
        Platform.runLater(() -> anchorPane.getChildren().add(c));
    }

    /**
     * Clears all marks on a player's board
     * @param p the player whose board will be cleared of the marks
     */
    void removeAllMarksOnPlayer(Player p) {
        if (getPlayerColor(p.getStringColor()) != null) {
            int index = getIndex(p.getStringColor());
            AnchorPane anchorPane = anchorPanePlayers.get(index);
            for (int i = 0; i < anchorPane.getChildren().size(); i++) {
                Node n = anchorPane.getChildren().get(i);
                if (n.getClass().equals(Rectangle.class)) {
                    Platform.runLater(() -> anchorPane.getChildren().remove(n));
                }
            }
        }
    }

    /**
     * Draws a mark on the player's boards
     * @param to the player receiving the mark
     * @param from the player giving the mark
     * @param position the offset of the mark
     */
    void drawMarkOnPlayer(Player to, Player from, int position) {
        //When drawing a circle, first arg is X, second is Y, third is radius. 138px is the height of where the circle must be placed
        //Then, for every new damage, the circle must be on same height but trasled on X.
        Rectangle rectangle = new Rectangle((470d + position * 42), 14d, 27d, 27d); //X,Y,L,H
        rectangle.setFill(getPlayerColor(from.getStringColor()));
        int index = getIndex(to.getStringColor());
        AnchorPane anchorPane = anchorPanePlayers.get(index);
        Platform.runLater(() -> anchorPane.getChildren().add(rectangle));
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void onNewPlayer(String playerName) {
        showMessage("Player " + playerName + " connected!");
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void onPlayerDisconnected(String name) {
        showMessage("Player " + name + " disconnected!");
    }

    public void powerUp1Clicked() {
        List<PowerUpCard> powerUpCards = Client.getInstance().getPlayer().getPowerUpList();
        if (powerUpCards.isEmpty()) {
            showMessage("Cannot click here");
            return;
        }
        PowerUpCard clickedPowerUp = powerUpCards.get(0);
        powerUpClicked(clickedPowerUp);
    }

    public void powerUp2Clicked() {
        List<PowerUpCard> powerUpCards = Client.getInstance().getPlayer().getPowerUpList();
        if (powerUpCards.size() < 2) {
            showMessage("Cannot click here");
            return;
        }
        PowerUpCard clickedPowerUp = powerUpCards.get(1);
        powerUpClicked(clickedPowerUp);
    }

    public void powerUp3Clicked() {
        List<PowerUpCard> powerUpCards = Client.getInstance().getPlayer().getPowerUpList();
        if (powerUpCards.size() < 3) {
            showMessage("Cannot click here");
            return;
        }
        PowerUpCard clickedPowerUp = powerUpCards.get(2);
        powerUpClicked(clickedPowerUp);
    }

    public void powerUp4Clicked() {
        List<PowerUpCard> powerUpCards = Client.getInstance().getPlayer().getPowerUpList();
        if (powerUpCards.size() < 4) {
            showMessage("Cannot click here");
            return;
        }
        PowerUpCard clickedPowerUp = powerUpCards.get(3);
        powerUpClicked(clickedPowerUp);
    }

    /**
     * when a powerup is clicked this event is fired
     * @param powerUpCard the powerup clicked
     */
    private void powerUpClicked(PowerUpCard powerUpCard) {
        Player me = Client.getInstance().getPlayer();
        if (canDoActionMap.get(ViewAction.CLICKPOWERUPSPAWN)) {
            canDoActionMap.put(ViewAction.CLICKPOWERUPSPAWN, false);
            gameController.spawn(powerUpCard);
        } else if (canDoActionMap.get(ViewAction.USEPOWERUP)) {
            Platform.runLater(() -> {
                setBtnEnabled(btnUsePowerUp, true);
                Ammo ammoToPay = null;
                PowerUpCard powerUpCardToPay = null;
                if (powerUpCard.hasPrice) {
                    ammoToPay = chooseAmmoDialog();
                    if (ammoToPay == null || ammoToPay.isEmpty()) {
                        List<PowerUpCard> payable = new ArrayList<>(me.getPowerUpList());
                        payable.remove(powerUpCard);
                        if (!payable.isEmpty()) {
                            powerUpCardToPay = choosePowerUpDialog(null, null, payable);
                        }
                    }
                }
                canDoActionMap.put(ViewAction.USEPOWERUP, false);
                gameController.playPowerUp(powerUpCard, ammoToPay, powerUpCardToPay);
            });
            showMessage("You played " + powerUpCard.getFullName());
        }
    }

    /**
     * dialog to open when paying for a powerup, only lets you select 1 cube of any ammo color
     * @return
     */
    private Ammo chooseAmmoDialog() {
        if (Client.getInstance().getPlayer().getAmmo().isEmpty()) return null;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Pay this powerup");
        alert.setHeaderText("This powerup costs 1 ammo of your choice");
        alert.setContentText("Click on one of the ammo buttons or the powerup button to pay with a powerup");

        List<ButtonType> btlist = new ArrayList<>();
        btlist.add(new ButtonType("Pay with powerup"));
        Player me = Client.getInstance().getPlayer();
        Ammo myAmmos = me.getAmmo();

        if (myAmmos.yellow > 0) {
            btlist.add(new ButtonType("Yellow"));
        }
        if (myAmmos.red > 0) {
            btlist.add(new ButtonType("Red"));
        }
        if (myAmmos.blue > 0) {
            btlist.add(new ButtonType("Blue"));
        }

        alert.getButtonTypes().setAll(btlist);
        Optional<ButtonType> result = alert.showAndWait();
        if (!result.isPresent() || result.get() == btlist.get(0)) return null;

        String stringResult = result.get().getText();

        switch (stringResult) {
            case "Yellow":
                return new Ammo(0, 0, 1);
            case "Blue":
                return new Ammo(0, 1, 0);
            case "Red":
                return new Ammo(1, 0, 0);
        }
        return null;
    }

    public void weaponClicked1() {
        weaponClicked(0);
    }

    public void weaponClicked2() {
        weaponClicked(1);
    }

    public void weaponClicked3() {
        weaponClicked(2);
    }

    /**
     * a weapon has been clicked. If you could click on it, the correct request is sent to the server and you cannot
     * click on it anymore
     * @param weaponIndex the index of the clicked weapon in the player's weapon list
     */
    private void weaponClicked(int weaponIndex){
        if(canDoActionMap.get(ViewAction.SHOOT)) {
            canDoActionMap.put(ViewAction.SHOOT, false);
            if (Client.getInstance().getPlayer().getWeaponList().isEmpty()) {
                showMessage("You cannot shoot.");
                gameController.finishCard();
            } else if (Client.getInstance().getPlayer().getWeaponList().size() > weaponIndex) {
                WeaponCard we = Client.getInstance().getPlayer().getWeaponList().get(weaponIndex);
                setActualWC(we);
                gameController.getEffects(we);
                showMessage("You clicked on " + we.getName());
                if(!we.isLoaded()) showMessage("You cannot shoot with an unloaded weapon");
            }
        }
    }

    /**
     * event fired when a square has been clicked, checks the action type and performs the right action
     * @param s the clicked square
     */
    private void squareClicked(Square s) {
        //check that it was clickable
        Coordinate coord = Client.getInstance().getMap().getSquareCoordinates(s);
        GridPane gridPane = paneList.get(coord.getX()).get(coord.getY());
        if (!clickableObjects.contains(gridPane)) {
            showMessage("You cannot click on this pane");
            return;
        }
        clickableObjects.clear();
        //-
        if (canDoActionMap.get(ViewAction.RUN)) {
            canDoActionMap.put(ViewAction.RUN, false);
            run(s);
        } else {
            Taggable tagged = s;
            if (canDoActionMap.get(ViewAction.SELECTROOM)) {
                RoomColor rc = s.getColor();
                tagged = rc;
                canDoActionMap.put(ViewAction.SELECTROOM, false);
            } else {
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

    /**
     * event fired when a weapon on a spawnpoint has been clicked, if it was clickable a dialog to pay for its grab is shown
     * and upon valid payment the user can grab it
     * @param color the color of the spawnpoint
     * @param position the offset of the weapon in the list
     */
    private void weaponOnSpawnPointClicked(RoomColor color, int position) {
        SpawnPoint spawnPoint = (SpawnPoint) Client.getInstance().getMap().getAllSquaresInRoom(color).stream().filter(Square::isSpawnPoint).findFirst().orElse(null);
        if (spawnPoint == null) {
            return;
        }
        Iterator<WeaponCard> it = spawnPoint.showCards().iterator();
        WeaponCard wc = null;
        while (position != 0) {
            wc = it.next();
            position--;
        }
        if (canDoActionMap.get(ViewAction.CHOOSESPAWNPOINTWEAPON)) {
            final WeaponCard weaponCard = wc;
            Platform.runLater(() -> {
                Player me = Client.getInstance().getPlayer();
                PowerUpCard toPay = choosePowerUpDialog();
                if (me.getWeaponList().size() == 3)
                    discardWeaponChoosingDialog(weaponCard, toPay);
                else
                    gameController.grab(weaponCard, toDiscard, toPay);
            });
        }
    }

    /**
     * If you are trying to grab a weapon this dialog is shown and lets you choose a weapon to discard to grab the new one
     * @param wc the weapon card you want to draw
     * @param payWith the powerup you want to pay with
     */
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

    /**
     * Gets the right image to put on the actions tab
     * @param actionType
     * @return the imageView correlated to the given action type
     */
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

    /**
     * Updates the action group button by adding the right action images on each request
     * @param actionType the current action type
     * @param btnNum the offset of the button
     */
    public void setTextAndEnableBtnActionGroup(ActionType actionType, int btnNum) {
        Platform.runLater(() -> {
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
    /**
     * {@inheritDoc}
     */
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
                if (index == btlist.size() - 1) {
                    gameController.finishCard();
                } else {
                    Effect chosenEffect = legitEffects.getLegitEffects().get(index);
                    //Would you like to pay with power up?
                    PowerUpCard toPay = null;
                    if (!chosenEffect.price.isEmpty()) toPay = choosePowerUpDialog();
                    gameController.playEffect(chosenEffect, toPay);
                }
            }
        });
    }

    /**
     * Shows a powerup dialog when you want to pay with a powerup
     * @return the chosen powerup
     */
    private PowerUpCard choosePowerUpDialog() {
        return choosePowerUpDialog(null, null, null);
    }

    /**
     * Shows a more flexible powerup dialog when you want to use a powerup in general
     * @param headerText the text of the header of the dialog
     * @param contentText the text of the content of the dialog
     * @param powerUpCards a list of powerup cards between the user can choose
     * @return the chosen powerup card or null
     */
    private PowerUpCard choosePowerUpDialog(String headerText, String contentText, List<PowerUpCard> powerUpCards) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Just a question...");
        if (headerText == null) headerText = "Do you want to use a power up to pay for this action?";
        if (contentText == null) contentText = "If yes, select one of yours, if not, please click no.";
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);


        List<ButtonType> btlist = new ArrayList<>();
        btlist.add(new ButtonType("No, I don't."));
        Player me = Client.getInstance().getPlayer();

        if (powerUpCards == null) {
            powerUpCards = me.getPowerUpList();
        }
        if (powerUpCards == null || powerUpCards.isEmpty()) return null;

        for (PowerUpCard powerUpCard : powerUpCards) {
            btlist.add(new ButtonType(powerUpCard.getFullName()));
        }

        alert.getButtonTypes().setAll(btlist);
        Optional<ButtonType> result = alert.showAndWait();
        if (!result.isPresent() || result.get() == btlist.get(0)) return null;

        int index = btlist.indexOf(result.get());
        return powerUpCards.get(index - 1);
    }

    public void setActualWC(WeaponCard wc) {
        this.actualWC = wc;
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void continueWeapon() {
        if (actualWC != null) {
            gameController.getEffects(actualWC);
        }
    }

    @Override
    /**
     * {@inheritDoc}
     */
    /**
     * {@inheritDoc}
     */
    public void onEndWeapon() {
        this.setActualWC(null);
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void onEndMatch(List<Player> winners, Map<Player, Integer> pointers) {
        disableActionGroupButtons();
        disableTurnEventButtons();

        for (Player player : winners) {
            showMessage(String.format("%s WON THE MATCH WITH %dPOINTS!!!!", player.getName(), pointers.get(player)));
        }

        int toastMsgTime = 3500; //2.5 seconds
        int fadeInTime = 500; //0.5 seconds
        int fadeOutTime = 500; //0.5 seconds
        Platform.runLater(() -> ToastView.makeText(ScreenController.getInstance().getActualStage(), "This match has ended, check game messages!", toastMsgTime, fadeInTime, fadeOutTime));
    }

    @Override
    /**
     * {@inheritDoc}
     */
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
                    highlightSquare((Square) t);
                }
                canDoActionMap.put(ViewAction.SELECTSQUARE, true);
                break;
        }

        if (selectable.isOptional()) {
            setBtnEnabled(btnEndSelect, true);
        }
    }

    /**
     * The tag has been selected, send the tag to server
     */
    public void endSelectTag() {
        onSelectDone(null);
    }

    /**
     * sends the tag to the server
     * @param selected
     */
    private void onSelectDone(Taggable selected) {
        clickableObjects.clear();
        setBtnEnabled(btnEndSelect, false);
        gameController.tagElement(selected, isShooting);

    }

    /**
     * Given a color string return the actual rgb of the color
     * @param circleColor the color as a string
     * @return a color object
     */
    public Color getPlayerColor(String circleColor) {
        switch (circleColor) {
            case "GREEN":
                return Color.rgb(50, 190, 55);
            case "BLUE":
                return Color.rgb(25, 135, 235);
            case "PURPLE":
                return Color.rgb(180, 25, 225);
            case "WHITE":
                return Color.rgb(255, 242, 246);
            case "YELLOW":
                return Color.rgb(200, 180, 30);
        }
        return null;
    }


}
