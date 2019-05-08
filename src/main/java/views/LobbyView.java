package views;

import java.io.IOException;

public interface LobbyView {
    /** ask if socket or rmi, then calls the controller */
    public void createConnection();
    /** Continue asking for the username until it matches the requirements, then sends it to server */
    public void registerPlayer() throws IOException;
    /** Close this view and creates a gameView */
    public void startGame();
    /** Prints an error */
    public void printError();
}
