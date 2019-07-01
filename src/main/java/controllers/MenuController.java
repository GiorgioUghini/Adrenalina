package controllers;

import network.Client;
import network.Connection;
import network.ConnectionFactory;
import network.ConnectionType;
import views.MenuView;

public class MenuController {
    public void createConnection(ConnectionType type) {
        Connection connection = new ConnectionFactory().getConnection(type);
        Client.getInstance().setConnection(connection);
        connection.init();
        ((MenuView) Client.getInstance().getCurrentView()).connectionCreated();
    }

    public void registerPlayer(String username, String password) {
        new Thread(() -> Client.getInstance().getConnection().registerPlayer(username, password)).start();
    }

    public void getWaitingPlayer() {
        Client.getInstance().getConnection().getWaitingPlayer();
    }

    public void chooseMap(int mapNum) {
        Client.getInstance().getConnection().chooseMap(mapNum);
    }
}
