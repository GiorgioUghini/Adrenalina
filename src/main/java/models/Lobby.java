package models;

import javafx.application.Platform;
import network.Response;
import utils.BiMap;
import utils.Constants;
import models.player.Player;

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
    public synchronized String registerPlayer(String username, String password, String token) {
        Player p;
        if (token == null || token.isEmpty()) {
            p = new Player(username, password);
        } else {
            p = new Player(username, password, token);
        }
        waitingPlayers.add(p);

        if (waitingPlayers.size() == 5) {
            activeCountdown.cancel(true);
            activeCountdown = null;
            startMatch();
        }
        if (waitingPlayers.size() >= 3) {
            startCountdown();
        }
        //tokenPlayerMap.add(p.getToken(), p);
        return p.getToken();
    }

    public synchronized String registerPlayer(String username, String password){
        return registerPlayer(username,password, null);
    }

    public synchronized void disconnectPlayer(Player player) {
        waitingPlayers.remove(player);
        //TODO: was first player?
        if ((activeCountdown != null) && waitingPlayers.size() < 3) {
            activeCountdown.cancel(true);
            activeCountdown = null;
        }
    }

    public void startMatch() {
        Match match = new Match(waitingPlayers);
        for(Player waitingPlayer : waitingPlayers){
            matchPlayerMap.add(match, waitingPlayer);
        }
        match.startMatch();  //Call this method when you want to start the match
        waitingPlayers.clear();
    }

    public synchronized void startCountdown() {
        //Starting countdown now...
        activeCountdown = scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                activeCountdown = null;
                startMatch();
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

    public void addPlayer(Player player){
        tokenPlayerMap.add(player.getToken(), player);
    }

    public Match getMatch(Player player) {
        Match match = matchPlayerMap.getSingleKey(player);
        return match;
    }

    public Player getPlayer(String token) {
      Player player = tokenPlayerMap.getSingleValue(token);
      return player;
    }

    public Player getPlayerByUsername(String username){
        return tokenPlayerMap.getValues().stream().filter(p -> p.getName().equals(username)).findFirst().orElse(null);
    }

    public void addUpdateWaitingPlayer(Response update){
        for(Player player : waitingPlayers){
            player.addUpdate(update);
        }
    }

    public Player getWaitingPlayer(String token) {
            for(Player player : waitingPlayers){
                if(player.getToken().equals(token)){
                    return player;
                }
            }
            return null;
    }
}