package controllers;

import network.Client;
import network.Connection;
import network.ConnectionFactory;
import network.ConnectionType;
import views.MenuView;

public class MenuController {
    /**
     * creates a connection of the given type
     *
     * @param type socket or rmi
     */
    public void createConnection(ConnectionType type) {
        Connection connection = new ConnectionFactory().getConnection(type);
        Client.getInstance().setConnection(connection);
        connection.init();
        ((MenuView) Client.getInstance().getCurrentView()).connectionCreated();
    }

    /**
     * Registers a player creating a new thread
     *
     * @param username
     * @param password
     */
    public void registerPlayer(String username, String password) {
        new Thread(() -> Client.getInstance().getConnection().registerPlayer(username, password)).start();
    }

    /**
     * asks the server for a list of the waiting players
     */
    public void getWaitingPlayer() {
        Client.getInstance().getConnection().getWaitingPlayer();
    }

    /**
     * Send the server a request for the map
     *
     * @param mapNum (0-4)
     */
    public void chooseMap(int mapNum) {
        Client.getInstance().getConnection().chooseMap(mapNum);
    }
}
