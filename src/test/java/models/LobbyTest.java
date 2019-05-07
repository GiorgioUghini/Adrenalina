package models;

import models.player.Player;
import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class LobbyTest {

    @Test
    public void lobbyTimerTest() throws InterruptedException {
        Lobby.getInstance().registerPlayer("Cosimo");
        Lobby.getInstance().registerPlayer("Giorgio");
        Lobby.getInstance().registerPlayer("Vila");
        Lobby lobby = Lobby.getInstance();
        List activeMatches = lobby.getActiveMatches();
        assertEquals(activeMatches.size(), 0);
        Thread.sleep(5100);
        activeMatches = lobby.getActiveMatches();
        assertEquals(activeMatches.size(), 1);
    }

    @Test
    public void lobbyTimerAbortTest() throws InterruptedException {
        Lobby.getInstance().registerPlayer("Cosimo");
        Lobby.getInstance().registerPlayer("Giorgio");
        Lobby.getInstance().registerPlayer("Vila");
        Lobby lobby = Lobby.getInstance();
        List activeMatches = lobby.getActiveMatches();
        assertEquals(0, activeMatches.size());

        Thread.sleep(2550);
        lobby.disconnectPlayer(lobby.getPlayerWaiting().get(1));

        Thread.sleep(2550);
        activeMatches = lobby.getActiveMatches();
        assertEquals(0, activeMatches.size());

        Thread.sleep(2550);
        activeMatches = lobby.getActiveMatches();
        assertEquals(0, activeMatches.size());
    }

    @Test
    public void lobbyStartMatchTest() throws InterruptedException {
        Lobby lobby = Lobby.getInstance();

        lobby.registerPlayer("Cosimo");
        lobby.registerPlayer("Giorgio");
        lobby.registerPlayer("Vila");   //Three player, timer starts

        Thread.sleep(1000);

        lobby.disconnectPlayer(lobby.getPlayerWaiting().get(1));    //Two player remaining, timer aborts

        Thread.sleep(1000);
        lobby.registerPlayer("Cosimo");
        lobby.registerPlayer("Giorgio");
        lobby.registerPlayer("Vila");   //Five player, match starts ASAP

        List activeMatches = lobby.getActiveMatches();
        assertEquals(1, activeMatches.size());
    }
}
