package models.turn;

import models.turn.state.FourthRunStateBehaviour;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class TurnEngineTest {

    @Test
    public void testStartCorrect() {
        TurnEngine turnEngine = new TurnEngine(TurnType.START_GAME, ActionGroup.NONE);
        turnEngine.draw();
        turnEngine.draw();
        turnEngine.spawn();
        turnEngine.end();
    }

    @Test
    public void testStartNoDraw() {
        try {
            TurnEngine turnEngine = new TurnEngine(TurnType.START_GAME, ActionGroup.NONE);
            turnEngine.spawn();
            fail();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @Test
    public void testStartNoSecondDraw() {
        try {
            TurnEngine turnEngine = new TurnEngine(TurnType.START_GAME, ActionGroup.NONE);
            turnEngine.draw();
            turnEngine.spawn();
            fail();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @Test
    public void testCorrectRespawn() {
        TurnEngine turnEngine = new TurnEngine(TurnType.RESPAWN, ActionGroup.NONE);
        turnEngine.draw();
        turnEngine.spawn();
        turnEngine.end();
    }

    @Test
    public void testRespawnNoDraw() {
        try {
            TurnEngine turnEngine = new TurnEngine(TurnType.RESPAWN, ActionGroup.NONE);
            turnEngine.spawn();
            fail();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }


    @Test
    public void testInGameNormalRunCorrect() {
        TurnEngine turnEngine = new TurnEngine(TurnType.IN_GAME, ActionGroup.NORMAL);
        turnEngine.run();
        turnEngine.end();
    }

    @Test
    public void testInGameNormalRunGrabCorrect() {
        TurnEngine turnEngine = new TurnEngine(TurnType.IN_GAME, ActionGroup.NORMAL);
        turnEngine.run();
        turnEngine.grab();

        turnEngine = new TurnEngine(TurnType.IN_GAME, ActionGroup.NORMAL);
        turnEngine.run();
        turnEngine.grab();
        turnEngine.end();
    }

    @Test
    public void testInGameNormalShootCorrect() {
        TurnEngine turnEngine = new TurnEngine(TurnType.IN_GAME, ActionGroup.NORMAL);
        turnEngine.shoot();

        turnEngine = new TurnEngine(TurnType.IN_GAME, ActionGroup.NORMAL);
        turnEngine.shoot();
    }

    @Test
    public void testInGameNormalInvalidMoves() {
        try {
            TurnEngine turnEngine = new TurnEngine(TurnType.IN_GAME, ActionGroup.NORMAL);
            turnEngine.run();
            turnEngine.run();
            turnEngine.run();
            turnEngine.run();
            fail();
        } catch (Exception ex) {
            System.out.println(ex);
        }

        try {
            TurnEngine turnEngine = new TurnEngine(TurnType.IN_GAME, ActionGroup.NORMAL);
            turnEngine.run();
            turnEngine.shoot();
            fail();
        } catch (Exception ex) {
            System.out.println(ex);
        }

        try {
            TurnEngine turnEngine = new TurnEngine(TurnType.IN_GAME, ActionGroup.NORMAL);
            turnEngine.grab();
            turnEngine.run();
            fail();
        } catch (Exception ex) {
            System.out.println(ex);
        }

        try {
            TurnEngine turnEngine = new TurnEngine(TurnType.IN_GAME, ActionGroup.NORMAL);
            turnEngine.grab();
            turnEngine.run();
            fail();
        } catch (Exception ex) {
            System.out.println(ex);
        }

        try {
            TurnEngine turnEngine = new TurnEngine(TurnType.IN_GAME, ActionGroup.NORMAL);
            turnEngine.run();
            turnEngine.run();
            turnEngine.grab();
            fail();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @Test
    public void testCorrectInGameLowLifeGrab() {
        TurnEngine turnEngine = new TurnEngine(TurnType.IN_GAME, ActionGroup.LOW_LIFE);
        turnEngine.run();
        turnEngine.run();
        turnEngine.grab();

        turnEngine = new TurnEngine(TurnType.IN_GAME, ActionGroup.LOW_LIFE);
        turnEngine.run();
        turnEngine.run();
        turnEngine.grab();

        turnEngine = new TurnEngine(TurnType.IN_GAME, ActionGroup.VERY_LOW_LIFE);
        turnEngine.run();
        turnEngine.run();
        turnEngine.grab();
    }

    @Test
    public void testCorrectInGameVeryLowLifeShoot() {
        TurnEngine turnEngine = new TurnEngine(TurnType.IN_GAME, ActionGroup.VERY_LOW_LIFE);
        turnEngine.run();
        turnEngine.shoot();

        turnEngine = new TurnEngine(TurnType.IN_GAME, ActionGroup.VERY_LOW_LIFE);
        turnEngine.run();
        turnEngine.shoot();
    }

    @Test
    public void testCorrectInGameFrenzyType1() {
        TurnEngine turnEngine = new TurnEngine(TurnType.IN_GAME, ActionGroup.FRENZY_TYPE_1);
        turnEngine.run();
        turnEngine.reload();
        turnEngine.shoot();
        turnEngine.end();

        turnEngine = new TurnEngine(TurnType.IN_GAME, ActionGroup.FRENZY_TYPE_1);
        turnEngine.run();
        turnEngine.run();
        turnEngine.run();
        turnEngine.run();
        turnEngine.end();

        turnEngine = new TurnEngine(TurnType.IN_GAME, ActionGroup.FRENZY_TYPE_1);
        turnEngine.run();
        turnEngine.run();
        turnEngine.grab();
        turnEngine.end();
    }

    @Test
    public void testSetState() {
        try {
            TurnEngine turnEngine = new TurnEngine(new FourthRunStateBehaviour(TurnType.IN_GAME, ActionGroup.VERY_LOW_LIFE));
            turnEngine.run();
            turnEngine.end();
            fail();
        } catch (Exception ex) {
            System.out.println(ex);
        }

        TurnEngine turnEngine = new TurnEngine(new FourthRunStateBehaviour(TurnType.IN_GAME, ActionGroup.VERY_LOW_LIFE));
        turnEngine.end();
    }
}
