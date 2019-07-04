package models;

import models.player.Player;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CircularArrayListTest {
    @Test
    public void testCreation() {
        List<Object> list = new ArrayList<>();
        Object a = new Object();
        list.add(a);

        CircularArrayList fromList = new CircularArrayList(list);
        CircularArrayList withInitialLen = new CircularArrayList(1);

        assertTrue(fromList.contains(a));
        assertTrue(withInitialLen.isEmpty());
    }

    @Test
    public void testGet() {
        CircularArrayList<Player> circularArrayList = new CircularArrayList<>();

        try {
            circularArrayList.get(1);
            assert false;
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        Player a = new Player("a", "");
        Player b = new Player("b", "");
        Player c = new Player("c", "");

        circularArrayList.add(a);
        circularArrayList.add(b);
        circularArrayList.add(c);

        assertEquals(c, circularArrayList.get(-1));
        assertEquals(a, circularArrayList.get(3));

    }
}
