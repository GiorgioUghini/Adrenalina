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

    @Override
    public Response handle(SpawnPlayerRequest request) throws RemoteException {
        return remoteMethods.spawnPlayer(request.getToken(), request.color);
    }

    @Override
    public Response handle(PlayEffectRequest request) throws RemoteException {
        return remoteMethods.playEffect(request.getToken(), request.effect, request.ammo, request.powerUpCard);
    }

    @Override
    public Response handle(TagElementRequest request) throws RemoteException {
        return remoteMethods.tagElement(request.getToken(), request.taggable);
    }

    @Override
    public Response handle(PowerUpTagElementRequest request) throws RemoteException {
        return remoteMethods.powerUpTagElement(request.getToken(), request.taggable);
    }

    @Override
    public Response handle(FinishCardRequest request) throws RemoteException {
        return remoteMethods.finishCard(request.getToken());
    }

    @Override
    public Response handle(EndTurnRequest request) throws RemoteException {
        return remoteMethods.endTurn(request.getToken());
    }

    @Override
    public Response handle(GrabRequest request) throws RemoteException {
        return remoteMethods.grab(request.getToken(), request.drawn, request.toRelease);
    }

    @Override
    public Response handle(RunRequest request) throws RemoteException {
        return remoteMethods.run(request.getToken(), request.turnEvent, request.square);
    }

    @Override
    public Response handle(ReloadRequest request) throws RemoteException {
        return remoteMethods.reload(request.getToken(), request.weapons);
    }

    @Override
    public Response handle(TurnActionRequest request) throws RemoteException {
        return remoteMethods.action(request.getToken(), request.actionType);
    }

    @Override
    public Response handle(PlayPowerUpRequest request) throws RemoteException {
        return remoteMethods.playPowerUp(request.getToken(), request.powerUpName, request.ammo, request.powerUpAmmo);
    }

}
