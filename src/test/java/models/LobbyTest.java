package models;

import models.player.Player;
import org.awaitility.core.ConditionTimeoutException;
import org.junit.Test;
import utils.TokenGenerator;

import java.util.List;
import java.util.concurrent.Callable;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.*;

public class LobbyTest {

    @Test
    public void lobbyTimerTest() {
        Config config = new Config(7000, 60000);
        Lobby lobby = new Lobby(config);
        lobby.registerPlayer("Cosimo", "");
        lobby.registerPlayer("Giorgio", "");
        lobby.registerPlayer("Vila", "");
        List activeMatches = lobby.getActiveMatches();
        assertEquals(0, activeMatches.size());

        try {
            await().atMost(config.getMatchConnectionTimeout() + 1000, MILLISECONDS).until(matchStarted(lobby));
        } catch (ConditionTimeoutException e) {
            assert false;
        }

        assert true;
        //lobby.resetInstance();
    }

    @Test
    public void testGetters(){
        Lobby lobby = new Lobby(new Config(100,100));
        String token = TokenGenerator.nextToken();
        Player a = lobby.registerPlayer("a", "", token);
        lobby.registerPlayer("b", "");
        lobby.registerPlayer("c", "");
        lobby.registerPlayer("d", "");
        lobby.registerPlayer("e", "");

        Match match = lobby.getMatch(token);
        assertEquals(match, lobby.getMatch(a));
        assertEquals(a, lobby.getPlayer(token));
        assertEquals(token, lobby.getToken(a));
        assertEquals(a, match.getPlayerByUsername("a"));

        assertEquals(5, lobby.getRegisteredPlayers().size());
        assertEquals(0, lobby.getWaitingPlayers().size());
        lobby.registerPlayer("gigi", "");
        assertEquals(1, lobby.getWaitingPlayers().size());
    }

    @Test
    public void testReconnection(){
        Lobby lobby = new Lobby(new Config(100,100));
        String token = TokenGenerator.nextToken();
        Player a = lobby.registerPlayer("a", "", token);
        lobby.registerPlayer("b", "");
        lobby.registerPlayer("c", "");
        lobby.registerPlayer("d", "");
        lobby.registerPlayer("e", "");

        lobby.reconnectPlayer(token, a);
        assertEquals(5, lobby.getMatch(token).getPlayersNumber());
    }

    private Callable<Boolean> matchStarted(Lobby lobby) {
        return new Callable<Boolean>() {
            public Boolean call() throws Exception {
                List activeMatches = lobby.getActiveMatches();
                return (activeMatches.size() == 1);
            }
        };
    }

}
