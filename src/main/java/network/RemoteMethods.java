package network;

import errors.InvalidInputException;
import errors.WrongPasswordException;
import models.Lobby;
import models.Match;
import models.card.*;
import models.map.GameMap;
import models.map.RoomColor;
import models.map.SpawnPoint;
import models.player.Player;
import models.turn.TurnEvent;
import network.responses.*;
import network.updates.MapChosenUpdate;
import network.updates.NewPlayerUpdate;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class RemoteMethods extends UnicastRemoteObject implements RemoteMethodsInterface {
    RemoteMethods() throws RemoteException {
    }

    @Override
    public List<Response> longPolling(String token) {
        RMIWrapper rmiWrapper = Server.getInstance().getConnection().getRMIWrapper(token);
        rmiWrapper.ping();
        List<Response> updates = new LinkedList<>(rmiWrapper.getUpdates());
        rmiWrapper.clearUpdates();
        return updates;
    }

    @Override
    public String handshake() throws RemoteException {
        RMIWrapper rmiWrapper = new RMIWrapper();
        String token = Server.getInstance().getConnection().getToken(rmiWrapper);
        RMIStatusTask rmiStatusTask = new RMIStatusTask(rmiWrapper);
        rmiWrapper.setRMIStatusTask(rmiStatusTask);
        Timer timer = new Timer();
        timer.schedule(rmiStatusTask, 0, 1000);
        return token;
    }

    @Override
    public Response registerPlayer(String username, String password, String token) {
        Lobby lobby = Server.getInstance().getLobby();
        Player player = lobby.getPlayerByUsername(username);
        if (player != null) {
            if (player.isOnline()) {
                return new ErrorResponse(new InvalidInputException());
            }
            if (!player.getPassword().equals(password))
                return new ErrorResponse(new WrongPasswordException());

            player.reconnect();
            return new ReconnectPlayerResponse(player);
        } else {
            player = lobby.registerPlayer(username, password, token);
            Server.getInstance().getConnection().addUpdateUnregisteredPlayers(new NewPlayerUpdate(player.getName()));
            lobby.addUpdateWaitingPlayers(new NewPlayerUpdate(player.getName()));
            return new RegisterPlayerResponse(player);
        }
    }

    @Override
    public ValidActionsResponse validActions(String token) {
        Lobby lobby = Server.getInstance().getLobby();
        Player currentPlayer = lobby.getPlayer(token);
        Match currentMatch = lobby.getMatch(currentPlayer);
        Set actions = currentMatch.getPossibleAction(currentPlayer);
        return new ValidActionsResponse(actions);
    }

    @Override
    public WaitingPlayerResponse waitingPlayer() throws RemoteException {
        List<String> waitingPlayers = Server.getInstance().getLobby().getWaitingPlayersUsername();
        return new WaitingPlayerResponse(waitingPlayers);
    }

    @Override
    public Response chooseMap(String token, int map) throws RemoteException {
        Server.getInstance().getLobby().getMatch(token).createMap(map);
        Server.getInstance().getLobby().getMatch(token).addUpdate(new MapChosenUpdate(map));
        return new ChooseMapResponse();
    }

    @Override
    public Response drawPowerUp(String token) throws RemoteException {
       Match match = Server.getInstance().getLobby().getMatch(token);
       PowerUpCard card = (PowerUpCard) match.drawPowerUp();
       match.doAction(TurnEvent.DRAW);
       return new DrawPowerUpResponse(card);
    }

    @Override
    public Response spawnPlayer(String token, RoomColor color) throws RemoteException {
        Player player = Server.getInstance().getLobby().getPlayer(token);
        Match match = Server.getInstance().getLobby().getMatch(player);
        GameMap map = match.getMap();
        List<PowerUpCard> powerUpCards = player.getPowerUpList();
        if(!powerUpCards.stream().map(c -> c.color).collect(Collectors.toList()).contains(color)){
            return new ErrorResponse(new InvalidInputException("You're cheating! You do not have a power up of that color"));
        }
        SpawnPoint spawnPoint = map.getSpawnPoints().stream().filter(p -> p.getColor() == color).findFirst().orElse(null);
        map.spawnPlayer(player,spawnPoint);
        match.doAction(TurnEvent.SPAWN);
        return new SpawnPlayerResponse();
    }

    @Override
    public Response cardEffects(String token, String cardName) throws RemoteException {
        Player player = Server.getInstance().getLobby().getPlayer(token);
        WeaponCard card = player.getWeaponList().stream().filter(w -> w.name.equals(cardName)).findFirst().orElse(null);
        player.playWeapon(card);
        LegitEffects legitEffects = player.getWeaponEffects();
        return new CardEffectsResponse(legitEffects);
    }

    @Override
    public Response playEffect(String token, Effect effect) throws RemoteException {
        Player player = Server.getInstance().getLobby().getPlayer(token);
        player.playWeaponEffect(effect);
        Action action;
        while ((action = player.playNextWeaponAction()) != null){
            if(action.type == ActionType.SELECT && !action.select.auto){
                Selectable selectable = player.getActiveWeapon().getSelectable();
                return new SelectResponse(selectable);
            }
        }
        LegitEffects legitEffects = player.getWeaponEffects();
        return legitEffects.getLegitEffects().isEmpty() ? new FinishCardResponse() : new FinishEffectResponse();
    }

    @Override
    public Response finishCard(String token) throws RemoteException {
        Player player = Server.getInstance().getLobby().getPlayer(token);
        player.resetWeapon();
        return new FinishCardResponse();
    }

    @Override
    public Response tagElement(String token, Taggable taggable) throws RemoteException {
        Player player = Server.getInstance().getLobby().getPlayer(token);
        WeaponCard card = player.getActiveWeapon();
        card.select(taggable);
        Action action;
        while ((action = player.playNextWeaponAction()) != null){
            if(action.type == ActionType.SELECT && !action.select.auto){
                Selectable selectable = player.getActiveWeapon().getSelectable();
                return new SelectResponse(selectable);
            }
        }
        LegitEffects legitEffects = player.getWeaponEffects();
        return legitEffects.getLegitEffects().isEmpty() ? new FinishCardResponse() : new FinishEffectResponse();
    }


}