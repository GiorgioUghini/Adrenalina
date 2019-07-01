package models.player;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MarkTest {
    @Test
    public void addMark(){
        Player pl1 = new Player( "Cosimo", "");
        Player pl2 = new Player( "Giorgio","");
        pl2.giveMark(2, pl1);
        int pl2Marks = pl2.getMarksFromPlayer(pl1);
        assertEquals(2, pl2Marks);
    }

    @Test
    public void tooMuchAddMarkSamePlayer(){
        Player pl1 = new Player( "Cosimo","");
        Player pl2 = new Player( "Giorgio", "");
        pl2.giveMark(10, pl1);
        int pl2Marks = pl2.getMarksFromPlayer(pl1);
        assertEquals(3, pl2Marks);
    }

    @Test
    public void AddMarkDifferentPlayer(){
        Player pl1 = new Player( "Cosimo", "");
        Player pl2 = new Player( "Giorgio", "");
        Player pl3 = new Player( "Vilardo", "");
        pl2.giveMark(1, pl1);
        pl3.giveMark(1, pl1);
        pl2.giveMark(2, pl1);
        int pl2Marks = pl2.getMarksFromPlayer(pl1);
        //assertEquals(2, pl2Marks); impl with at most 3 marks in total
        assertEquals(3, pl2Marks);
    }

    @Test
    public void testOverKill(){
        Player a = new Player("a", "a");
        Player b = new Player("b", "b");

        b.getDamage(12, a);
        assertEquals(1, a.getMarksFromPlayer(b));
    }
}
