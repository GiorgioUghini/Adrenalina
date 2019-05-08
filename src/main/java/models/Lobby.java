package models;

import config.Constants;
import models.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Lobby {
    private static Lobby instance = null;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> activeCountdown;
    private List<Match> activeMatches = new ArrayList<>();

    private List<Player> waitingPlayerList = new ArrayList<>();

    private Lobby(){
    }

    /**
     * Method that creates or directly return the singleton instance
     * @return the instance of the singleton class "Lobby"
     */
    public static Lobby getInstance()
    {
        if (instance == null) {
            instance = new Lobby();
        }
        return instance;
    }

    public synchronized void resetInstance() {    //Only for testing purpose
        waitingPlayerList.clear();
        activeMatches.clear();
        if(activeCountdown!=null) {
            activeCountdown.cancel(true);
            activeCountdown = null;
        }
        scheduler.shutdownNow();
        scheduler = Executors.newScheduledThreadPool(1);
    }

    /**
     * Method that register a player into the lobby of the server, waiting to start his match
     * @param username the username of the player to register
     * @return a String that contains the player token
     */
    public synchronized String registerPlayer(String username) {
        Player p;
        if (waitingPlayerList.isEmpty()) {
            p = new Player(true, username);
        }
        else {
            p = new Player(false, username);
        }
        waitingPlayerList.add(p);
        if (waitingPlayerList.size() == 5) {
            activeCountdown.cancel(true);
            activeCountdown = null;
            startMatch();
        }
        if (waitingPlayerList.size() >= 3) {
            startCountdown();
        }
        return p.getToken();
    }

    public synchronized void disconnectPlayer(Player player) {
        waitingPlayerList.remove(player);
        if ((activeCountdown != null) && waitingPlayerList.size() < 3) {
            activeCountdown.cancel(true);
            activeCountdown = null;
        }
    }

    public synchronized List<Player> getPlayerWaiting() {
        return new ArrayList<>(waitingPlayerList);
    }

    public Match startMatch() {
        Match newMatch = new Match();
        synchronized (waitingPlayerList) {
            for (Player p : waitingPlayerList) {
                newMatch.addPlayer(p);
                if (p.isFirstPlayer()) {
                    newMatch.setFirstPlayer(p);
                }
            }
        }
        newMatch.startMatch();  //Call this method when you want to start the match
        waitingPlayerList.clear();
        activeMatches.add(newMatch);
        return newMatch;
    }

    public synchronized void startCountdown() {
        //Starting countdown now...
        activeCountdown = scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                Lobby.getInstance().activeCountdown = null;
                Lobby.getInstance().startMatch();
            }}, Constants.DELAY_SECONDS, TimeUnit.SECONDS);
    }

    public synchronized List<Match> getActiveMatches() {
        return new ArrayList<>(activeMatches);
    }

}