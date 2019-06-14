package views;

import java.io.IOException;
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
    /** Notify the user that game is starting, the controller will close this view and create a gameView */
    void startGame();
    /** Show who is waiting when a player first set up his connection **/
    void showWaitingPlayerList(List<String> waitingPlayerUsernames);
    /** Map is chosen, we can start the game
     * @param map which map to use
     * **/
    void mapChosen(int map);
    /** Need to choose the map
     * @param username who should choose
     */
    void chooseMap(String username);
}
