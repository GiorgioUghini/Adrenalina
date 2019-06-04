package network;

import models.Match;
import network.requests.*;
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
        return remoteMethods.registerPlayer(request.username, request.password, request.getToken());
    }

    @Override
    public Response handle(WaitingPlayerRequest request) throws RemoteException {
        return remoteMethods.waitingPlayer();
    }

    @Override
    public Response handle(ChooseMapRequest request) throws RemoteException {
        return remoteMethods.chooseMap(request.getToken(), request.map);
    }

    @Override
    public Response handle(CardEffectsRequest request) throws RemoteException {
        return remoteMethods.cardEffects(request.getToken(), request.cardName);
    }

    @Override
    public Response handle(DrawPowerUpRequest request) throws RemoteException {
        return remoteMethods.drawPowerUp(request.getToken());
    }

}
