package controllers;

import network.*;
import utils.Constants;
import errors.InvalidConnectionTypeException;
import network.requests.RegisterPlayerRequest;
import network.responses.RegisterPlayerResponse;

import java.io.IOException;
import java.rmi.NotBoundException;

public class MenuController {
    public void createConnection(ConnectionType type) throws IOException, NotBoundException, InterruptedException {
        Connection connection = new ConnectionFactory().getConnection(type);
        Client.getInstance().setConnection(connection);
        connection.init();
    }

    public void registerPlayer(String username) {
        Client.getInstance().getConnection().registerPlayer(username);
    }
}
