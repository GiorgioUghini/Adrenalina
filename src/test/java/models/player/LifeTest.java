package models.player;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class LifeTest {
    @Test
    public void damage(){
        Player pl1 = new Player( "Cosimo");
        Player pl2 = new Player( "Giorgio");
        pl2.getDamage(2, pl1);
        Map<Player, Integer> pl2Damages = pl2.getDamageMap();
        assertEquals(2, pl2Damages.get(pl1).intValue());
        Integer totalDamage = pl2Damages.values().stream().mapToInt(Integer::intValue).sum();
        assertEquals(2, totalDamage.intValue());
    }

    @Test
    public void marksAndDamage(){
        Player pl1 = new Player( "Cosimo");
        Player pl2 = new Player( "Giorgio");
        pl2.giveMark(1, pl1);
        pl2.getDamage(2, pl1);
        Map<Player, Integer> pl2Damages = pl2.getDamageMap();
        assertEquals(3, pl2Damages.get(pl1).intValue());
        Integer totalDamage = pl2Damages.values().stream().mapToInt(Integer::intValue).sum();
        assertEquals(3, totalDamage.intValue());
    }

    @Test
    public void damageOverflow(){
        Player pl1 = new Player( "Cosimo");
        Player pl2 = new Player( "Giorgio");
        pl2.getDamage(24, pl1);
        Map<Player, Integer> pl2Damages = pl2.getDamageMap();
        assertEquals(12, pl2Damages.get(pl1).intValue());
        Integer totalDamage = pl2Damages.values().stream().mapToInt(Integer::intValue).sum();
        assertEquals(12, totalDamage.intValue());
    }

    @Test
    public void moreDifferentDamage(){
        Player pl1 = new Player( "Cosimo");
        Player pl2 = new Player( "Giorgio");
        pl2.getDamage(2, pl1);
        pl2.getDamage(3, pl1);
        pl2.getDamage(1, pl1);
        Map<Player, Integer> pl2Damages = pl2.getDamageMap();
        assertEquals(6, pl2Damages.get(pl1).intValue());
        Integer totalDamage = pl2Damages.values().stream().mapToInt(Integer::intValue).sum();
        assertEquals(6, totalDamage.intValue());
    }

    @Test
    public void clearDamages(){
        Player pl2 = new Player( "Giorgio");
        Map<Player, Integer> pl2CountedPoints = pl2.countPoints();
        assertTrue(pl2CountedPoints.isEmpty());
    }

    @Test
    public void countPoints(){
        Player pl1 = new Player( "Cosimo");
        Player pl2 = new Player( "Giorgio");
        Player pl3 = new Player( "Vila");
        pl2.getDamage(2, pl1);
        pl2.getDamage(4, pl3);
        pl2.getDamage(2, pl1);
        pl2.getDamage(1, pl3);
        Map<Player, Integer> pl2Damages = pl2.getDamageMap();
        assertFalse(pl2Damages.isEmpty());
        pl2.clearDamages();
        pl2Damages = pl2.getDamageMap();
        assertTrue(pl2Damages.isEmpty());
    }

    @Test
    public void noDamageCountPoints(){
        Player pl2 = new Player( "Giorgio");
        Map<Player, Integer> pl2CountedPoints = pl2.countPoints();
        assertTrue(pl2CountedPoints.isEmpty());
    }

    @Test
    public void fiveTimesDeadCountPoints(){
        int[] assignablePoints = {8, 6, 4, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

        Player pl1 = new Player( "Cosimo");
        Player pl2 = new Player( "Giorgio");
        Player pl3 = new Player( "Vila");

        for (int skull = 0; skull <5; skull++) {
            pl2.clearDamages();
            pl2.setNumberOfSkulls(skull); //I'm dead some times before this
            pl2.getDamage(2, pl1);
            pl2.getDamage(4, pl3);
            pl2.getDamage(2, pl1);
            pl2.getDamage(3, pl3);
            Map<Player, Integer> pl2CountedPoints = pl2.countPoints();
            assertEquals(assignablePoints[1+skull] + 1, pl2CountedPoints.get(pl1).intValue());
            assertEquals(assignablePoints[skull], pl2CountedPoints.get(pl3).intValue());
        }

    }

    @Test
    public void revengeAddMarkCountPoints(){
        int[] assignablePoints = {8, 6, 4, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

        Player pl1 = new Player( "Cosimo");
        Player pl2 = new Player( "Giorgio");
        Player pl3 = new Player( "Vila");

        pl2.getDamage(5, pl1);
        pl2.getDamage(7, pl1);
        Map<Player, Integer> pl2CountedPoints = pl2.countPoints();
        assertEquals(assignablePoints[0] + 1, pl2CountedPoints.get(pl1).intValue());
        assertEquals(1, pl1.getMarksFromPlayer(pl2));

        pl2.clearDamages();
        pl2.setNumberOfSkulls(1); //I'm dead one time before this
        pl1.getDamage(5, pl2);
        pl1.getDamage(6, pl3);
        Map<Player, Integer> pl1CountedPoints = pl1.countPoints();
        assertEquals(assignablePoints[0] + 1, pl1CountedPoints.get(pl2).intValue());
    }

    @Test
    public void isDead(){
        Player pl1 = new Player( "Cosimo");
        Player pl2 = new Player( "Giorgio");
        pl2.getDamage(10, pl1);
        assertFalse(pl2.isDead());
        pl2.getDamage(1, pl1);
        assertTrue(pl2.isDead());
    }




}
