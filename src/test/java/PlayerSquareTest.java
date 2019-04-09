import models.Player;
import models.map.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerSquareTest {
    @Test
    public void PlayerOnSquare(){
        Player player = new Player(false, "a");
        Square square = new SpawnPoint(RoomColor.GREEN, 0);

        PlayerSquare playerSquare = new PlayerSquare();
        playerSquare.addPlayer(player, square);

        assertTrue(playerSquare.hasPlayer(player));
        assertEquals(square, playerSquare.getSquare(player));
        assertTrue(playerSquare.getPlayers(square).contains(player));
    }
    @Test
    public void multiplePlayers(){
        Player p1 = new Player(false, "a");
        Player p2 = new Player(false, "a");
        Square square = new SpawnPoint(RoomColor.GREEN, 0);

        PlayerSquare playerSquare = new PlayerSquare();
        playerSquare.addPlayer(p1, square);
        playerSquare.addPlayer(p2, square);

        assertTrue(playerSquare.hasPlayer(p1));
        assertTrue(playerSquare.hasPlayer(p2));
        assertTrue(playerSquare.getSquare(p1).equals(square));
        assertTrue(playerSquare.getPlayers(square).contains(p1));
        assertTrue(playerSquare.getPlayers(square).contains(p2));
    }
    @Test
    public void movePlayerInSameRoom(){
        Player p = new Player(false, "a");
        Square s1 = new SpawnPoint(RoomColor.GREEN, 0);
        Square s2 = new AmmoPoint(RoomColor.GREEN, 1);

        PlayerSquare playerSquare = new PlayerSquare();

        boolean added = playerSquare.addPlayer(p, s1);
        assertTrue(added);
        assertTrue(playerSquare.hasPlayer(p));

        Square oldSquare = playerSquare.movePlayer(p, s2);
        assertEquals(s2, playerSquare.getSquare(p));
        assertEquals(s1, oldSquare);
        assertNotEquals(s1, playerSquare.getSquare(p));
    }
    @Test
    public void addSamePlayerTwice(){
        Player p = new Player(false, "a");
        Square s1 = new SpawnPoint(RoomColor.GREEN, 0);
        Square s2 = new AmmoPoint(RoomColor.PURPLE, 1);
        PlayerSquare playerSquare = new PlayerSquare();
        boolean added1 = playerSquare.addPlayer(p, s1);
        boolean added2 = playerSquare.addPlayer(p, s2);
        assertTrue(added1);
        assertFalse(added2);
        assertEquals(s1, playerSquare.getSquare(p));
        assertNotEquals(s2, playerSquare.getSquare(p));
        assertTrue(playerSquare.getPlayers(s1).contains(p));
        assertFalse(playerSquare.getPlayers(s2).contains(p));
    }
    @Test
    public void getPlayersNumber(){
        PlayerSquare playerSquare = new PlayerSquare();
        assertEquals(0, playerSquare.getPlayersNumber());
        Player p = new Player(false, 'a');
        Square square = new AmmoPoint(RoomColor.WHITE, 0);
        playerSquare.addPlayer(p, square);
        assertEquals(1, playerSquare.getPlayersNumber());
        playerSquare.addPlayer(p, square);
        assertEquals(1, playerSquare.getPlayersNumber());
    }
}
