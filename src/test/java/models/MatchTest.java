package models;

import models.player.Player;
import models.turn.ActionElement;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class MatchTest {
    @Test
    public void startTurnNextTurnEndTurnPossibleActions() {
        Player pl1 = new Player("Cosimo", "");
        Player pl2 = new Player( "Giorgio", "");
        Player pl3 = new Player( "Vila", "");
        List<Player> players = new LinkedList<>();
        players.add(pl2);
        players.add(pl3);
        players.add(pl1);
        Match m = new Match(players);
        assertEquals(pl2, m.getFirstPlayer());

        Map possibleActions = m.getPossibleAction(pl2);
        assertTrue(possibleActions == null || possibleActions.isEmpty());

        m.chooseMapAndStartMatch();
        m.nextTurn();
        possibleActions = m.getPossibleAction(pl2);
        assertFalse(possibleActions.isEmpty());

        m.nextTurn();
        possibleActions = m.getPossibleAction(pl2);
        assertTrue(possibleActions.isEmpty());

        possibleActions = m.getPossibleAction(pl3);
        assertFalse(possibleActions.isEmpty());
    }

}