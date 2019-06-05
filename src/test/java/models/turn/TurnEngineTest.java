package models.turn;

import org.junit.Test;
import static org.junit.Assert.*;

public class TurnEngineTest {

    @Test
    public void testStartCorrect(){
        TurnEngine turnEngine = new TurnEngine(TurnType.START_GAME, ActionGroup.NONE);
        turnEngine.draw();
        turnEngine.draw();
        turnEngine.spawn();
    }

    @Test
    public void testStartNoDraw(){
        try {
            TurnEngine turnEngine = new TurnEngine(TurnType.START_GAME, ActionGroup.NONE);
            turnEngine.spawn();
            fail();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @Test
    public void testStartNoSecondDraw(){
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
    public void testCorrectRespawn(){
        TurnEngine turnEngine = new TurnEngine(TurnType.RESPAWN, ActionGroup.NONE);
        turnEngine.draw();
        turnEngine.spawn();
    }

    @Test
    public void testRespawnNoDraw(){
        try {
            TurnEngine turnEngine = new TurnEngine(TurnType.RESPAWN, ActionGroup.NONE);
            turnEngine.spawn();
            fail();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }


    @Test
    public void testInGameNormalRunCorrect(){
        TurnEngine turnEngine = new TurnEngine(TurnType.IN_GAME, ActionGroup.NORMAL);
        turnEngine.run();
        turnEngine.reload();
    }

    @Test
    public void testInGameNormalRunGrabCorrect(){
        TurnEngine turnEngine = new TurnEngine(TurnType.IN_GAME, ActionGroup.NORMAL);
        turnEngine.run();
        turnEngine.grab();
        turnEngine.reload();

        turnEngine = new TurnEngine(TurnType.IN_GAME, ActionGroup.NORMAL);
        turnEngine.run();
        turnEngine.grab();
    }

    @Test
    public void testInGameNormalShootCorrect(){
        TurnEngine turnEngine = new TurnEngine(TurnType.IN_GAME, ActionGroup.NORMAL);
        turnEngine.shoot();

        turnEngine = new TurnEngine(TurnType.IN_GAME, ActionGroup.NORMAL);
        turnEngine.shoot();
        turnEngine.reload();
    }

    @Test
    public void testInGameNormalInvalidMoves(){
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
            turnEngine.reload();
            turnEngine.run();
            fail();
        } catch (Exception ex) {
            System.out.println(ex);
        }

        try {
            TurnEngine turnEngine = new TurnEngine(TurnType.IN_GAME, ActionGroup.NORMAL);
            turnEngine.reload();
            turnEngine.grab();
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
    public void testCorrectInGameLowLifeGrab(){
        TurnEngine turnEngine = new TurnEngine(TurnType.IN_GAME, ActionGroup.LOW_LIFE);
        turnEngine.run();
        turnEngine.run();
        turnEngine.grab();

        turnEngine = new TurnEngine(TurnType.IN_GAME, ActionGroup.LOW_LIFE);
        turnEngine.run();
        turnEngine.run();
        turnEngine.grab();
        turnEngine.reload();

        turnEngine = new TurnEngine(TurnType.IN_GAME, ActionGroup.VERY_LOW_LIFE);
        turnEngine.run();
        turnEngine.run();
        turnEngine.grab();
        turnEngine.reload();
    }

    @Test
    public void testCorrectInGameVeryLowLifeShoot(){
        TurnEngine turnEngine = new TurnEngine(TurnType.IN_GAME, ActionGroup.VERY_LOW_LIFE);
        turnEngine.run();
        turnEngine.shoot();

        turnEngine = new TurnEngine(TurnType.IN_GAME, ActionGroup.VERY_LOW_LIFE);
        turnEngine.run();
        turnEngine.shoot();
        turnEngine.reload();
    }
}
