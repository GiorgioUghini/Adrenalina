package views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface MenuView extends View {
    /** starts the menu view **/
    void startView();
    /** ask if socket or rmi, then calls the controller */
    void createConnection();
    /** After a successful connection creation, notify the user */
    void connectionCreated();
    /** Continue asking for the username until it matches the requirements, then sends it to server */
    void registerPlayer() throws IOException;
    /** After a successful registration of the user, receive a complete list of the users in the lobby
     * @param players a list of all the users in the lobby, included myself */
    void registrationCompleted(List<String> players);
    /** Event when a new player joins the lobby
     * @param playerName the name of the player that just joined */
    void onNewPlayer(String playerName);
    /** Event when a player leaves the lobby
     * @param name the name of the leaving player*/
    void onPlayerDisconnected(String name);
    /** Notify the user that game is starting, the controller will close this view and create a gameView */
    void startGame();
}
