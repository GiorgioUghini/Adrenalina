package controllers;

import network.*;
import utils.Constants;
import errors.InvalidConnectionTypeException;
import network.requests.RegisterPlayerRequest;
import network.responses.RegisterPlayerResponse;
import views.MenuView;

import java.io.IOException;
import java.rmi.NotBoundException;

public class MenuController {
    public void createConnection(ConnectionType type) {
        Connection connection = new ConnectionFactory().getConnection(type);
        Client.getInstance().setConnection(connection);
        connection.init();
        ((MenuView) Client.getInstance().getCurrentView()).connectionCreated();
    }

    public void registerPlayer(String username, String password) {
        //TODO: Do not block GUI while doing RMI network operations
        Client.getInstance().getConnection().registerPlayer(username, password);
    }

    public void getWaitingPlayer() {
        Client.getInstance().getConnection().getWaitingPlayer();
    }

    public void chooseMap(int mapNum) {
        Client.getInstance().getConnection().chooseMap(mapNum);
    }
}
