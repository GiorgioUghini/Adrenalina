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
        Connection connection = new Connection();
        Client.getInstance().setConnection(connection);
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
        Connection connection  = Client.getInstance().getConnection();
        if(connection.getType() == ConnectionType.Socket){
            RegisterPlayerRequest request = new RegisterPlayerRequest(username);
            connection.getOutputStream().writeObject(request);
        }
        else if(connection.getType() == ConnectionType.RMI){
            RemoteMethodsInterface remoteMethods = connection.getRemoteMethods();
            RegisterPlayerResponse response = remoteMethods.registerPlayer(username);
            response.handle(new ResponseHandler());
        }
    }
}
