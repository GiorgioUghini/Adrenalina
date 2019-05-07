package models;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class LobbyTest {

    @Test
    public void lobbyTimerTest() {
        Lobby lobby = Lobby.getInstance();
        lobby.registerPlayer("Cosimo");
        lobby.registerPlayer("Giorgio");
        lobby.registerPlayer("Vila");
        List activeMatches = lobby.getActiveMatches();
        assertEquals(activeMatches.size(), 0);
        long actualTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - actualTime < 5.1 * 1000); //It's like Thread.sleep(5100); but in this way I stop sonar here, without going to other tests.
        activeMatches = lobby.getActiveMatches();
        assertEquals(activeMatches.size(), 1);
    }

}
