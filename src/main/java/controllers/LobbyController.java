package controllers;

import config.Constants;
import errors.InvalidConnectionTypeException;
import network.Connection;
import network.ConnectionType;

import java.io.IOException;
import java.rmi.NotBoundException;

public class LobbyController {
    public void connect(String username, ConnectionType type) throws IOException, NotBoundException, InterruptedException {
        Connection connection = Connection.getIstance();
        if(type == ConnectionType.Socket){
            connection.initSocket(Constants.HOSTNAME, Constants.PORT);
        }
        else if(type == ConnectionType.RMI){
            connection.initRMI();
        }
        else
        {
            throw new InvalidConnectionTypeException();
        }

    }
}
