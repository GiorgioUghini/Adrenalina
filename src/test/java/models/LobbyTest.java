package models;

import utils.Constants;
import org.awaitility.core.ConditionTimeoutException;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.Callable;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
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

    private Callable<Boolean> matchStarted(Lobby lobby) {
        return new Callable<Boolean>() {
            public Boolean call() throws Exception {
                List activeMatches = lobby.getActiveMatches();
                return (activeMatches.size() == 1);
            }
        };
    }

}
