package models;

import models.player.Player;
import models.turn.ActionElement;
import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class MatchTest {
    @Test
    public void startTurnNextTurnEndTurnPossibleActions() {
        Player pl1 = new Player("Cosimo", "");
        Player pl2 = new Player( "Giorgio", "");
        Player pl3 = new Player( "Vila", "");
        List<Player> players = new LinkedList<>();
        players.add(pl3);
        players.add(pl2);
        players.add(pl1);
        Match m = new Match(players);
        m.setFirstPlayer(pl2);
        assertEquals(pl2, m.getFirstPlayer());

        Set possibleActions = m.getPossibleAction(pl2);
        assertTrue(possibleActions.isEmpty());

        m.chooseMapAndStartMatch();
        possibleActions = m.getPossibleAction(pl2);
        assertFalse(possibleActions.isEmpty());

        m.nextTurn();
        possibleActions = m.getPossibleAction(pl2);
        assertFalse(possibleActions.isEmpty());

        m.nextTurn();
        possibleActions = m.getPossibleAction(pl2);
        assertTrue(possibleActions.isEmpty());

        possibleActions = m.getPossibleAction(pl3);
        assertFalse(possibleActions.isEmpty());
    }

    @Test
    public void confirmActions() {
        Player pl1 = new Player("Cosimo", "");
        Player pl2 = new Player( "Giorgio", "");
        Player pl3 = new Player( "Vila", "");
        List<Player> players = new LinkedList<>();
        players.add(pl2);
        players.add(pl3);
        players.add(pl1);
        Match m = new Match(players);
        assertEquals(pl2, m.getFirstPlayer());

        m.chooseMapAndStartMatch();

        LinkedList<ActionElement> wantsToDoList = new LinkedList<>();
        wantsToDoList.add(ActionElement.RUN);
        wantsToDoList.add(ActionElement.SHOOT);
        boolean confirmation = m.confirmActions(pl2, wantsToDoList);
        // [RUN, SHOOT] not permitted in this stage
        assertFalse(confirmation);

        wantsToDoList.clear();
        wantsToDoList.add(ActionElement.GRAB);
        confirmation = m.confirmActions(pl2, wantsToDoList);
        // [GRAB] permitted in this stage as subset of [RUN, GRAB]
        assertTrue(confirmation);

        wantsToDoList.add(ActionElement.RUN);
        confirmation = m.confirmActions(pl2, wantsToDoList);
        // [GRAB, RUN] not permitted in this stage (order not correct)
        assertFalse(confirmation);

        pl3.getDamage(3, pl2);

        wantsToDoList.clear();
        wantsToDoList.add(ActionElement.RUN);
        wantsToDoList.add(ActionElement.RUN);
        wantsToDoList.add(ActionElement.GRAB);
        confirmation = m.confirmActions(pl2, wantsToDoList);
        // [RUN, RUN, GRAB] not permitted in this stage (Giorgio has full life)
        assertFalse(confirmation);

        m.nextTurn(); //Start Vila
        wantsToDoList.clear();
        wantsToDoList.add(ActionElement.RUN);
        wantsToDoList.add(ActionElement.RUN);
        wantsToDoList.add(ActionElement.GRAB);
        confirmation = m.confirmActions(pl3, wantsToDoList);
        // [RUN, RUN, GRAB] permitted in this stage (Vila has three damages)
        assertTrue(confirmation);

        // Frenzy enabled!!!
        m.activateFrenzy(pl3);

        m.nextTurn(); //Start Cosimo
        wantsToDoList.clear();
        wantsToDoList.add(ActionElement.RUN);
        wantsToDoList.add(ActionElement.RUN);
        wantsToDoList.add(ActionElement.RELOAD);
        wantsToDoList.add(ActionElement.SHOOT);
        confirmation = m.confirmActions(pl1, wantsToDoList);
        // [RUN, RUN, RELOAD, SHOOT] not permitted in this stage (Not type 2)
        assertFalse(confirmation);
        wantsToDoList.remove(ActionElement.RUN);
        confirmation = m.confirmActions(pl1, wantsToDoList);
        // [RUN, RELOAD, SHOOT] permitted in this stage (Not type 2)
        assertTrue(confirmation);

        m.nextTurn(); //Start Giorgio
        confirmation = m.confirmActions(pl2, wantsToDoList);
        // [RUN, RELOAD, SHOOT] permitted in this stage as subset of [RUN, RUN, RELOAD, SHOOT] (Type 2)
        assertTrue(confirmation);
        wantsToDoList.clear();
        wantsToDoList.add(ActionElement.RUN);
        confirmation = m.confirmActions(pl1, wantsToDoList);
        // Player2 ("Vila"), it's not your turn!
        assertFalse(confirmation);
        wantsToDoList.add(ActionElement.RUN);
        wantsToDoList.add(ActionElement.RELOAD);
        wantsToDoList.add(ActionElement.SHOOT);
        confirmation = m.confirmActions(pl2, wantsToDoList);
        // [RUN, RUN, RELOAD, SHOOT] permitted in this stage (Type 2)
        assertTrue(confirmation);
    }
}