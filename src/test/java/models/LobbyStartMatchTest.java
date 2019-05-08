package models;

import org.awaitility.core.ConditionTimeoutException;
import org.junit.Test;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.*;
import org.awaitility.Duration;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class LobbyStartMatchTest {

    @Test
    public void lobbyStartMatchTest() throws InterruptedException {
        Lobby lobby = Lobby.getInstance();

        lobby.registerPlayer("Cosimo");
        lobby.registerPlayer("Giorgio");
        lobby.registerPlayer("Vila");   //Three player, timer starts

        lobby.disconnectPlayer(lobby.getPlayerWaiting().get(1));    //Two player remaining, timer aborts

        try {
            await().atMost(5, SECONDS).until(retFalse());
        } catch (ConditionTimeoutException e) {
            assert true;
        }

        lobby.registerPlayer("Cosimo");
        lobby.registerPlayer("Giorgio");
        lobby.registerPlayer("Vila");   //Five player, match starts ASAP

        List activeMatches = lobby.getActiveMatches();
        assertEquals(1, activeMatches.size());
    }

    private Callable<Boolean> retFalse() {
        return new Callable<Boolean>() {
            public Boolean call() throws Exception {
                return false;
            }
        };
    }

}
