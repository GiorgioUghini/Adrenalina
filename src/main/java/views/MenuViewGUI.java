package views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import controllers.MenuController;
import controllers.ScreenController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import network.ConnectionType;
import utils.Console;
import javafx.scene.control.ListView;

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
    private ListView listView;

    private ConnectionType connectionType = ConnectionType.RMI;
    private MenuController menuController;
    private List<String> players = new ArrayList<>();
    private String username = null;

    @FXML
    private void register(final ActionEvent event) {
        event.consume();
        username = usernameLabel.getText();
        this.createConnection();
    }

    public MenuViewGUI(){
        this.menuController = new MenuController();
    }

    @Override
    public void initialize(java.net.URL arg0, ResourceBundle arg1) {
        if (connectionSelector!=null) { //If it is not null, we are in register stage, if it is: we are in waitingPlayerLobby stage.
            connectionSelector.getItems().setAll("RMI", "Socket");
            connectionSelector.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override public void changed(ObservableValue<? extends String> selected, String oldConnection, String newConnection) {
                    if (newConnection.equals("RMI")) {
                        connectionType = ConnectionType.RMI;
                    } else {
                        connectionType = ConnectionType.Socket;
                    }
                }
            });
        }
    }

    @Override
    public void startView() {
        StartGUI.main(new String[] {});
    }

    /**{@inheritDoc}*/
    @Override
    public void createConnection() {
        try {
            menuController.createConnection(connectionType);
        } catch (Exception e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.SEVERE, "An exception was thrown:", e);
        }
        registerPlayer();
    }

    /**{@inheritDoc}*/
    @Override
    public void registerPlayer() {
        menuController.registerPlayer(username);
    }

    /**{@inheritDoc}*/
    @Override
    public void registrationCompleted(List<String> players){
        String toastMsg = "You joined the waiting room!";
        int toastMsgTime = 2500; //2.5 seconds
        int fadeInTime = 500; //0.5 seconds
        int fadeOutTime= 500; //0.5 seconds
        Toast.makeText(ScreenController.getInstance().getActualStage(), toastMsg, toastMsgTime, fadeInTime, fadeOutTime);
    }

    /**{@inheritDoc}*/
    @Override
    public void onNewPlayer(String playerName){
        Console.println(playerName + " joined the Lobby");
        players.add(playerName);
    }

    /**{@inheritDoc}*/
    @Override
    public void onPlayerDisconnected(String name){
        Console.println(name + "left the room");
        players.remove(name);
    }

    /**{@inheritDoc}*/
    @Override
    public void startGame() {
        Console.println("The game is starting");
    }

    /**{@inheritDoc}*/
    @Override
    public void printError(String error) {
        Console.println(error);
    }

    @Override
    public void showMessage(String message) {
        int toastMsgTime = 2500; //2.5 seconds
        int fadeInTime = 500; //0.5 seconds
        int fadeOutTime= 500; //0.5 seconds
        Platform.runLater(() -> Toast.makeText(ScreenController.getInstance().getActualStage(), message, toastMsgTime, fadeInTime, fadeOutTime));
        ScreenController.getInstance().activate("WaitingRoom.fxml");
    }
}