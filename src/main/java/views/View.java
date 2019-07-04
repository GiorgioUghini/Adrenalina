package views;

public interface View {
    /**
     * Prints an error
     */
    void printError(String error);

    void showMessage(String message);

    /**
     * Event when a new player joins the lobby
     *
     * @param playerName the name of the player that just joined
     */
    void onNewPlayer(String playerName);

    /**
     * Event when a player leaves the lobby
     *
     * @param name the name of the leaving player
     */
    void onPlayerDisconnected(String name);
}
