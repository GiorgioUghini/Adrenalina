package models.player;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class LifeTest {
    @Test
    public void damage(){
        Player pl1 = new Player(false, "Cosimo");
        Player pl2 = new Player(false, "Giorgio");
        pl2.getDamaged(2, pl1);
        Map<Player, Integer> pl2Damages = pl2.getDamages();
        assertEquals(2, pl2Damages.get(pl1).intValue());
        Integer totalDamage = pl2Damages.values().stream().mapToInt(Integer::intValue).sum();
        assertEquals(2, totalDamage.intValue());
    }

    @Test
    public void damageOverflow(){
        Player pl1 = new Player(false, "Cosimo");
        Player pl2 = new Player(false, "Giorgio");
        pl2.getDamaged(24, pl1);
        Map<Player, Integer> pl2Damages = pl2.getDamages();
        assertEquals(12, pl2Damages.get(pl1).intValue());
        Integer totalDamage = pl2Damages.values().stream().mapToInt(Integer::intValue).sum();
        assertEquals(12, totalDamage.intValue());
    }

    @Test
    public void moreDifferentDamage(){
        Player pl1 = new Player(false, "Cosimo");
        Player pl2 = new Player(false, "Giorgio");
        pl2.getDamaged(2, pl1);
        pl2.getDamaged(3, pl1);
        pl2.getDamaged(1, pl1);
        Map<Player, Integer> pl2Damages = pl2.getDamages();
        assertEquals(6, pl2Damages.get(pl1).intValue());
        Integer totalDamage = pl2Damages.values().stream().mapToInt(Integer::intValue).sum();
        assertEquals(6, totalDamage.intValue());
    }

    @Test
    public void clearDamages(){
        Player pl2 = new Player(false, "Giorgio");
        Map<Player, Integer> pl2CountedPoints = pl2.countPoints();
        assertTrue(pl2CountedPoints.isEmpty());
    }

    @Test
    public void countPoints(){
        Player pl1 = new Player(false, "Cosimo");
        Player pl2 = new Player(false, "Giorgio");
        Player pl3 = new Player(false, "Vila");
        pl2.getDamaged(2, pl1);
        pl2.getDamaged(4, pl3);
        pl2.getDamaged(2, pl1);
        pl2.getDamaged(1, pl3);
        Map<Player, Integer> pl2Damages = pl2.getDamages();
        assertFalse(pl2Damages.isEmpty());
        pl2.clearDamages();
        pl2Damages = pl2.getDamages();
        assertTrue(pl2Damages.isEmpty());
    }

    @Test
    public void noDamageCountPoints(){
        Player pl2 = new Player(false, "Giorgio");
        Map<Player, Integer> pl2CountedPoints = pl2.countPoints();
        assertTrue(pl2CountedPoints.isEmpty());
    }

    @Test
    public void fiveTimesDeadCountPoints(){
        int[] assignablePoints = {8, 6, 4, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

        Player pl1 = new Player(false, "Cosimo");
        Player pl2 = new Player(false, "Giorgio");
        Player pl3 = new Player(false, "Vila");

        for (int skull = 0; skull <5; skull++) {
            pl2.clearDamages();
            pl2.setNumerOfSkulls(skull); //I'm dead some times before this
            pl2.getDamaged(2, pl1);
            pl2.getDamaged(4, pl3);
            pl2.getDamaged(2, pl1);
            pl2.getDamaged(1, pl3);
            Map<Player, Integer> pl2CountedPoints = pl2.countPoints();
            assertEquals(assignablePoints[1+skull] + 1, pl2CountedPoints.get(pl1).intValue());
            assertEquals(assignablePoints[skull], pl2CountedPoints.get(pl3).intValue());
        }

    }

    @Test
    public void isDead(){
        Player pl1 = new Player(false, "Cosimo");
        Player pl2 = new Player(false, "Giorgio");
        pl2.getDamaged(10, pl1);
        assertFalse(pl2.isDead());
        pl2.getDamaged(1, pl1);
        assertTrue(pl2.isDead());
    }




}
