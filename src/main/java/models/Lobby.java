package models;

import models.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lobby {
    private static Lobby instance = null;

    private List<Player> waitingPlayerList;

    private Lobby(){
        waitingPlayerList = new ArrayList<>();
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

        return p.getToken();
    }

    public List<Player> getPlayerWaiting() {
        return new ArrayList<>(waitingPlayerList);
    }

}