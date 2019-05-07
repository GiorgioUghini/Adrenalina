package models;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class LobbyStartMatchTest {

    @Test
    public void lobbyStartMatchTest() {
        Lobby lobby = Lobby.getInstance();

        lobby.registerPlayer("Cosimo");
        lobby.registerPlayer("Giorgio");
        lobby.registerPlayer("Vila");   //Three player, timer starts

        long actualTime = System.currentTimeMillis();
        while(System.currentTimeMillis() - actualTime < 1000); //It's like Thread.sleep(5100); but in this way I stop sonar here, without going to other tests.


        lobby.disconnectPlayer(lobby.getPlayerWaiting().get(1));    //Two player remaining, timer aborts

        actualTime = System.currentTimeMillis();
        while(System.currentTimeMillis() - actualTime < 1000); //It's like Thread.sleep(5100); but in this way I stop sonar here, without going to other tests.
        lobby.registerPlayer("Cosimo");
        lobby.registerPlayer("Giorgio");
        lobby.registerPlayer("Vila");   //Five player, match starts ASAP

        List activeMatches = lobby.getActiveMatches();
        assertEquals(1, activeMatches.size());
    }
}
