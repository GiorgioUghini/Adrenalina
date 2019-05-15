package views;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import controllers.MenuController;
import controllers.ScreenController;
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
    private Text waitingPlayerLabel;
    @FXML
    private Text connectedLabel;

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
        waitingPlayerLabel.setText("Player waiting: " + players.size());
        connectedLabel.setText("Connected: true");
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
        //TODO: It seems that you must be on the same thread of JavaFX to show toasts... Socket isn't!
        int toastMsgTime = 2500; //2.5 seconds
        int fadeInTime = 500; //0.5 seconds
        int fadeOutTime= 500; //0.5 seconds
        Toast.makeText(ScreenController.getInstance().getActualStage(), message, toastMsgTime, fadeInTime, fadeOutTime);
    }
}