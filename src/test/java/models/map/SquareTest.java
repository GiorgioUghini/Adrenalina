package models.map;

import errors.NotWallException;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class SquareTest {
    private Square square;
    private UUID uuid = UUID.randomUUID();

    @Test
    public void ammoPoint(){
        square = new AmmoPoint(RoomColor.BLUE, 0, UUID.randomUUID());
        assertEquals(square.getColor(), RoomColor.BLUE);
        assertFalse(square.isSpawnPoint());
    }
    @Test
    public void spawnPoint(){
        square = new SpawnPoint(RoomColor.GREEN, 0, UUID.randomUUID());
        assertEquals(square.getColor(), RoomColor.GREEN);
        assertTrue(square.isSpawnPoint());
    }
    @Test
    public void connectSameRoom(){
        Square s1 = new AmmoPoint(RoomColor.GREEN, 0, uuid);
        Square s2 = new AmmoPoint(RoomColor.GREEN, 1, uuid);

        s1.connectToSquare(s2, CardinalDirection.RIGHT);
        s2.connectToSquare(s1, CardinalDirection.LEFT);

        assertEquals(s2, s1.getNextSquare(CardinalDirection.RIGHT));
        assertEquals(s1, s2.getNextSquare(CardinalDirection.LEFT));

        assertFalse(s1.getLink(CardinalDirection.RIGHT).isWall());
        assertFalse(s1.getLink(CardinalDirection.RIGHT).isDoor());
        assertTrue(s1.getLink(CardinalDirection.RIGHT).isSameRoom());
        assertTrue(s1.getLink(CardinalDirection.RIGHT).isVisible());

        assertNull(s1.getNextSquare(CardinalDirection.TOP));
        assertNull(s1.getNextSquare(CardinalDirection.BOTTOM));
        assertNull(s1.getNextSquare(CardinalDirection.LEFT));
        assertFalse(s1.hasDoors());
    }
    @Test
    public void connectDifferentRoomThroughWall(){
        Square s1 = new AmmoPoint(RoomColor.GREEN, 0, uuid);
        Square s2 = new AmmoPoint(RoomColor.BLUE, 1, uuid);

        s1.connectToSquare(s2, CardinalDirection.RIGHT);
        s2.connectToSquare(s1, CardinalDirection.LEFT);

        assertEquals(s2, s1.getNextSquare(CardinalDirection.RIGHT));
        assertEquals(s1, s2.getNextSquare(CardinalDirection.LEFT));

        assertTrue(s1.getLink(CardinalDirection.RIGHT).isWall());
        assertFalse(s1.getLink(CardinalDirection.RIGHT).isDoor());
        assertFalse(s1.getLink(CardinalDirection.RIGHT).isSameRoom());
        assertFalse(s1.getLink(CardinalDirection.RIGHT).isVisible());
        assertFalse(s1.hasDoors());
    }

    @Test
    public void connectDifferentRoomsThroughDoor() throws NotWallException {
        Square s1 = new AmmoPoint(RoomColor.GREEN, 0, uuid);
        Square s2 = new AmmoPoint(RoomColor.BLUE, 1, uuid);

        s1.connectToSquare(s2, CardinalDirection.RIGHT);
        s2.connectToSquare(s1, CardinalDirection.LEFT);
        s1.addDoor(CardinalDirection.RIGHT);
        s2.addDoor(CardinalDirection.LEFT);

        assertEquals(s2, s1.getNextSquare(CardinalDirection.RIGHT));
        assertEquals(s1, s2.getNextSquare(CardinalDirection.LEFT));

        assertFalse(s1.getLink(CardinalDirection.RIGHT).isWall());
        assertTrue(s1.getLink(CardinalDirection.RIGHT).isDoor());
        assertFalse(s1.getLink(CardinalDirection.RIGHT).isSameRoom());
        assertTrue(s1.getLink(CardinalDirection.RIGHT).isVisible());
        assertTrue(s1.hasDoors());
    }
    @Test
    public void hasNextInSameRoom(){
        Square s1 = new AmmoPoint(RoomColor.GREEN, 0, uuid);
        Square s2 = new AmmoPoint(RoomColor.BLUE, 1, uuid);
        s1.connectToSquare(s2, CardinalDirection.RIGHT);
        s2.connectToSquare(s1, CardinalDirection.LEFT);
        assertTrue(s1.hasNext(CardinalDirection.RIGHT));
        assertTrue(s2.hasNext(CardinalDirection.LEFT));
        assertFalse(s1.hasNext(CardinalDirection.RIGHT, true));
        assertFalse(s2.hasNext(CardinalDirection.LEFT, true));
    }

}
