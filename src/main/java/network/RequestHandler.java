package network;

import models.Match;
import network.requests.RegisterPlayerRequest;
import network.requests.ValidActionsRequest;
import network.requests.WaitingPlayerRequest;
import network.responses.ErrorResponse;
import network.responses.RegisterPlayerResponse;

import java.lang.management.MemoryUsage;
import java.rmi.RemoteException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestHandler implements RequestHandlerInterface {

    private RemoteMethods remoteMethods;

    public RequestHandler() throws RemoteException {
        remoteMethods = new RemoteMethods();
    }

    @Override
    public Response handle(ValidActionsRequest request) throws RemoteException {
        return remoteMethods.validActions(request.getToken());
    }

    @Override
    public Response handle(RegisterPlayerRequest request) throws RemoteException {
        return remoteMethods.registerPlayer(request.username, request.getToken());
    }

    @Override
    public Response handle(WaitingPlayerRequest request) throws RemoteException {
        return remoteMethods.waitingPlayer();
    }

}
