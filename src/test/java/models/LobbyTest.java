package models;

import config.Constants;
import org.awaitility.core.ConditionTimeoutException;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.Callable;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.*;

public class LobbyTest {

    @Test
    public void lobbyTimerTest() {
        Lobby lobby = Lobby.getInstance();
        lobby.registerPlayer("Cosimo");
        lobby.registerPlayer("Giorgio");
        lobby.registerPlayer("Vila");
        List activeMatches = lobby.getActiveMatches();
        assertEquals(0, activeMatches.size());

        try {
            await().atMost(Constants.DELAY_SECONDS + 1, SECONDS).until(matchStarted());
        } catch (ConditionTimeoutException e) {
            assert false;
        }

        assert true;
    }

    private Callable<Boolean> matchStarted() {
        return new Callable<Boolean>() {
            public Boolean call() throws Exception {
                List activeMatches = Lobby.getInstance().getActiveMatches();
                return (activeMatches.size() == 1);
            }
        };
    }

}
