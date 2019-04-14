package models.map;

import org.junit.Test;

import static org.junit.Assert.*;

public class MapGeneratorTest {
    @Test
    public void generateMap0(){
        GameMap map1 = MapGenerator.generate(0);
        assertEquals(10, map1.getAllSquares().size());
        Square tmp = map1.getSquareById(0);
        assertTrue(tmp.getLink(CardinalDirection.BOTTOM).isDoor());
        for(int i=0;i<2;i++){
            assertEquals(RoomColor.BLUE, tmp.getColor());
            tmp = tmp.getNextSquare(CardinalDirection.RIGHT);
        }
        assertEquals(RoomColor.BLUE, tmp.getColor());
        assertTrue(tmp.getLink(CardinalDirection.BOTTOM).isDoor());

        tmp = tmp.getNextSquare(CardinalDirection.BOTTOM);

        assertEquals(RoomColor.RED, tmp.getColor());
        assertTrue(tmp.getLink(CardinalDirection.RIGHT).isDoor());
        tmp = tmp.getNextSquare(CardinalDirection.RIGHT);
        assertEquals(RoomColor.YELLOW, tmp.getColor());
        tmp = tmp.getNextSquare(CardinalDirection.BOTTOM);
        assertEquals(RoomColor.YELLOW, tmp.getColor());
        assertTrue(tmp.getLink(CardinalDirection.LEFT).isDoor());
        tmp = tmp.getNextSquare(CardinalDirection.LEFT);
        assertEquals(RoomColor.WHITE, tmp.getColor());
        assertTrue(tmp.getLink(CardinalDirection.TOP).isWall());
        tmp = tmp.getNextSquare(CardinalDirection.LEFT);
        assertEquals(RoomColor.WHITE, tmp.getColor());
        assertTrue(tmp.getLink(CardinalDirection.TOP).isDoor());
    }
    @Test
    public void map1(){
        GameMap map1 = MapGenerator.generate(1);

        Square s = map1.getSquareById(0);
        assertEquals(RoomColor.BLUE, s.getColor());
        assertTrue(s.getLink(CardinalDirection.BOTTOM).isDoor());
        assertEquals(4, s.getNextSquare(CardinalDirection.BOTTOM).getId());
        assertTrue(s.getLink(CardinalDirection.RIGHT).isSameRoom());
        assertEquals(1, s.getNextSquare(CardinalDirection.RIGHT).getId());

        s = map1.getSquareById(1);
        assertEquals(RoomColor.BLUE, s.getColor());
        assertTrue(s.getLink(CardinalDirection.BOTTOM).isWall());
        assertEquals(5, s.getNextSquare(CardinalDirection.BOTTOM).getId());
        assertTrue(s.getLink(CardinalDirection.RIGHT).isSameRoom());
        assertEquals(2, s.getNextSquare(CardinalDirection.RIGHT).getId());

        s = map1.getSquareById(2);
        assertEquals(RoomColor.BLUE, s.getColor());
        assertTrue(s.getLink(CardinalDirection.BOTTOM).isDoor());
        assertEquals(6, s.getNextSquare(CardinalDirection.BOTTOM).getId());
        assertTrue(s.getLink(CardinalDirection.RIGHT).isDoor());
        assertEquals(3, s.getNextSquare(CardinalDirection.RIGHT).getId());

        s = map1.getSquareById(3);
        assertEquals(RoomColor.GREEN, s.getColor());
        assertTrue(s.getLink(CardinalDirection.BOTTOM).isDoor());
        assertEquals(7, s.getNextSquare(CardinalDirection.BOTTOM).getId());
        assertNull(s.getLink(CardinalDirection.RIGHT));

        s = map1.getSquareById(4);
        assertEquals(RoomColor.RED, s.getColor());
        assertNull(s.getLink(CardinalDirection.BOTTOM));
        assertTrue(s.getLink(CardinalDirection.RIGHT).isSameRoom());
        assertEquals(5, s.getNextSquare(CardinalDirection.RIGHT).getId());

        s = map1.getSquareById(5);
        assertEquals(RoomColor.RED, s.getColor());
        assertTrue(s.getLink(CardinalDirection.BOTTOM).isDoor());
        assertEquals(10, s.getNextSquare(CardinalDirection.BOTTOM).getId());
        assertTrue(s.getLink(CardinalDirection.RIGHT).isWall());
        assertEquals(6, s.getNextSquare(CardinalDirection.RIGHT).getId());

        s = map1.getSquareById(6);
        assertEquals(RoomColor.YELLOW, s.getColor());
        assertTrue(s.getLink(CardinalDirection.BOTTOM).isSameRoom());
        assertEquals(8, s.getNextSquare(CardinalDirection.BOTTOM).getId());
        assertTrue(s.getLink(CardinalDirection.RIGHT).isSameRoom());
        assertEquals(7, s.getNextSquare(CardinalDirection.RIGHT).getId());

        s = map1.getSquareById(7);
        assertEquals(RoomColor.YELLOW, s.getColor());
        assertTrue(s.getLink(CardinalDirection.BOTTOM).isSameRoom());
        assertEquals(9, s.getNextSquare(CardinalDirection.BOTTOM).getId());
        assertNull(s.getLink(CardinalDirection.RIGHT));

        s = map1.getSquareById(8);
        assertEquals(RoomColor.YELLOW, s.getColor());
        assertNull(s.getLink(CardinalDirection.BOTTOM));
        assertTrue(s.getLink(CardinalDirection.RIGHT).isSameRoom());
        assertEquals(9, s.getNextSquare(CardinalDirection.RIGHT).getId());

        s = map1.getSquareById(9);
        assertEquals(RoomColor.YELLOW, s.getColor());
        assertNull(s.getLink(CardinalDirection.BOTTOM));
        assertNull(s.getLink(CardinalDirection.RIGHT));

        s = map1.getSquareById(10);
        assertEquals(RoomColor.WHITE, s.getColor());
        assertNull(s.getLink(CardinalDirection.BOTTOM));
        assertTrue(s.getLink(CardinalDirection.RIGHT).isDoor());
        assertEquals(8, s.getNextSquare(CardinalDirection.RIGHT).getId());
    }
}
