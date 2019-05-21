package models;

import org.awaitility.core.ConditionTimeoutException;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.Callable;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.*;

public class LobbyTimerTest {

    @Test
    public void lobbyTimerAbortTest() {
        Lobby lobby = Lobby.getInstance();
        lobby.registerPlayer("Cosimo");
        String token1 = lobby.registerPlayer("Giorgio");
        lobby.registerPlayer("Vila");
        List activeMatches = lobby.getActiveMatches();
        assertEquals(0, activeMatches.size());

        lobby.disconnectPlayer(lobby.getWaitingMatch().getPlayerByToken(token1));

        activeMatches = lobby.getActiveMatches();
        assertEquals(0, activeMatches.size());

        try {
            await().atMost(5, SECONDS).until(retFalse());
        } catch (ConditionTimeoutException e) {
            assert true;
        }

        activeMatches = lobby.getActiveMatches();
        assertEquals(0, activeMatches.size());
        lobby.resetInstance();
    }

    private Callable<Boolean> retFalse() {
        return new Callable<Boolean>() {
            public Boolean call() throws Exception {
                return false;
            }
        };
    }

}
