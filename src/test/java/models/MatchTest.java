package models;

import models.player.Player;
import models.turn.ActionElement;
import models.turn.ActionGroup;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class MatchTest {
    @Test
    public void testEquals(){
        List<Player> players = new ArrayList<>();
        players.add(new Player("A", ""));

        Match a = new Match(players);
        Match b = new Match(players);
        Match c = new Match(new ArrayList<>());

        assertEquals(a, b);
        assertNotEquals(a, c);
        assertNotEquals(a, new Object());
        assertNotEquals(a, null);
    }

    @Test
    public void testActivateFrenzy(){
        List<Player> players = new ArrayList<>();
        players.add(new Player("a", ""));
        players.add(new Player("b", ""));
        players.add(new Player("c", ""));

        Match match = new Match(players);
        match.activateFrenzy();
        assertEquals(players.get(0), match.getCurrentPlayer());
        assertEquals("a", players.get(0).getName());

        for(Player player : players){
            if(player.getName().equals("a")){
                assertEquals(ActionGroup.FRENZY_TYPE_2, player.getLifeState());
            }else{
                assertEquals(ActionGroup.FRENZY_TYPE_1, player.getLifeState());
            }
        }
    }

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