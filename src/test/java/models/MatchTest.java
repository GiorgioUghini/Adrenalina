package models;

import models.player.Player;
import models.turn.ActionElement;
import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static org.junit.Assert.*;

public class MatchTest {
    @Test
    public void startTurnAndPossibleActions() {
        Match m = new Match();
        Player pl1 = new Player(false, "Cosimo");
        Player pl2 = new Player(false, "Giorgio");
        Player pl3 = new Player(false, "Vila");
        m.addPlayer(pl1);
        m.addPlayer(pl2);
        m.addPlayer(pl3);
        m.setFirstPlayer(pl2);
        assertEquals(pl2, m.getFirstPlayer());

        Set possibleActions = m.getPossibleAction(pl2);
        assertTrue(possibleActions.isEmpty());

        m.startMatch();
        possibleActions = m.getPossibleAction(pl2);
        assertFalse(possibleActions.isEmpty());

        m.nextTurn();
        possibleActions = m.getPossibleAction(pl2);
        assertFalse(possibleActions.isEmpty());

        m.endTurn();
        m.nextTurn();
        possibleActions = m.getPossibleAction(pl2);
        assertTrue(possibleActions.isEmpty());

        possibleActions = m.getPossibleAction(pl3);
        assertFalse(possibleActions.isEmpty());
    }
}
