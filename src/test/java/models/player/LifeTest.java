package models.player;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LifeTest {
    @Test
    public void damage(){
        Player pl1 = new Player(false, "Cosimo");
        Player pl2 = new Player(false, "Giorgio");
        pl2.damage(2, pl1);
        Map<Player, Integer> pl2Damages = pl2.getDamages();
        assertEquals(2, pl2Damages.get(pl1).intValue());
        assertTrue(pl2Damages.containsKey(pl1));
    }

    @Test
    public void damageOverflow(){
        Player pl1 = new Player(false, "Cosimo");
        Player pl2 = new Player(false, "Giorgio");
        pl2.damage(24, pl1);
        Map<Player, Integer> pl2Damages = pl2.getDamages();
        assertEquals(12, pl2Damages.get(pl1).intValue());
    }
}
