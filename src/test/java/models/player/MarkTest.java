package models.player;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MarkTest {
    @Test
    public void addMark(){
        Player pl1 = new Player(false, "Cosimo");
        Player pl2 = new Player(false, "Giorgio");
        pl2.addMark(pl1, 2);
        int pl2Marks = pl2.getMarksFromPlayer(pl1);
        assertEquals(2, pl2Marks);
    }

    @Test
    public void tooMuchAddMarkSamePlayer(){
        Player pl1 = new Player(false, "Cosimo");
        Player pl2 = new Player(false, "Giorgio");
        pl2.addMark(pl1, 10);
        int pl2Marks = pl2.getMarksFromPlayer(pl1);
        assertEquals(3, pl2Marks);
    }

    @Test
    public void tooMuchAddMarkDifferentPlayer(){
        Player pl1 = new Player(false, "Cosimo");
        Player pl2 = new Player(false, "Giorgio");
        Player pl3 = new Player(false, "Vilardo");
        pl2.addMark(pl1, 1);
        pl3.addMark(pl1, 1);
        pl2.addMark(pl1, 2);
        int pl2Marks = pl2.getMarksFromPlayer(pl1);
        assertEquals(2, pl2Marks);
    }
}
