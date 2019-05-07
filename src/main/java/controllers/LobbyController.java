package controllers;

import config.Constants;
import errors.InvalidConnectionTypeException;
import network.Connection;
import network.ConnectionType;
import network.RemoteMethodsInterface;
import network.ResponseHandler;
import network.requests.RegisterPlayerRequest;
import network.responses.RegisterPlayerResponse;

import java.io.IOException;
import java.rmi.NotBoundException;

public class LobbyController {
    public void createConnection(ConnectionType type) throws IOException, NotBoundException, InterruptedException {
        Connection connection = Connection.getInstance();
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

    public void registerPlayer(String username) throws IOException {
        if(Connection.getInstance().getType() == ConnectionType.Socket){
            RegisterPlayerRequest request = new RegisterPlayerRequest(username);
            Connection.getInstance().getOutputStream().writeObject(request);
        }
        else if(Connection.getInstance().getType() == ConnectionType.RMI){
            RemoteMethodsInterface remoteMethods = Connection.getInstance().getRemoteMethods();
            RegisterPlayerResponse response = remoteMethods.registerPlayer(username);
            response.handle(new ResponseHandler());
        }
    }
}
