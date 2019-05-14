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
    public void startTurnNextTurnEndTurnPossibleActions() {
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

    @Test
    public void confirmActions() {
        Match m = new Match();
        Player pl1 = new Player(false, "Cosimo");
        Player pl2 = new Player(false, "Giorgio");
        Player pl3 = new Player(false, "Vila");
        m.addPlayer(pl1);
        m.addPlayer(pl2);
        m.addPlayer(pl3);
        m.setFirstPlayer(pl2);
        assertEquals(pl2, m.getFirstPlayer());

        m.startMatch();

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

        m.endTurn(); //End Giorgio

        m.nextTurn();   //Start Cosimo
        m.endTurn();    //End Cosimo

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
        m.endTurn(); //End Vila

        m.nextTurn();   //Start Giorgio
        m.endTurn();    //End Giorgio

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
        m.endTurn(); //End Cosimo

        m.nextTurn();   //Start Vila
        m.endTurn();    //End Vila

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