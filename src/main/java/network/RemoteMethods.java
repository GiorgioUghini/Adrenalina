package network;

import errors.*;
import models.Lobby;
import models.Match;
import models.card.*;
import models.map.*;
import models.player.Ammo;
import models.player.Player;
import models.turn.TurnEvent;
import network.responses.*;
import network.updates.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RemoteMethods extends UnicastRemoteObject implements RemoteMethodsInterface {
    private transient Logger logger;

    RemoteMethods() throws RemoteException {
        logger = Logger.getAnonymousLogger();
        if(Server.getInstance().isDebug()){
            logger.setLevel(Level.ALL);
            ConsoleHandler handler = new ConsoleHandler();
            handler.setLevel(Level.ALL);
            logger.addHandler(handler);
        }
    }

    @Override
    public synchronized List<Response> longPolling(String token) {
        try{
            RMIWrapper rmiWrapper = Server.getInstance().getConnection().getRMIWrapper(token);
            rmiWrapper.ping();
            List<Response> updates = new LinkedList<>(rmiWrapper.getUpdates());
            rmiWrapper.clearUpdates();
            return updates;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public synchronized String handshake() {
        try{
            RMIWrapper rmiWrapper = new RMIWrapper();
            String token = Server.getInstance().getConnection().getToken(rmiWrapper);
            RMIStatusTask rmiStatusTask = new RMIStatusTask(rmiWrapper);
            rmiWrapper.setRMIStatusTask(rmiStatusTask);
            Timer timer = new Timer();
            timer.schedule(rmiStatusTask, 0, 1000);
            return token;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return "";
        }

    }

    @Override
    public synchronized Response registerPlayer(String username, String password, String token) {
        try{
            Lobby lobby = Server.getInstance().getLobby();
            Player player = lobby.getPlayerByUsername(username);
            if (player != null) {
                if (player.isOnline()) {
                    return new ErrorResponse(new InvalidInputException());
                }
                if (!player.getPassword().equals(password))
                    return new ErrorResponse(new WrongPasswordException());

                player.reconnect();
                Match match = player.getMatch();
                GameMap map = match.getMap();
                Server.getInstance().getLobby().reconnectPlayer(token, player);
                return new ReconnectPlayerResponse(player, map, match.getPlayers());
            } else {
                player = lobby.registerPlayer(username, password, token);
                Server.getInstance().getConnection().addUpdateUnregisteredPlayers(new NewPlayerUpdate(player.getName()));
                lobby.addUpdateWaitingPlayers(new NewPlayerUpdate(player.getName()));
                return new RegisterPlayerResponse(player.getName());
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            return new ErrorResponse(ex);
        }
    }

    @Override
    public synchronized Response reconnect(String token) {
        try{
            Lobby lobby = Server.getInstance().getLobby();
            Player currentPlayer = lobby.getPlayer(token);
            Match currentMatch = lobby.getMatch(currentPlayer);
            ConnectionWrapper connectionWrapper = Server.getInstance().getConnection().getConnectionWrapper(token);
            connectionWrapper.addUpdate(new PlayersUpdate(currentMatch.getPlayers()));
            connectionWrapper.addUpdate(new PlayerUpdate(currentPlayer));
            connectionWrapper.addUpdate(new MapUpdate(currentMatch.getMap()));
            for(Player p : currentMatch.getPlayers()){
                connectionWrapper.addUpdate(new DamageUpdate(p));
                connectionWrapper.addUpdate(new MarkUpdate(p));
            }
            return new ReconnectResponse();
        }
        catch (Exception ex){
            ex.printStackTrace();
            return new ErrorResponse(ex);
        }

    }

    @Override
    public synchronized Response validActions(String token) {
        try{
            Lobby lobby = Server.getInstance().getLobby();
            Player currentPlayer = lobby.getPlayer(token);
            Match currentMatch = lobby.getMatch(currentPlayer);
            boolean isNewActions = currentMatch.getCurrentActionType() == null;
            Map<models.turn.ActionType,  List<TurnEvent>>  actions = currentMatch.getPossibleAction(currentPlayer);
            return new ValidActionsResponse(actions, isNewActions);
        }
        catch (Exception ex){
            ex.printStackTrace();
            return new ErrorResponse(ex);
        }
    }

    @Override
    public synchronized Response waitingPlayer(){
        try{
            List<String> waitingPlayers = Server.getInstance().getLobby().getWaitingPlayersUsername();
            return new WaitingPlayerResponse(waitingPlayers);
        }
        catch (Exception ex){
            ex.printStackTrace();
            return new ErrorResponse(ex);
        }
    }

    @Override
    public synchronized Response chooseMap(String token, int map) {
        try{
            Player player = Server.getInstance().getLobby().getPlayer(token);
            Match match = Server.getInstance().getLobby().getMatch(player);
            match.createMap(map);
            match.nextTurn();
            match.addUpdate(new MapChosenUpdate(match.getMap(), map));
            match.addUpdate(new StartGameUpdate(match.getPlayers()));
            return new ChooseMapResponse();
        }
        catch (Exception ex){
            ex.printStackTrace();
            return new ErrorResponse(ex);
        }
    }

    @Override
    public synchronized Response drawPowerUp(String token) {
        try{
            Player player = Server.getInstance().getLobby().getPlayer(token);
            Match match = Server.getInstance().getLobby().getMatch(player);
            PowerUpCard card = player.drawPowerUp();
            Server.getInstance().getConnection().getConnectionWrapper(token).addUpdate(new PlayerUpdate(player));
            match.turnEvent(TurnEvent.DRAW);
            return new DrawPowerUpResponse(card);
        }
        catch (Exception ex){
            ex.printStackTrace();
            return new ErrorResponse(ex);
        }
    }

    @Override
    public synchronized Response spawnPlayer(String token, PowerUpCard powerUpCard) {
        try{
            RoomColor color = powerUpCard.color;
            Player player = Server.getInstance().getLobby().getPlayer(token);
            Match match = Server.getInstance().getLobby().getMatch(player);
            GameMap map = match.getMap();
            player.clearDamages();
            List<PowerUpCard> powerUpCards = player.getPowerUpList();
            if(!powerUpCards.stream().map(c -> c.color).collect(Collectors.toList()).contains(color)){
                return new ErrorResponse(new CheatException());
            }
            powerUpCard = player.getPowerUpByName(powerUpCard.name, powerUpCard.color);
            player.throwPowerUp(powerUpCard);
            SpawnPoint spawnPoint = map.getSpawnPoints().stream().filter(p -> p.getColor() == color).findFirst().orElse(null);
            map.spawnPlayer(player,spawnPoint);
            match.addUpdate(new MapUpdate(match.getMap()));
            match.addUpdate(new DamageUpdate(player));
            match.addUpdate(new MarkUpdate(player));
            Server.getInstance().getConnection().getConnectionWrapper(token).addUpdate(new PlayerUpdate(player));
            match.turnEvent(TurnEvent.SPAWN);
            return new SpawnPlayerResponse();
        }
        catch (Exception ex){
            ex.printStackTrace();
            return new ErrorResponse(ex);
        }
    }

    @Override
    public synchronized Response playEffect(String token, Effect effect, Ammo ammo, PowerUpCard powerUpCard) {
        try{
            Player player = Server.getInstance().getLobby().getPlayer(token);
            Match match = Server.getInstance().getLobby().getMatch(player);
            logger.fine("Active weapon: " + player.getActiveWeapon().getName());
            effect = player.getActiveWeapon().getEffectByName(effect.name);
            logger.fine("Chosen effect: " + effect.name);
            if(powerUpCard!=null){
                powerUpCard = player.getPowerUpByName(powerUpCard.name, powerUpCard.color);
            }
            player.playWeaponEffect(effect, powerUpCard);
            Action action;
            while ((action = player.playNextWeaponAction()) != null){
                if(action.type == ActionType.SELECT && !action.select.auto){
                    Selectable selectable = player.getActiveWeapon().getSelectable();
                    if(selectable.get().isEmpty()) continue;
                    return new SelectResponse(selectable);
                }
                else if(action.type == ActionType.DAMAGE){
                    for(Player damagedPlayer : player.getActiveWeapon().getPlayersToDamage().keySet()){
                        match.addUpdate(new DamageUpdate(damagedPlayer));
                    }
                    long deadCount = player.getActiveWeapon().getPlayersToDamage().keySet().stream().filter(Player::isDead).count();
                    if(match.getSkullCount() + deadCount >= 8){
                        player.resetWeapon();
                        player.resetPowerUp();
                        match.activateFrenzy();
                        match.nextTurn();
                        logger.fine("Returning a finish card response");
                        return new FinishCardResponse();
                    }
                }
                else if(action.type == ActionType.MARK){
                    for(Player markedPlayer : player.getActiveWeapon().getPlayersToMark().keySet()){
                        match.addUpdate(new MarkUpdate(markedPlayer));
                    }
                }
            }
            LegitEffects legitEffects = player.getWeaponEffects();
            match.addUpdate(new MapUpdate(match.getMap()));
            Server.getInstance().getConnection().getConnectionWrapper(token).addUpdate(new PlayerUpdate(player));
            if(legitEffects.getLegitEffects().isEmpty()){
                match.turnEvent(TurnEvent.SHOOT);
                logger.fine("Returning a finish card response");
                return new FinishCardResponse();
            }
            else{
                logger.fine("Returning a finish effect response");
                return new FinishEffectResponse();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            logger.fine("Returning an error response");
            return new ErrorResponse(ex);
        }
    }

    @Override
    public synchronized Response cardEffects(String token, String cardName) {
        try{
            Player player = Server.getInstance().getLobby().getPlayer(token);
            WeaponCard card = player.getWeaponList().stream().filter(w -> w.name.equals(cardName)).findFirst().orElse(null);
            if(player.getActiveWeapon()==null){
                player.playWeapon(card);
                Server server = Server.getInstance();
                if(server.isDebug() && server.isAutoReload()){
                    card.load(new Ammo(3,3,3), null);
                }
            }
            LegitEffects legitEffects = player.getWeaponEffects();
            Server.getInstance().getConnection().getConnectionWrapper(token).addUpdate(new PlayerUpdate(player));
            return new CardEffectsResponse(legitEffects);
        }
        catch (Exception ex){
            ex.printStackTrace();
            return new ErrorResponse(ex);
        }
    }

    @Override
    public synchronized Response finishCard(String token) {
        logger.fine("Received finishCard request");
        try{
            Player player = Server.getInstance().getLobby().getPlayer(token);
            player.resetWeapon();
            logger.fine("Weapon succesfully reset");
            Server.getInstance().getConnection().getConnectionWrapper(token).addUpdate(new PlayerUpdate(player));
            Match match = player.getMatch();
            match.turnEvent(TurnEvent.SHOOT);
            return new FinishCardResponse();
        }
        catch (Exception ex){
            ex.printStackTrace();
            return new ErrorResponse(ex);
        }
    }

    @Override
    public synchronized Response tagElement(String token, Taggable taggable) {
        try{
            Player player = Server.getInstance().getLobby().getPlayer(token);
            Match match = Server.getInstance().getLobby().getMatch(player);
            WeaponCard card = player.getActiveWeapon();
            logger.fine("Tagging: " + taggable);
            card.select(taggable);
            logger.fine("Tag successful");
            Action action;
            while ((action = player.playNextWeaponAction()) != null){
                logger.fine("Playing action " + action.type);
                if(action.type == ActionType.SELECT && !action.select.auto){
                    Selectable selectable = card.getSelectable();
                    logger.fine("Selectable size: " + selectable.get().size());
                    if(selectable.get().isEmpty()) continue;
                    return new SelectResponse(selectable);
                }
                else if(action.type == ActionType.DAMAGE){
                    for(Player damagedPlayer : player.getActiveWeapon().getPlayersToDamage().keySet()){
                        match.addUpdate(new DamageUpdate(damagedPlayer));
                    }
                    long deadCount = player.getActiveWeapon().getPlayersToDamage().keySet().stream().filter(Player::isDead).count();
                    if(match.getSkullCount() + deadCount >= 8){
                        player.resetWeapon();
                        player.resetPowerUp();
                        match.activateFrenzy();
                        match.nextTurn();
                        logger.fine("Returning a finish card response");
                        return new FinishCardResponse();
                    }
                }
                else if(action.type == ActionType.MARK){
                    for(Player markedPlayer : player.getActiveWeapon().getPlayersToMark().keySet()){
                        match.addUpdate(new MarkUpdate(markedPlayer));
                    }
                }
            }
            logger.fine("Actions ended");
            LegitEffects legitEffects = player.getWeaponEffects();
            logger.fine("legitEffects size: " + legitEffects.getLegitEffects().size());
            match.addUpdate(new MapUpdate(match.getMap()));
            Server.getInstance().getConnection().getConnectionWrapper(token).addUpdate(new PlayerUpdate(player));
            if(legitEffects.getLegitEffects().isEmpty()){
                match.turnEvent(TurnEvent.SHOOT);
                return new FinishCardResponse();
            }
            else{
                return new FinishEffectResponse();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            return new ErrorResponse(ex);
        }
    }

    @Override
    public synchronized Response powerUpTagElement(String token, Taggable taggable) {
        try{
            Player player = Server.getInstance().getLobby().getPlayer(token);
            Match match = player.getMatch();
            PowerUpCard card = player.getActivePowerUp();
            card.select(taggable);
            Action action;
            while ((action = player.playNextPowerUpAction()) != null){
                if(action.type == ActionType.SELECT && !action.select.auto){
                    Selectable selectable = player.getActivePowerUp().getSelectable();
                    if(selectable.get().isEmpty()) continue;
                    return new SelectResponse(selectable);
                }
                else if(action.type == ActionType.DAMAGE){
                    for(Player damagedPlayer : player.getActiveWeapon().getPlayersToDamage().keySet()){
                        match.addUpdate(new DamageUpdate(damagedPlayer));
                    }
                    long deadCount = player.getActiveWeapon().getPlayersToDamage().keySet().stream().filter(Player::isDead).count();
                    if(match.getSkullCount() + deadCount >= 8){
                        player.resetWeapon();
                        player.resetPowerUp();
                        match.activateFrenzy();
                        match.nextTurn();
                        logger.fine("Returning a finish card response");
                        return new FinishPowerUpResponse();
                    }
                }
                else if(action.type == ActionType.MARK){
                    for(Player markedPlayer : player.getActiveWeapon().getPlayersToMark().keySet()){
                        match.addUpdate(new MarkUpdate(markedPlayer));
                    }
                }
            }
            match.addUpdate(new MapUpdate(match.getMap()));
            Server.getInstance().getConnection().getConnectionWrapper(token).addUpdate(new PlayerUpdate(player));
            return new FinishPowerUpResponse();
        }
        catch (Exception ex){
            ex.printStackTrace();
            return new ErrorResponse(ex);
        }
    }

    @Override
    public synchronized Response endTurn(String token) {
        try{
            Player player = Server.getInstance().getLobby().getPlayer(token);
            Match match = player.getMatch();
            for(Player p : match.getPlayers()){
                if(p.isDead()){
                    p.addPartialPointsCount();
                    match.getMap().removePlayer(p);
                }
            }
            match.addUpdate(new PointsUpdate(match.getTotalPoints()));
            match.nextTurn();
            match.addUpdate(new MapUpdate(match.getMap()));
            Server.getInstance().getConnection().getConnectionWrapper(token).addUpdate(new PlayerUpdate(player));
            return new EndTurnResponse();
        }
        catch (Exception ex){
            ex.printStackTrace();
            return new ErrorResponse(ex);
        }
    }

    @Override
    public synchronized Response action(String token, models.turn.ActionType actionType) {
        try{
            Player player = Server.getInstance().getLobby().getPlayer(token);
            Match match = Server.getInstance().getLobby().getMatch(player);
            try{
                match.action(actionType);
            }
            catch (Exception ex){
                return new ErrorResponse(ex);
            }
            Server.getInstance().getConnection().getConnectionWrapper(token).addUpdate(new PlayerUpdate(player));
            return new TurnActionResponse();
        }
        catch (Exception ex){
            ex.printStackTrace();
            return new ErrorResponse(ex);
        }
    }

    @Override
    public synchronized Response grab(String token, WeaponCard drawn, WeaponCard toRelease,PowerUpCard powerUpCard) {
        Match match = null;
        try{
            Player player = Server.getInstance().getLobby().getPlayer(token);
            match = Server.getInstance().getLobby().getMatch(player);
            GameMap map = match.getMap();
            Square playerSquare = map.getPlayerPosition(player);
            if(playerSquare.isSpawnPoint()){
                SpawnPoint spawnPoint = (SpawnPoint) playerSquare;
                drawn = (WeaponCard) spawnPoint.getByName(drawn.name);
                if(powerUpCard!=null){
                    powerUpCard = player.getPowerUpByName(powerUpCard.name, powerUpCard.color);
                }
                if(toRelease != null){
                    toRelease = player.getWeaponByName(toRelease.name);
                }
                player.drawWeaponCard(drawn, powerUpCard, toRelease);
            }else{
                player.drawAmmoCard();
            }
            match.turnEvent(TurnEvent.GRAB);
            match.addUpdate(new MapUpdate(match.getMap()));
            Server.getInstance().getConnection().getConnectionWrapper(token).addUpdate(new PlayerUpdate(player));
            return new GrabResponse();
        }
        catch (NothingToGrabException e){
            match.turnEvent(TurnEvent.GRAB);
            e.printStackTrace();
            return new ErrorResponse(e);
        }
        catch(NotEnoughAmmoException e){
            match.turnEvent(TurnEvent.GRAB);
            e.printStackTrace();
            return new ErrorResponse(e);
        }
        catch (Exception ex){
            ex.printStackTrace();
            return new ErrorResponse(ex);
        }
    }

    @Override
    public synchronized Response run(String token, TurnEvent turnEvent, Square targetSquare) {
        try{
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
            if(!map.getAllSquaresAtDistanceLessThanOrEquals(playerSquare, max).contains(targetSquare)){
                logger.severe(String.format("Player square id: %d\nTarget square id: %d\nMax: %d", playerSquare.getId(), targetSquare.getId(), max));
                throw new CheatException();
            }
            targetSquare = map.getSquareById(targetSquare.getId());
            map.movePlayer(player, targetSquare);
            match.turnEvent(turnEvent);
            match.addUpdate(new MapUpdate(match.getMap()));
            Server.getInstance().getConnection().getConnectionWrapper(token).addUpdate(new PlayerUpdate(player));
            return new RunResponse();
        }
        catch (Exception ex){
            ex.printStackTrace();
            return new ErrorResponse(ex);
        }
    }

    @Override
    public synchronized Response reload(String token, Map<WeaponCard, PowerUpCard> weaponsMap) {
        try{
            Player player = Server.getInstance().getLobby().getPlayer(token);
            for(WeaponCard weaponCard : player.getWeaponList()){
                if(weaponsMap.keySet().contains(weaponCard)){
                    PowerUpCard reloadPowerUpCard = weaponsMap.get(weaponCard);
                    if(player.canReloadWeapon(weaponCard, reloadPowerUpCard)){
                        player.reloadWeapon(weaponCard, reloadPowerUpCard);
                    }else{
                        //TODO: What if the weapon cannot be loaded?
                    }
                }
            }
            return new ReloadResponse();
        }
        catch (Exception ex){
            ex.printStackTrace();
            return new ErrorResponse(ex);
        }
    }

    @Override
    public synchronized Response playPowerUp(String token, String powerUpName, Ammo ammo, PowerUpCard powerUpAmmo) {
        try{
            Player player = Server.getInstance().getLobby().getPlayer(token);
            Match match = Server.getInstance().getLobby().getMatch(player);
            if(powerUpAmmo!=null) powerUpAmmo = player.getPowerUpByName(powerUpAmmo.name, powerUpAmmo.color);
            PowerUpCard powerUpCard = player.getPowerUpList().stream().filter(c -> c.name.equals(powerUpName)).findFirst().orElse(null);
            player.playPowerUp(powerUpCard, ammo, powerUpAmmo);
            Action action;
            while ((action = player.playNextPowerUpAction()) != null){
                if(action.type == ActionType.SELECT && !action.select.auto){
                    Selectable selectable = powerUpCard.getSelectable();
                    if(selectable.get().isEmpty()) continue;
                    return new SelectResponse(selectable);
                }
                else if(action.type == ActionType.DAMAGE){
                    for(Player damagedPlayer : player.getActiveWeapon().getPlayersToDamage().keySet()){
                        match.addUpdate(new DamageUpdate(damagedPlayer));
                    }
                    long deadCount = player.getActiveWeapon().getPlayersToDamage().keySet().stream().filter(Player::isDead).count();
                    if(match.getSkullCount() + deadCount >= 8){
                        player.resetWeapon();
                        player.resetPowerUp();
                        match.activateFrenzy();
                        match.nextTurn();
                        logger.fine("Returning a finish card response");
                        return new FinishPowerUpResponse();
                    }
                }
                else if(action.type == ActionType.MARK){
                    for(Player markedPlayer : player.getActiveWeapon().getPlayersToMark().keySet()){
                        match.addUpdate(new MarkUpdate(markedPlayer));
                    }
                }
            }
            match.addUpdate(new MapUpdate(match.getMap()));
            Server.getInstance().getConnection().getConnectionWrapper(token).addUpdate(new PlayerUpdate(player));
            return new FinishPowerUpResponse();
        }
        catch (Exception ex){
            ex.printStackTrace();
            return new ErrorResponse(ex);
        }
    }
}