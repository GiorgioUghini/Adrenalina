package network;

import errors.CheatException;
import errors.InvalidInputException;
import errors.WrongPasswordException;
import models.Lobby;
import models.Match;
import models.card.*;
import models.map.GameMap;
import models.map.RoomColor;
import models.map.SpawnPoint;
import models.map.Square;
import models.player.Ammo;
import models.player.Player;
import models.turn.TurnEvent;
import models.turn.TurnType;
import network.responses.*;
import network.updates.MapChosenUpdate;
import network.updates.MapUpdate;
import network.updates.NewPlayerUpdate;
import network.updates.StartGameUpdate;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class RemoteMethods extends UnicastRemoteObject implements RemoteMethodsInterface {
    RemoteMethods() throws RemoteException {
    }

    @Override
    public synchronized List<Response> longPolling(String token) {
        RMIWrapper rmiWrapper = Server.getInstance().getConnection().getRMIWrapper(token);
        rmiWrapper.ping();
        List<Response> updates = new LinkedList<>(rmiWrapper.getUpdates());
        rmiWrapper.clearUpdates();
        return updates;
    }

    @Override
    public synchronized String handshake() throws RemoteException {
        RMIWrapper rmiWrapper = new RMIWrapper();
        String token = Server.getInstance().getConnection().getToken(rmiWrapper);
        RMIStatusTask rmiStatusTask = new RMIStatusTask(rmiWrapper);
        rmiWrapper.setRMIStatusTask(rmiStatusTask);
        Timer timer = new Timer();
        timer.schedule(rmiStatusTask, 0, 1000);
        return token;
    }

    @Override
    public synchronized Response registerPlayer(String username, String password, String token) {
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
    public synchronized ValidActionsResponse validActions(String token) {
        Lobby lobby = Server.getInstance().getLobby();
        Player currentPlayer = lobby.getPlayer(token);
        Match currentMatch = lobby.getMatch(currentPlayer);
        boolean isNewActions = currentMatch.getCurrentActionType() == null;
        Map<models.turn.ActionType,  List<TurnEvent>>  actions = currentMatch.getPossibleAction(currentPlayer);
        return new ValidActionsResponse(actions, isNewActions);
    }

    @Override
    public synchronized WaitingPlayerResponse waitingPlayer() throws RemoteException {
        List<String> waitingPlayers = Server.getInstance().getLobby().getWaitingPlayersUsername();
        return new WaitingPlayerResponse(waitingPlayers);
    }

    @Override
    public synchronized Response chooseMap(String token, int map) throws RemoteException {
        Player player = Server.getInstance().getLobby().getPlayer(token);
        Match match = Server.getInstance().getLobby().getMatch(player);
        match.createMap(map);
        match.addUpdate(new MapChosenUpdate(match.getMap(), map));
        match.addUpdate(new StartGameUpdate(match.getPlayers()));
        return new ChooseMapResponse();
    }

    @Override
    public synchronized Response drawPowerUp(String token) throws RemoteException {
       Match match = Server.getInstance().getLobby().getMatch(token);
       PowerUpCard card = (PowerUpCard) match.drawPowerUp();
       match.turnEvent(TurnEvent.DRAW);
       return new DrawPowerUpResponse(card);
    }

    @Override
    public synchronized Response spawnPlayer(String token, RoomColor color) throws RemoteException {
        Player player = Server.getInstance().getLobby().getPlayer(token);
        Match match = Server.getInstance().getLobby().getMatch(player);
        GameMap map = match.getMap();
        List<PowerUpCard> powerUpCards = player.getPowerUpList();
        if(!powerUpCards.stream().map(c -> c.color).collect(Collectors.toList()).contains(color)){
            return new ErrorResponse(new CheatException());
        }
        SpawnPoint spawnPoint = map.getSpawnPoints().stream().filter(p -> p.getColor() == color).findFirst().orElse(null);
        map.spawnPlayer(player,spawnPoint);
        match.addUpdate(new MapUpdate(match.getMap()));
        match.turnEvent(TurnEvent.SPAWN);
        return new SpawnPlayerResponse();
    }

    @Override
    public Response playEffect(String token, Effect effect, Ammo ammo, PowerUpCard powerUpCard) throws RemoteException {
        Player player = Server.getInstance().getLobby().getPlayer(token);
        Match match = Server.getInstance().getLobby().getMatch(player);
        player.playWeaponEffect(effect);
        Action action;
        while ((action = player.playNextWeaponAction()) != null){
            if(action.type == ActionType.SELECT && !action.select.auto){
                Selectable selectable = player.getActiveWeapon().getSelectable();
                return new SelectResponse(selectable);
            }
        }
        LegitEffects legitEffects = player.getWeaponEffects();
        match.addUpdate(new MapUpdate(match.getMap()));
        return legitEffects.getLegitEffects().isEmpty() ? new FinishCardResponse() : new FinishEffectResponse();
    }

    @Override
    public synchronized Response cardEffects(String token, String cardName) throws RemoteException {
        Player player = Server.getInstance().getLobby().getPlayer(token);
        WeaponCard card = player.getWeaponList().stream().filter(w -> w.name.equals(cardName)).findFirst().orElse(null);
        player.playWeapon(card);
        LegitEffects legitEffects = player.getWeaponEffects();
        return new CardEffectsResponse(legitEffects);
    }

    @Override
    public synchronized Response finishCard(String token) throws RemoteException {
        Player player = Server.getInstance().getLobby().getPlayer(token);
        player.resetWeapon();
        return new FinishCardResponse();
    }

    @Override
    public synchronized Response tagElement(String token, Taggable taggable) throws RemoteException {
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

    @Override
    public synchronized Response endTurn(String token) throws RemoteException {
        Server.getInstance().getLobby().getMatch(token).nextTurn();
        return new EndTurnResponse();
    }

    @Override
    public Response action(String token, models.turn.ActionType actionType) throws RemoteException {
        Player player = Server.getInstance().getLobby().getPlayer(token);
        Match match = Server.getInstance().getLobby().getMatch(player);
        try{
            match.action(actionType);
        }
        catch (Exception ex){
            return new ErrorResponse(ex);
        }
        return new TurnActionResponse();
    }

    @Override
    public Response grab(String token) throws RemoteException {
        Player player = Server.getInstance().getLobby().getPlayer(token);
        Match match = Server.getInstance().getLobby().getMatch(player);
        match.addUpdate(new MapUpdate(match.getMap()));
        return new GrabResponse(); //TODO grabbare
    }

    @Override
    public Response run(String token, TurnEvent turnEvent, Square targetSquare) throws RemoteException {
        Player player = Server.getInstance().getLobby().getPlayer(token);
        Match match = Server.getInstance().getLobby().getMatch(player);
        GameMap map = match.getMap();
        int max = 0;
        if(turnEvent == TurnEvent.RUN_1)
            max = 1;
        else if(turnEvent == TurnEvent.RUN_2)
            max = 2;
        else if(turnEvent == TurnEvent.RUN_3)
            max = 3;
        else if(turnEvent == TurnEvent.RUN_4)
            max = 4;
        Square playerSquare = map.getPlayerPosition(player);
        if(!map.getAllSquaresAtDistanceLessThanOrEquals(playerSquare, max).contains(targetSquare))
            throw new CheatException();
        map.movePlayer(player, targetSquare);
        match.turnEvent(turnEvent);
        match.addUpdate(new MapUpdate(match.getMap()));
        return new RunResponse();
    }

    @Override
    public Response reload(String token, List<WeaponCard> weapons) throws RemoteException {
        return new ReloadResponse();
    }

    @Override
    public Response playPowerUp(String token, String powerUpName, Ammo ammo, PowerUpCard powerUpAmmo) throws RemoteException {
        return null;
    }
}