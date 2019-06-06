package network;

import models.card.Effect;
import models.card.Taggable;
import models.map.RoomColor;
import network.responses.RegisterPlayerResponse;
import network.responses.ValidActionsResponse;
import network.responses.WaitingPlayerResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteMethodsInterface extends Remote {
    List<Response> longPolling(String token) throws RemoteException;
    String handshake() throws RemoteException;
    Response registerPlayer(String username, String password, String token) throws RemoteException;
    Response validActions(String token) throws RemoteException;
    Response waitingPlayer() throws RemoteException;
    Response chooseMap(String token, int map) throws RemoteException;
    Response cardEffects(String token, String cardName) throws RemoteException;
    Response drawPowerUp(String token) throws  RemoteException;
    Response spawnPlayer(String token, RoomColor color) throws  RemoteException;
    Response playEffect(String token, Effect effect) throws  RemoteException;
    Response finishCard(String token) throws  RemoteException;
    Response tagElement(String token, Taggable taggable) throws  RemoteException;
}