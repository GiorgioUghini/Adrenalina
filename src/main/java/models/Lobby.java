package models;

import utils.Constants;
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

    private Match waitingMatch = new Match();

    public Match getWaitingMatch() {
        return waitingMatch;
    }

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
        if (waitingMatch.getPlayersNumber() == 0) {
            p = new Player(true, username);
        }
        else {
            p = new Player(false, username);
        }
        waitingMatch.addPlayer(p);

        if (waitingMatch.getPlayersNumber() == 5) {
            activeCountdown.cancel(true);
            activeCountdown = null;
            startMatch();
        }
        if (waitingMatch.getPlayersNumber() >= 3) {
            startCountdown();
        }
        return p.getToken();
    }

    public synchronized void disconnectPlayer(Player player) {
        waitingMatch.removePlayer(player);
        //TODO: was first player?
        if ((activeCountdown != null) && waitingMatch.getPlayersNumber() < 3) {
            activeCountdown.cancel(true);
            activeCountdown = null;
        }
    }

    public void startMatch() {
        waitingMatch.startMatch();  //Call this method when you want to start the match
        activeMatches.add(waitingMatch);

        waitingMatch = new Match();
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

    public Match getMatchByToken(String token) {
        for(Match match : getActiveMatches()){
            Player player = match.getPlayerByToken(token);
            if(player != null)
                return match;
        }
        return null;
    }

}