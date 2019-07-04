package models.player;

import controllers.CardController;
import errors.NotEnoughAmmoException;
import errors.WeaponCardException;
import models.Match;
import models.card.*;
import models.map.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PlayerTest {

    @Test
    public void testEquals() {
        Player a = new Player("a", "");
        Player b = new Player("b", "");
        assertEquals(a, a);
        assertEquals(a, new Player("a", ""));
        assertNotEquals(a, null);
        assertNotEquals(a, b);
        assertNotEquals(a, new Object());
    }

    @Test
    public void testDrawAmmo() {
        Player player = new Player("a", "");
        List<Player> players = new ArrayList<>();
        players.add(player);
        Match match = new Match(players);
        match.createMap(1);
        GameMap gameMap = match.getMap();
        SpawnPoint yellow = getSpawnPointByColor(gameMap, RoomColor.YELLOW);
        gameMap.spawnPlayer(player, yellow);
        Square square = gameMap.getPlayerPosition(player).getNextSquare(CardinalDirection.TOP);
        AmmoPoint ammoPoint = (AmmoPoint) square;
        AmmoCard ammoCard = new AmmoCard(2, 2, 0, true);
        ammoPoint.drawCard();
        ammoPoint.addCard(ammoCard);
        gameMap.movePlayer(player, ammoPoint);
        AmmoCard ammoCard1 = player.drawAmmoCard();
        assertEquals(ammoCard, ammoCard1);
        assertEquals(1, player.getPowerUpList().size());
    }

    @Test
    public void testDrawNullWeapon() {
        Player player = new Player("a", "");
        try {
            player.drawWeaponCard(null, null, null);
        } catch (NullPointerException e) {
            assertEquals("Drawn weapon cannot be null", e.getMessage());
        }
    }

    @Test
    public void testDrawNotEnoughAmmo() {
        Player player = new Player("a", "");
        WeaponCard weaponCard = getWeaponCardByName("Lanciarazzi");
        player.setAmmo(new Ammo());
        assertTrue(player.getAmmo().isEmpty());
        try {
            player.drawWeaponCard(weaponCard, null, null);
            assert false;
        } catch (NotEnoughAmmoException e) {
            assert true;
        } catch (Exception e) {
            assert false;
        }

    }

    @Test
    public void testDrawWeapon() {
        Player player = new Player("a", "");
        List<Player> players = new ArrayList<>();
        players.add(player);
        Match match = new Match(players);
        match.createMap(1);
        GameMap gameMap = match.getMap();
        SpawnPoint yellow = getSpawnPointByColor(gameMap, RoomColor.YELLOW);
        gameMap.spawnPlayer(player, yellow);
        Square square = gameMap.getPlayerPosition(player);
        SpawnPoint spawnPoint = (SpawnPoint) square;
        WeaponCard weaponCard = getWeaponCardByName("Lanciarazzi");
        spawnPoint.drawCard(spawnPoint.showCards().get(0));
        spawnPoint.addCard(weaponCard);
        assertNotNull(weaponCard);
        Ammo ammo = new Ammo(3, 3, 3);
        player.setAmmo(ammo);
        player.drawWeaponCard(weaponCard, null, null);
        assertEquals(weaponCard, player.getWeaponList().get(0));
    }

    @Test
    public void testDrawWeaponPayWithPowerUpYouDoNotHave() {
        Player player = new Player("a", "");
        List<Player> players = new ArrayList<>();
        players.add(player);
        Match match = new Match(players);
        match.createMap(1);
        GameMap gameMap = match.getMap();
        SpawnPoint yellow = getSpawnPointByColor(gameMap, RoomColor.YELLOW);
        gameMap.spawnPlayer(player, yellow);
        Square square = gameMap.getPlayerPosition(player);
        SpawnPoint spawnPoint = (SpawnPoint) square;
        WeaponCard weaponCard = getWeaponCardByName("Distruttore");
        spawnPoint.drawCard(spawnPoint.showCards().get(0));
        spawnPoint.addCard(weaponCard);
        assertNotNull(weaponCard);
        Ammo ammo = new Ammo(0, 0, 0);
        player.setAmmo(ammo);
        PowerUpCard toPay = getPowerUpByName("Teletrasporto", RoomColor.BLUE);
        try {
            player.drawWeaponCard(weaponCard, toPay, null);
            assert false;
        } catch (WeaponCardException e) {
            assertEquals("You do not have this powerup: Teletrasporto", e.getMessage());
        }
    }

    @Test
    public void testDrawWeaponPayWithPowerUp() {
        Player player = new Player("a", "");
        List<Player> players = new ArrayList<>();
        players.add(player);
        Match match = new Match(players);
        match.createMap(1);
        GameMap gameMap = match.getMap();
        SpawnPoint yellow = getSpawnPointByColor(gameMap, RoomColor.YELLOW);
        gameMap.spawnPlayer(player, yellow);
        Square square = gameMap.getPlayerPosition(player);
        SpawnPoint spawnPoint = (SpawnPoint) square;
        WeaponCard weaponCard = getWeaponCardByName("Distruttore");
        spawnPoint.drawCard(spawnPoint.showCards().get(0));
        spawnPoint.addCard(weaponCard);
        assertNotNull(weaponCard);
        Ammo ammo = new Ammo(0, 0, 0);
        player.setAmmo(ammo);
        PowerUpCard toPay = getPowerUpByName("Teletrasporto", RoomColor.BLUE);
        List<PowerUpCard> list = new ArrayList<>();
        list.add(toPay);
        player.setPowerUpList(list);
        player.drawWeaponCard(weaponCard, toPay, null);
        assertEquals(weaponCard, player.getWeaponList().get(0));
    }

    @Test
    public void testDrawWeaponReleaseWeapon() {
        Player player = new Player("a", "");
        List<Player> players = new ArrayList<>();
        players.add(player);
        Match match = new Match(players);
        match.createMap(1);
        GameMap gameMap = match.getMap();
        SpawnPoint yellow = getSpawnPointByColor(gameMap, RoomColor.YELLOW);
        gameMap.spawnPlayer(player, yellow);
        Square square = gameMap.getPlayerPosition(player);
        SpawnPoint spawnPoint = (SpawnPoint) square;
        WeaponCard weaponCard = getWeaponCardByName("Lanciarazzi");
        spawnPoint.drawCard(spawnPoint.showCards().get(0));
        spawnPoint.addCard(weaponCard);
        assertNotNull(weaponCard);
        Ammo ammo = new Ammo(3, 3, 3);
        player.setAmmo(ammo);
        player.setWeaponList(getRandomWeapons(3));
        player.drawWeaponCard(weaponCard, null, player.getWeaponList().get(0));
        assertEquals(3, player.getWeaponList().size());
        assertTrue(player.getWeaponList().contains(weaponCard));
    }

    private List<WeaponCard> getRandomWeapons(int howMany) {
        CardController cardController = new CardController();
        WeaponDeck weaponDeck = cardController.getWeaponDeck();
        List<WeaponCard> out = new ArrayList<>();
        for (int i = 0; i < howMany; i++) {
            out.add((WeaponCard) weaponDeck.draw());
        }
        return out;
    }

    @Test
    public void testGetPowerUpByName() {
        PowerUpCard powerUpCard = getPowerUpByName("Teletrasporto", RoomColor.BLUE);
        Player player = new Player("a", "");
        List<PowerUpCard> powerUpCardList = new ArrayList<>();
        powerUpCardList.add(powerUpCard);
        player.setPowerUpList(powerUpCardList);
        assertEquals(powerUpCard, player.getPowerUpByName("Teletrasporto", RoomColor.BLUE));
    }

    @Test
    public void testGetPowerUpByNameWrongColor() {
        PowerUpCard powerUpCard = getPowerUpByName("Teletrasporto", RoomColor.BLUE);
        Player player = new Player("a", "");
        List<PowerUpCard> powerUpCardList = new ArrayList<>();
        powerUpCardList.add(powerUpCard);
        player.setPowerUpList(powerUpCardList);
        try {
            player.getPowerUpByName("Teletrasporto", RoomColor.RED);
        } catch (WeaponCardException e) {
            assertEquals("User does not have this powerUp: Teletrasporto color: RED", e.getMessage());
        }
    }

    @Test
    public void isFirstPlayer() {
        Player pl1 = new Player("Giorgio", "");
        // pl1.setFirstPlayer(true);
        //assertTrue(pl1.isFirstPlayer());
    }

    @Test
    public void setName() {
        Player pl1 = new Player("Giorgio", "");
        assertEquals(pl1.getName(), "Giorgio");
    }

    @Test
    public void getAmmo() {
        Player pl1 = new Player("Giorgio", "");
        Ammo ammo = new Ammo();
        pl1.setAmmo(ammo);
        assertEquals(pl1.getAmmo(), ammo);
    }

    @Test
    public void getWeaponList() {
        Player pl1 = new Player("Giorgio", "");
        List<WeaponCard> deck = new ArrayList<>();
        pl1.setWeaponList(deck);
        assertEquals(pl1.getWeaponList(), deck);
    }

    @Test
    public void getPowerUpList() {
        Player pl1 = new Player("Giorgio", "");
        List<PowerUpCard> deck = new ArrayList<>();
        pl1.setPowerUpList(deck);
        assertEquals(pl1.getPowerUpList(), deck);
    }

    @Test
    public void drawWeaponCard() {
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
        me.setAmmo(new Ammo(3, 3, 3));
        assertFalse(yellow.showCards().isEmpty());
        WeaponCard toDraw = (WeaponCard) yellow.showCards().get(0);
        me.drawWeaponCard(toDraw, null, null);
        assertEquals(1, me.getWeaponList().size());
    }

    @Test
    public void drawPowerUpCard() {
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
        AmmoCard testCard = new AmmoCard(1, 2, 3, true);
        ammoPoint.drawCard();
        ammoPoint.addCard(testCard);
        assertTrue(me.getPowerUpList().isEmpty());
        me.drawAmmoCard();
        assertFalse(me.getPowerUpList().isEmpty());
        assertEquals(testCard.getAmmo(), me.getAmmo());
    }

    private SpawnPoint getSpawnPointByColor(GameMap gameMap, RoomColor color) {
        for (SpawnPoint spawnPoint : gameMap.getSpawnPoints()) {
            if (spawnPoint.getColor().equals(color)) return spawnPoint;
        }
        return null;
    }

    private WeaponCard getWeaponCardByName(String name) {
        CardController cardController = new CardController();
        WeaponDeck deck = cardController.getWeaponDeck();
        int size = deck.size();
        for (int i = 0; i < size; i++) {
            WeaponCard card = (WeaponCard) deck.draw();
            if (card.getName().equals(name)) return card;
        }
        return null;
    }

    private PowerUpCard getPowerUpByName(String name, RoomColor color) {
        CardController cardController = new CardController();
        PowerUpDeck deck = cardController.getPowerUpDeck();
        int size = deck.size();
        for (int i = 0; i < size; i++) {
            PowerUpCard card = (PowerUpCard) deck.draw();
            if (card.name.equals(name)) {
                if (color == null || color == card.color) return card;
            }
        }
        return null;
    }

}
