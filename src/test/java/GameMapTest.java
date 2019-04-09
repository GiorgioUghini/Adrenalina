import errors.NotWallException;
import models.Player;
import models.map.*;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class GameMapTest {
    @Test
    public void allSquaresInRoomAreConnected(){
        final int width = 2;
        final int height = 3;
        GameMap gameMap = new GameMap();
        gameMap.createRoom(width, height, RoomColor.PURPLE, new Coordinate(0,0));
        assertEquals(1, gameMap.getSpawnPoints().size());
        Square square = (SpawnPoint)gameMap.getSpawnPoints().toArray()[0];
        for(int y=0;y<height;y++){
            for(int x=0;x<width;x++){
                if(x==width-1 && y==height-1){
                    assertNull(square.getNextSquare(CardinalDirection.RIGHT));
                    assertNull(square.getNextSquare(CardinalDirection.BOTTOM));
                }else if(x==width-1) {
                    assertNull(square.getNextSquare(CardinalDirection.RIGHT));
                    assertNotNull(square.getNextSquare(CardinalDirection.BOTTOM));
                    while(square.hasNext(CardinalDirection.LEFT)){
                        square = square.getNextSquare(CardinalDirection.LEFT);
                    }
                    square = square.getNextSquare(CardinalDirection.BOTTOM);
                }else if(y==height-1) {
                    assertNull(square.getNextSquare(CardinalDirection.BOTTOM));
                    assertNotNull(square.getNextSquare(CardinalDirection.RIGHT));
                    square = square.getNextSquare(CardinalDirection.RIGHT);
                }else{
                    assertNotNull(square.getNextSquare(CardinalDirection.RIGHT));
                    assertNotNull(square.getNextSquare(CardinalDirection.BOTTOM));
                    square = square.getNextSquare(CardinalDirection.RIGHT);
                }
            }
        }
    }
    @Test
    public void allSquaresHaveDifferentIds(){
        GameMap gameMap = new GameMap();
        gameMap.createRoom(5,8, RoomColor.PURPLE, new Coordinate(1,1));
        gameMap.createRoom(3, 12, RoomColor.GREEN, new Coordinate(0,0));
        assertEquals(76, gameMap.getAllSquares().size());
    }
    @Test
    public void cannotCreateSameRoomColorTwice(){
        GameMap gameMap = new GameMap();
        gameMap.createRoom(3,4, RoomColor.WHITE, null);
        try{
            gameMap.createRoom(2,2, RoomColor.WHITE, new Coordinate(0,0));
            assert false;
        }catch(Exception e){
            assertEquals("Room already exists", e.getMessage());
        }
    }
    @Test
    public void getSquareById(){
        GameMap gameMap = new GameMap();
        gameMap.createRoom(3,4, RoomColor.GREEN, new Coordinate(2,3));
        Square square = (Square) gameMap.getSpawnPoints().toArray()[0];
        assertEquals(square, gameMap.getSquareById(11));
        assertNotEquals(square, gameMap.getSquareById(10));
    }
    @Test
    public void getSquareByIdInEmptyMap(){
        GameMap gameMap = new GameMap();
        assertNull(gameMap.getSquareById(11));
    }
    @Test
    public void getSquareByPositionInRoom(){
        GameMap gameMap = new GameMap();
        gameMap.createRoom(5,5, RoomColor.GREEN, new Coordinate(1,1));
        gameMap.createRoom(4,4, RoomColor.WHITE, null);
        Square square = (Square) gameMap.getSpawnPoints().toArray()[0];
        assertEquals(square, gameMap.getSquareByPositionInRoom(new Coordinate(1,1), RoomColor.GREEN));
    }
    @Test
    public void getSquareByPositionInRoomThatNotExists(){
        GameMap gameMap = new GameMap();
        try{
            assertNull(gameMap.getSquareByPositionInRoom(new Coordinate(1,1), RoomColor.GREEN));
            assert false;
        }catch (Exception e){
            assertEquals("No square with that color in this room", e.getMessage());
        }

        gameMap.createRoom(5,5, RoomColor.GREEN, new Coordinate(1,1));
        try{
            assertNull(gameMap.getSquareByPositionInRoom(new Coordinate(1,1), RoomColor.PURPLE));
            assert false;
        }catch (Exception e){
            assertEquals("No square with that color in this room", e.getMessage());
        }
    }
    @Test
    public void getSquareByPositionInRoomOutOfRange(){
        GameMap gameMap = new GameMap();
        gameMap.createRoom(2,2, RoomColor.GREEN, new Coordinate(1,1));
        assertNull(gameMap.getSquareByPositionInRoom(new Coordinate(3,0), RoomColor.GREEN));
        assertNull(gameMap.getSquareByPositionInRoom(new Coordinate(0,3), RoomColor.GREEN));
    }
    @Test
    public void getSquareByPositionInRoomOutOfRangeInOtherRoom() throws NotWallException{
        GameMap gameMap = new GameMap();
        gameMap.createRoom(2,2, RoomColor.GREEN, new Coordinate(1,1));
        gameMap.createRoom(2,2, RoomColor.WHITE, new Coordinate(1,1));
        gameMap.createRoom(2,2, RoomColor.BLUE, new Coordinate(1,1));

        Square s1 = gameMap.getSquareByPositionInRoom(new Coordinate(1,0), RoomColor.GREEN);
        Square s2 = gameMap.getSquareByPositionInRoom(new Coordinate(0,0), RoomColor.WHITE);
        gameMap.connectRooms(s1, s2, false, null);
        assertNull(gameMap.getSquareByPositionInRoom(new Coordinate(3,0), RoomColor.GREEN));

        s1 = gameMap.getSquareByPositionInRoom(new Coordinate(0,1), RoomColor.GREEN);
        s2 = gameMap.getSquareByPositionInRoom(new Coordinate(0,0), RoomColor.WHITE);
        gameMap.connectRooms(s1, s2, true, null);
        assertNull(gameMap.getSquareByPositionInRoom(new Coordinate(0,3), RoomColor.GREEN));
    }
    @Test
    public void connectRoomsVertically() throws NotWallException {
        GameMap gameMap = new GameMap();
        gameMap.createRoom(3,3, RoomColor.WHITE, new Coordinate(2,0));
        gameMap.createRoom(3,3, RoomColor.GREEN, new Coordinate(0,0));

        Square s1 = gameMap.getSquareByPositionInRoom(new Coordinate(2,0), RoomColor.WHITE);
        Square s2 = gameMap.getSquareByPositionInRoom(new Coordinate(0,0), RoomColor.GREEN);
        Set<Integer> doorOffsets = new HashSet<>();
        doorOffsets.add(0);

        gameMap.connectRooms(s1, s2, false, doorOffsets);

        for(int i=0;i<3;i++){
            if(i==0){
                assertTrue(s1.hasDoors());
                assertTrue(s2.hasDoors());
                assertTrue(s1.getLink(CardinalDirection.RIGHT).isDoor());
                assertTrue(s2.getLink(CardinalDirection.LEFT).isDoor());
            }else{
                assertFalse(s1.hasDoors());
                assertFalse(s2.hasDoors());
                assertTrue(s1.getLink(CardinalDirection.RIGHT).isWall());
                assertTrue(s2.getLink(CardinalDirection.LEFT).isWall());
            }

            assertTrue(s1.hasNext(CardinalDirection.RIGHT));
            assertTrue(s2.hasNext(CardinalDirection.LEFT));
            assertFalse(s1.hasNext(CardinalDirection.RIGHT, true));
            assertFalse(s2.hasNext(CardinalDirection.LEFT, true));
            assertEquals(s2, s1.getNextSquare(CardinalDirection.RIGHT));
            assertEquals(s1, s2.getNextSquare(CardinalDirection.LEFT));

            s1 = s1.getNextSquare(CardinalDirection.BOTTOM);
            s2 = s2.getNextSquare(CardinalDirection.BOTTOM);
        }

    }
    @Test
    public void connectRoomsHorizontally() throws NotWallException {
        GameMap gameMap = new GameMap();
        gameMap.createRoom(3,3, RoomColor.WHITE, new Coordinate(0,2));
        gameMap.createRoom(3,3, RoomColor.GREEN, new Coordinate(0,0));

        Square s1 = gameMap.getSquareByPositionInRoom(new Coordinate(0,2), RoomColor.WHITE);
        Square s2 = gameMap.getSquareByPositionInRoom(new Coordinate(0,0), RoomColor.GREEN);
        Set<Integer> doorOffsets = new HashSet<>();
        doorOffsets.add(2);

        gameMap.connectRooms(s1, s2, true, doorOffsets);

        for(int i=0;i<3;i++){
            if(i==2){
                assertTrue(s1.hasDoors());
                assertTrue(s2.hasDoors());
                assertTrue(s1.getLink(CardinalDirection.BOTTOM).isDoor());
                assertTrue(s2.getLink(CardinalDirection.TOP).isDoor());
            }else{
                assertFalse(s1.hasDoors());
                assertFalse(s2.hasDoors());
                assertTrue(s1.getLink(CardinalDirection.BOTTOM).isWall());
                assertTrue(s2.getLink(CardinalDirection.TOP).isWall());
            }

            assertTrue(s1.hasNext(CardinalDirection.BOTTOM));
            assertTrue(s2.hasNext(CardinalDirection.TOP));
            assertFalse(s1.hasNext(CardinalDirection.BOTTOM, true));
            assertFalse(s2.hasNext(CardinalDirection.TOP, true));
            assertEquals(s2, s1.getNextSquare(CardinalDirection.BOTTOM));
            assertEquals(s1, s2.getNextSquare(CardinalDirection.TOP));

            s1 = s1.getNextSquare(CardinalDirection.RIGHT);
            s2 = s2.getNextSquare(CardinalDirection.RIGHT);
        }

    }
    @Test
    public void addPlayer(){
        GameMap gameMap = new GameMap();
        gameMap.createRoom(3,4, RoomColor.WHITE, new Coordinate(0,0));
        Player[] players = new Player[6];
        for(int i=0;i<6;i++){
            players[i] = new Player();
        }
        gameMap.addPlayer(players[0]);
        gameMap.addPlayer(players[1]);
        gameMap.addPlayer(players[2]);
        assertEquals(3, gameMap.getPlayersNumber());
        try{
            gameMap.addPlayer(null);
            assert false;
        }catch(NullPointerException e){ }
        catch(Exception e){ assert false; }
        try{
            gameMap.addPlayer(players[1]);
            assert false;
        }catch (Exception e){
            assertEquals("Player already on this map", e.getMessage());
        }
        gameMap.addPlayer(players[3]);
        gameMap.addPlayer(players[4]);
        try{
            gameMap.addPlayer(players[5]);
            assert false;
        }catch(Exception e){
            assertEquals("Too many players on this map", e.getMessage());
        }
    }
    @Test
    public void spawnPlayer(){
        GameMap gameMap = new GameMap();
        gameMap.createRoom(3,3, RoomColor.PURPLE, new Coordinate(0,0));
        SpawnPoint spawnPoint = (SpawnPoint) gameMap.getSpawnPoints().toArray()[0];
        Player pippo = new Player();
        Player pluto = new Player();
        gameMap.addPlayer(pippo);
        gameMap.addPlayer(pluto);

        assertTrue(gameMap.getAllPlayers().contains(pippo));
        assertTrue(gameMap.getAllPlayers().contains(pluto));

        gameMap.spawnPlayer(pippo, spawnPoint);
        assertEquals(spawnPoint, gameMap.getPlayerPosition(pippo));
        assertEquals(pippo, gameMap.getPlayersOnSquare(spawnPoint).toArray()[0]);

        try{
            gameMap.spawnPlayer(pippo, spawnPoint);
            assert false;
        }catch (Exception e){
            assertEquals("Player has already spawned", e.getMessage());
        }

        Player notOnMap = new Player();
        try{
            gameMap.spawnPlayer(notOnMap, spawnPoint);
            assert false;
        }catch (Exception e){
            assertEquals("This player is not in this game", e.getMessage());
        }

        SpawnPoint stillNotOnMap = new SpawnPoint(RoomColor.PURPLE, 100);
        try{
            gameMap.spawnPlayer(pluto, stillNotOnMap);
            assert false;
        }catch (Exception e){
            assertEquals("Map does not contain this spawnPoint", e.getMessage());
        }
    }
    @Test
    public void getAllSquaresAtDistance1InSameRoom(){
        GameMap gameMap = new GameMap();
        gameMap.createRoom(3,3, RoomColor.GREEN, new Coordinate(1,1));
        SpawnPoint startSquare = (SpawnPoint) gameMap.getSpawnPoints().toArray()[0];
        Set<Square> squares = gameMap.getAllSquaresAtExactDistance(startSquare, 1);
        assertEquals(4, squares.size());
        assertTrue(squares.contains(gameMap.getSquareById(1)));
        assertTrue(squares.contains(gameMap.getSquareById(5)));
        assertTrue(squares.contains(gameMap.getSquareById(7)));
        assertTrue(squares.contains(gameMap.getSquareById(3)));
        assertFalse(squares.contains(startSquare));
    }
    @Test
    public void getAllSquaresAtDistance2InSameRoom(){
        GameMap gameMap = new GameMap();
        gameMap.createRoom(3,3, RoomColor.GREEN, new Coordinate(1,1));
        SpawnPoint startSquare = (SpawnPoint) gameMap.getSpawnPoints().toArray()[0];
        Set<Square> squares = gameMap.getAllSquaresAtExactDistance(startSquare, 2);
        assertEquals(4, squares.size());
        assertTrue(squares.contains(gameMap.getSquareById(0)));
        assertTrue(squares.contains(gameMap.getSquareById(2)));
        assertTrue(squares.contains(gameMap.getSquareById(8)));
        assertTrue(squares.contains(gameMap.getSquareById(6)));
        assertFalse(squares.contains(startSquare));
    }
    @Test
    public void getAllSquaresAtDistance3InSameRoom(){
        GameMap gameMap = new GameMap();
        gameMap.createRoom(7,7, RoomColor.GREEN, new Coordinate(1,1));
        Square startSquare = gameMap.getSquareById(24);
        Set<Square> squares = gameMap.getAllSquaresAtExactDistance(startSquare, 3);

        assertEquals(12, squares.size());
        int[] expectedSquareIds = {3,11,19,27,33,39,45,37,29,21,15,9};
        for(int i=0;i<12;i++){
            Square s = gameMap.getSquareById(expectedSquareIds[i]);
            assertTrue(squares.contains(s));
        }
        assertFalse(squares.contains(startSquare));
    }
    @Test
    public void getAllSquaresAtDistance3InDifferentRooms()throws NotWallException{
        GameMap gameMap = new GameMap();
        gameMap.createRoom(2,2, RoomColor.GREEN, new Coordinate(1,1));
        gameMap.createRoom(1,3, RoomColor.PURPLE, null);

        Set<Integer> doorOffsets = new HashSet<>();
        doorOffsets.add(1);
        gameMap.connectRooms(gameMap.getSquareById(1), gameMap.getSquareById(4), false, doorOffsets);

        Square startSquare = gameMap.getSquareById(1);
        Set<Square> squares = gameMap.getAllSquaresAtExactDistance(startSquare, 3);

        assertEquals(2, squares.size());
        int[] expectedSquareIds = {4,6};
        for(int i=0;i<2;i++){
            Square s = gameMap.getSquareById(expectedSquareIds[i]);
            assertTrue(squares.contains(s));
        }
        assertFalse(squares.contains(startSquare));
    }
    @Test
    public void getAllSquaresAtMaxDistance3InDifferentRooms()throws NotWallException{
        GameMap gameMap = new GameMap();
        gameMap.createRoom(2,2, RoomColor.GREEN, new Coordinate(1,1));
        gameMap.createRoom(1,3, RoomColor.PURPLE, null);

        Set<Integer> doorOffsets = new HashSet<>();
        doorOffsets.add(1);
        gameMap.connectRooms(gameMap.getSquareById(1), gameMap.getSquareById(4), false, doorOffsets);

        Square startSquare = gameMap.getSquareById(1);
        Set<Square> squares = gameMap.getAllSquaresAtDistanceLessThanOrEquals(startSquare, 3);

        assertEquals(7, squares.size());
        int[] expectedSquareIds = {0,1,2,3,4,5,6};
        for(int i=0;i<7;i++){
            Square s = gameMap.getSquareById(expectedSquareIds[i]);
            assertTrue(squares.contains(s));
        }
        assertTrue(squares.contains(startSquare));
    }
    @Test
    public void InvalidParamsInManhattanDistance(){
        GameMap gameMap = new GameMap();
        Square s = new AmmoPoint(RoomColor.PURPLE, 2);
        try{
            gameMap.getAllSquaresAtExactDistance(null, 2);
        }catch(NullPointerException e){
            assertEquals("'from' square cannot be null", e.getMessage());
        }
        try{
            gameMap.getAllSquaresAtExactDistance(s, -1);
        }catch (Exception e){
            assertEquals("Distance cannot be negative", e.getMessage());
        }

    }
    @Test
    public void getAllSquaresInRoom() throws NotWallException{
        GameMap gameMap = new GameMap();
        gameMap.createRoom(3,3, RoomColor.GREEN, null);
        gameMap.createRoom(3,4, RoomColor.WHITE, null);
        gameMap.connectRooms(gameMap.getSquareById(2), gameMap.getSquareById(9), false, null);
        Set<Square> squares = gameMap.getAllSquaresInRoom(RoomColor.GREEN);
        assertEquals(9, squares.size());
        for(Square s : squares){
            assertEquals(RoomColor.GREEN, s.getColor());
        }
    }
    @Test
    public void getAllVisibleSquaresNotOnDoor(){
        GameMap gameMap = new GameMap();
        gameMap.createRoom(3,4,RoomColor.GREEN, null);
        gameMap.createRoom(3,3, RoomColor.PURPLE, null);
        Set<Integer> doorOffsets = new HashSet<>();
        doorOffsets.add(0);
        gameMap.connectRooms(gameMap.getSquareById(2), gameMap.getSquareById(12), false, doorOffsets);

        Square myPosition = gameMap.getSquareById(0);
        Set<Square> visibleSquares = gameMap.getAllVisibleSquares(myPosition);
        assertEquals(12, visibleSquares.size());
        for(Square s : visibleSquares){
            assertEquals(RoomColor.GREEN, s.getColor());
        }
    }
    @Test
    public void getAllVisibleSquaresOnDoor(){
        GameMap gameMap = new GameMap();
        gameMap.createRoom(3,4,RoomColor.GREEN, null);
        gameMap.createRoom(3,3, RoomColor.PURPLE, null);
        gameMap.createRoom(2,2, RoomColor.WHITE, null);
        Set<Integer> doorOffsets = new HashSet<>();
        doorOffsets.add(0);
        gameMap.connectRooms(gameMap.getSquareById(2), gameMap.getSquareById(12), false, doorOffsets);
        gameMap.connectRooms(gameMap.getSquareById(14), gameMap.getSquareById(21), false, doorOffsets);

        Square myPosition = gameMap.getSquareById(2);
        Set<Square> visibleSquares = gameMap.getAllVisibleSquares(myPosition);
        assertEquals(21, visibleSquares.size());
        for(Square s : visibleSquares){
            assertTrue(s.getColor().equals(RoomColor.GREEN) || s.getColor().equals(RoomColor.PURPLE));
        }
    }
}
