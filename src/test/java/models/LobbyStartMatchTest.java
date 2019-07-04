package models;

import models.player.Player;
import org.awaitility.core.ConditionTimeoutException;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.Callable;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;

public class LobbyStartMatchTest {

    @Test
    public void lobbyStartMatchTest() throws InterruptedException {
        Config config = new Config(7000, 60000);
        Lobby lobby = new Lobby(config);

        lobby.registerPlayer("Cosimo", "");
        Player player1 = lobby.registerPlayer("Giorgio", "");
        lobby.registerPlayer("Vila", "");   //Three player, timer starts

        lobby.disconnectPlayer(player1);    //Two player remaining, timer aborts

        try {
            await().atMost(5, SECONDS).until(retFalse());
        } catch (ConditionTimeoutException e) {
            assert true;
        }

        lobby.registerPlayer("Cosimo", "");
        lobby.registerPlayer("Giorgio", "");
        lobby.registerPlayer("Vila", "");   //Five player, match starts ASAP

        List activeMatches = lobby.getActiveMatches();
        assertEquals(1, activeMatches.size());
        //lobby.resetInstance();
    }

    private Callable<Boolean> retFalse() {
        return new Callable<Boolean>() {
            public Boolean call() throws Exception {
                return false;
            }
        };
    }

}
