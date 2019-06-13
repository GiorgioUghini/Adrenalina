package network;

import models.card.Effect;
import models.card.PowerUpCard;
import models.card.Taggable;
import models.card.WeaponCard;
import models.map.RoomColor;
import models.map.Square;
import models.player.Ammo;
import models.turn.ActionType;
import models.turn.TurnEvent;
import models.turn.TurnType;
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
    Response playEffect(String token, Effect effect, Ammo ammo, PowerUpCard powerUpCard) throws  RemoteException;
    Response finishCard(String token) throws  RemoteException;
    Response tagElement(String token, Taggable taggable) throws  RemoteException;
    Response endTurn(String token) throws  RemoteException;
    Response action(String token, ActionType actionType) throws RemoteException;
    Response grab(String token) throws  RemoteException;
    Response run(String token, TurnEvent turnEvent, Square square) throws  RemoteException;
    Response reload(String token, List<WeaponCard> weapons) throws  RemoteException;
    Response playPowerUp(String token, String powerUpName, String color) throws  RemoteException;
}