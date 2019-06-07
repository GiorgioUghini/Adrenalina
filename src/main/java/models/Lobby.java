package models;

import network.ConnectionWrapper;
import network.Response;
import network.Server;
import utils.BiMap;
import utils.Constants;
import models.player.Player;
import utils.TokenGenerator;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Lobby {
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> activeCountdown;
    private BiMap<Match, Player> matchPlayerMap = new BiMap<>();
    private BiMap<String, Player> tokenPlayerMap = new BiMap<>();
    private List<Player> waitingPlayers = new LinkedList<>();

    public List<Player> getWaitingPlayers() {
        return waitingPlayers;
    }

    public List<String> getWaitingPlayersUsername() {
        return waitingPlayers.stream().map(Player::getName).collect(Collectors.toList());
    }

    /**
     * Method that register a player into the lobby of the server, waiting to start his match
     *
     * @param username the username of the player to register
     * @return a String that contains the player token
     */
    public synchronized Player registerPlayer(String username, String password, String token) {
        Player p = new Player(username, password);
        waitingPlayers.add(p);
        new Thread(() -> {
            if (waitingPlayers.size() == 1) {
                if (activeCountdown != null) {
                    activeCountdown.cancel(true);
                    activeCountdown = null;
                }
                chooseMapAndStartMatch();
            }
            if (waitingPlayers.size() >= 3) {
                startCountdown();
            }
        }).start();
        tokenPlayerMap.add(token, p);
        return p;
    }

    //SOLO PER I TEST!!
    public synchronized Player registerPlayer(String username, String password) {
        return registerPlayer(username,password, TokenGenerator.nextToken());
    }
    //SOLO PER I TEST!!

    public synchronized void disconnectPlayer(Player player) {
        if(waitingPlayers.contains(player)){
            tokenPlayerMap.removeByValue(player);
            waitingPlayers.remove(player);
        }
        if ((activeCountdown != null) && waitingPlayers.size() < 3) {
            activeCountdown.cancel(true);
            activeCountdown = null;
        }
    }

    public synchronized void chooseMapAndStartMatch() {
        Match match = new Match(waitingPlayers);
        for(Player waitingPlayer : waitingPlayers){
            matchPlayerMap.add(match, waitingPlayer);
        }
        match.chooseMapAndStartMatch();  //Call this method when you want to start the match
        waitingPlayers.clear();
    }

    public synchronized void startCountdown() {
        //Starting countdown now...
        activeCountdown = scheduler.schedule(() -> {
            activeCountdown = null;
            if (!(waitingPlayers.size() < 3)) {
                chooseMapAndStartMatch();
            }
        }, Constants.DELAY_SECONDS, TimeUnit.SECONDS);
    }

    public synchronized List<Match> getActiveMatches() {
        return matchPlayerMap.getKeys();
    }

    public Match getMatch(String token) {
        Player player = getPlayer(token);
        Match match = getMatch(player);
        return match;
    }

    public Match getMatch(Player player) {
        Match match = matchPlayerMap.getSingleKey(player);
        return match;
    }

    public Player getPlayer(String token) {
      Player player = tokenPlayerMap.getSingleValue(token);
      return player;
    }

    public String getToken(Player player) {
        String token = tokenPlayerMap.getSingleKey(player);
        return token;
    }

    public Player getPlayerByUsername(String username){
        return tokenPlayerMap.getValues().stream().filter(p -> p.getName().equals(username)).findFirst().orElse(null);
    }

    public void addUpdateWaitingPlayers(Response update){
        for(Player player : waitingPlayers){
            Server.getInstance().getConnection().getConnectionWrapper(Server.getInstance().getLobby().getToken(player)).addUpdate(update);
        }
    }

    public List<Player> getRegisteredPlayers(){
        List<Player> players = new ArrayList<>(tokenPlayerMap.getValues());
        players.addAll(waitingPlayers);
        return players;
    }

    public List<ConnectionWrapper> getRegisteredConnectionWrappers(){
        List<Player> players = getRegisteredPlayers();
        List<ConnectionWrapper> wrappers = players.stream().map(p -> Server.getInstance().getConnection().getConnectionWrapper(getToken(p))).collect(Collectors.toList());
        return wrappers;
    }

    public Player getWaitingPlayer(String token) {
        return waitingPlayers.stream().filter(p -> Server.getInstance().getLobby().getToken(p).equals(token)).findFirst().orElse(null);
    }
}