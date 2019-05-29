package views;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import controllers.MenuController;
import controllers.ResourceController;
import controllers.ScreenController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import network.Client;
import network.ConnectionType;
import utils.Console;

public class MenuViewGUI implements Initializable, MenuView {

    @FXML
    private TextField usernameLabel;
    @FXML
    private Text txtMsg;
    @FXML
    private Text infoLabel;
    @FXML
    private ComboBox<String> connectionSelector;
    @FXML
    private Button registerButton;
    @FXML
    private Button joinButton;
    @FXML
    private ListView<String> listView;
    @FXML
    private PasswordField passwordLabel;


    private ConnectionType connectionType = ConnectionType.RMI;
    private MenuController menuController;
    private String username;

    @FXML
    private void register(final ActionEvent event) {
        event.consume();
        this.createConnection();
    }

    public MenuViewGUI() {
        this.menuController = new MenuController();
    }

    @Override
    public void initialize(java.net.URL arg0, ResourceBundle arg1) {
        if (connectionSelector != null) { //If it is not null, we are in register stage, if it is: we are in waitingPlayerLobby stage.
            connectionSelector.getItems().setAll("RMI", "Socket");
            connectionSelector.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> selected, String oldConnection, String newConnection) {
                    if (newConnection.equals("RMI")) {
                        connectionType = ConnectionType.RMI;
                    } else {
                        connectionType = ConnectionType.Socket;
                    }
                }
            });
        } else {
            Client.getInstance().setCurrentView(this);
            menuController.getWaitingPlayer();
        }
    }

    @Override
    public void startView() {
        StartGUI.main(new String[]{});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createConnection() {
        try {
            menuController.createConnection(connectionType);
        } catch (Exception e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.SEVERE, "An exception was thrown:", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connectionCreated() {
        ScreenController.getInstance().activate("WaitingRoom.fxml");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerPlayer() {
        this.username = usernameLabel.getText();
        menuController.registerPlayer(usernameLabel.getText(), passwordLabel.getText());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registrationCompleted(List<String> players) {
        String toastMsg = "You joined the waiting room!";
        int toastMsgTime = 2500; //2.5 seconds
        int fadeInTime = 500; //0.5 seconds
        int fadeOutTime = 500; //0.5 seconds
        Toast.makeText(ScreenController.getInstance().getActualStage(), toastMsg, toastMsgTime, fadeInTime, fadeOutTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onNewPlayer(String playerName) {
        Platform.runLater(() -> listView.getItems().add(playerName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPlayerDisconnected(String playerName) {
        Platform.runLater(() -> listView.getItems().remove(playerName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startGame() {
        ScreenController.getInstance().activate("GameRoom.fxml");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printError(String error) {
        Console.println(error);
    }

    @Override
    public void showMessage(String message) {
        int toastMsgTime = 2500; //2.5 seconds
        int fadeInTime = 500; //0.5 seconds
        int fadeOutTime = 500; //0.5 seconds
        Platform.runLater(() -> Toast.makeText(ScreenController.getInstance().getActualStage(), message, toastMsgTime, fadeInTime, fadeOutTime));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showWaitingPlayerList(List<String> waitingPlayersUsername) {
        for (String p : waitingPlayersUsername) {
            Platform.runLater(() -> listView.getItems().add(p));
        }
    }

    public void mapChosen(int mapNum) {
        Client.getInstance().setMapNum(mapNum);
        startGame();
    }

    @Override
    public void chooseMap(String username) {
        if (username.equals(this.username)) {
            boolean done = false;
            Platform.runLater(() -> {
                int i = 0;
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Time to start the game!");
                alert.setHeaderText("Are you ready? We only need you to select the map for this match!");
                alert.setContentText("C'mon, you're the first player! Choose one of these maps...");

                ButtonType buttonTypeOne = new ButtonType("One");
                ButtonType buttonTypeTwo = new ButtonType("Two");
                ButtonType buttonTypeThree = new ButtonType("Three");
                ButtonType buttonTypeFour = new ButtonType("Four");

                alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree, buttonTypeFour);
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == buttonTypeOne) {
                    i = 0;
                } else if (result.get() == buttonTypeTwo) {
                    i = 1;
                } else if (result.get() == buttonTypeThree) {
                    i = 2;
                } else if (result.get() == buttonTypeFour) {
                    i = 3;
                } else {
                    //TODO: User closed the window
                }
                menuController.chooseMap(i);
            });
        }
        //TODO: ADD TIMER (IN SERVER), AFTER IT TIMERS CHOOSE RANDOMLY
    }
}