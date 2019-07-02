package models.Turn;

import models.turn.Turn;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TurnTest {
    @Test
    public void testCreation(){
        Turn turn = new Turn();

        assertFalse(turn.hasFinished());
        turn.endTurn();
        assertTrue(turn.hasFinished());
    }
}
