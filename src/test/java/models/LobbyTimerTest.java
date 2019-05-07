package models;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class LobbyTimerTest {

    @Test
    public void lobbyTimerAbortTest() {
        Lobby lobby = Lobby.getInstance();
        lobby.registerPlayer("Cosimo");
        lobby.registerPlayer("Giorgio");
        lobby.registerPlayer("Vila");
        List activeMatches = lobby.getActiveMatches();
        assertEquals(0, activeMatches.size());

        long actualTime = System.currentTimeMillis();
        while(System.currentTimeMillis() - actualTime < 2.55 * 1000); //It's like Thread.sleep(5100); but in this way I stop sonar here, without going to other tests.
        lobby.disconnectPlayer(lobby.getPlayerWaiting().get(1));

        actualTime = System.currentTimeMillis();
        while(System.currentTimeMillis() - actualTime < 2.55 * 1000); //It's like Thread.sleep(5100); but in this way I stop sonar here, without going to other tests.
        activeMatches = lobby.getActiveMatches();
        assertEquals(0, activeMatches.size());

        actualTime = System.currentTimeMillis();
        while(System.currentTimeMillis() - actualTime < 2.55 * 1000); //It's like Thread.sleep(5100); but in this way I stop sonar here, without going to other tests.
        activeMatches = lobby.getActiveMatches();
        assertEquals(0, activeMatches.size());
    }

}
