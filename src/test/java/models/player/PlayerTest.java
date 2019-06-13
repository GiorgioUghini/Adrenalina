package models.player;

import models.Match;
import models.card.AmmoCard;
import models.card.PowerUpCard;
import models.card.WeaponCard;
import models.map.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PlayerTest {

    @Test
    public void equals(){
        Player pl1 = new Player( "Cosimo", "");
        Player pl2 = new Player( "Giorgio", "");
        Player pl3 = pl1;
        assertTrue(! pl1.equals(pl2));
        assertEquals(pl1, pl3);
    }

    @Test
    public void isFirstPlayer(){
        Player pl1 = new Player( "Giorgio", "");
       // pl1.setFirstPlayer(true);
        //assertTrue(pl1.isFirstPlayer());
    }

    @Test
    public void setName(){
        Player pl1 = new Player( "Giorgio", "");
        assertEquals(pl1.getName(), "Giorgio");
    }

    @Test
    public void getAmmo(){
        Player pl1 = new Player( "Giorgio", "");
        Ammo ammo = new Ammo();
        pl1.setAmmo(ammo);
        assertEquals(pl1.getAmmo(), ammo);
    }

    @Test
    public void getWeaponList(){
        Player pl1 = new Player( "Giorgio", "");
        List<WeaponCard> deck = new ArrayList<>();
        pl1.setWeaponList(deck);
        assertEquals(pl1.getWeaponList(), deck);
    }

    @Test
    public void getPowerUpList(){
        Player pl1 = new Player( "Giorgio", "");
        List<PowerUpCard> deck = new ArrayList<>();
        pl1.setPowerUpList(deck);
        assertEquals(pl1.getPowerUpList(), deck);
    }

    @Test
    public void drawWeaponCard(){
        List<Player> players = new ArrayList<>();
        Player me = new Player("me", "password");
        players.add(me);
        players.add(new Player("p1", "password"));
        players.add(new Player("p2", "password"));
        Match match = new Match(players);
        match.createMap(1);
        match.chooseMapAndStartMatch();
        GameMap gameMap = match.getMap();
        SpawnPoint yellow = getSpawnPointByColor(gameMap, RoomColor.YELLOW);
        gameMap.spawnPlayer(me, yellow);
        me.setAmmo(new Ammo(3,3,3));
        assertFalse(yellow.showCards().isEmpty());
        WeaponCard toDraw = (WeaponCard) yellow.showCards().toArray()[0];
        me.drawWeaponCard(toDraw, null);
        assertEquals(1, me.getWeaponList().size());
    }

    @Test
    public void drawPowerUpCard(){
        List<Player> players = new ArrayList<>();
        Player me = new Player("me", "password");
        players.add(me);
        players.add(new Player("p1", "password"));
        players.add(new Player("p2", "password"));
        Match match = new Match(players);
        match.createMap(1);
        match.chooseMapAndStartMatch();
        GameMap gameMap = match.getMap();
        SpawnPoint yellow = getSpawnPointByColor(gameMap, RoomColor.YELLOW);
        AmmoPoint ammoPoint = (AmmoPoint) yellow.getNextSquare(CardinalDirection.LEFT);
        gameMap.spawnPlayer(me, yellow);
        gameMap.movePlayer(me, ammoPoint);
        AmmoCard testCard = new AmmoCard(1,2,3, true);
        ammoPoint.drawCard();
        ammoPoint.addCard(testCard);
        assertTrue(me.getPowerUpList().isEmpty());
        me.drawAmmoCard();
        assertFalse(me.getPowerUpList().isEmpty());
        assertEquals(testCard.getAmmo(), me.getAmmo());
    }

    private SpawnPoint getSpawnPointByColor(GameMap gameMap, RoomColor color){
        for(SpawnPoint spawnPoint : gameMap.getSpawnPoints()){
            if(spawnPoint.getColor().equals(color)) return spawnPoint;
        }
        return null;
    }

}
