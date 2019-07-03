package models.card;

import controllers.CardController;
import errors.WeaponCardException;
import models.Match;
import models.map.GameMap;
import models.map.RoomColor;
import models.map.SpawnPoint;
import models.player.Ammo;
import models.player.Player;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PowerupTest {
    @Test
    public void testEquals(){
        PowerUpCard teletrasporto = getPowerUpByName("Teletrasporto");
        PowerUpCard teletrasporto2 = getPowerUpByName("Teletrasporto");
        PowerUpCard raggioTraente = getPowerUpByName("Raggio traente");

        assertTrue(teletrasporto.equals(teletrasporto));
        assertFalse(teletrasporto.equals(null));
        assertFalse(teletrasporto.equals(new Object()));
        assertFalse(teletrasporto.equals(raggioTraente));
        assertTrue(teletrasporto.equals(teletrasporto2));
    }

    @Test
    public void testFullName(){
        PowerUpCard teletrasporto = getPowerUpByName("Teletrasporto", RoomColor.RED);
        assertNotNull(teletrasporto);
        assertEquals("Teletrasporto(R)", teletrasporto.getFullName());

    }

    @Test
    public void testPayPrice(){
        PowerUpCard teletrasporto = getPowerUpByName("Teletrasporto");
        PowerUpCard mirino = getPowerUpByName("Mirino");

        assertNotNull(teletrasporto);
        assertNotNull(mirino);

        teletrasporto.payPrice(null, null, null); //does not have price
        Ammo myAmmo = new Ammo(3,3,3);
        Ammo whichAmmo = new Ammo(1,0,0);

        mirino.payPrice(myAmmo, whichAmmo, null);
        assertEquals(new Ammo(2,3,3), myAmmo);
    }

    @Test
    public void testPayPriceWrongAmmo(){
        PowerUpCard mirino = getPowerUpByName("Mirino");
        assertNotNull(mirino);

        Ammo myAmmo = new Ammo(3,3,3);
        Ammo whichAmmo = new Ammo(2,0,0);

        try{
            mirino.payPrice(myAmmo, whichAmmo, null);
            assert false;
        }catch (WeaponCardException e){
            assertEquals("You have to pay exactly one ammo of any color", e.getMessage());
        }
    }

    @Test
    public void testPayWithPowerUp(){
        Player player = new Player("x", "");
        PowerUpCard mirino = getPowerUpByName("Mirino");
        mirino.setPlayer(player);
        assertNotNull(mirino);
        Ammo myAmmo = new Ammo(0,0,0);
        Ammo whichAmmo = null;
        PowerUpCard toPay = getPowerUpByName("Teletrasporto");
        mirino.payPrice(myAmmo, whichAmmo, toPay);
    }

    @Test
    public void testMandatoryFields(){
        PowerUpDeck deck = new CardController().getPowerUpDeck();
        int deckSize = deck.size();
        for(int i =0; i<deckSize; i++){
            PowerUpCard card = (PowerUpCard)deck.draw();
            assertNotNull(card.name);
            assertNotNull(card.color);
            assertNotNull(card.when);
            assertNotNull(card.image);
            for(Action action : card.effects){
                switch (action.type){
                    case SELECT:
                        assertNotNull(action.select.id);
                        assertNotNull(action.select.type);
                        break;
                    case DAMAGE:
                        assertNotNull(action.damage.target);
                        assertNotNull(action.damage.type);
                        assertNotEquals("me", action.damage.target);
                        break;
                    case MOVE:
                        assertNotNull(action.move.target);
                        assertNotNull(action.move.dest);
                        break;
                    case MARK:
                        assertNotNull(action.mark.target);
                        assertNotNull(action.mark.type);
                        assertNotEquals("me", action.mark.target);
                        break;
                }
            }
        }
    }

    @Test
    public void testTeletrasporto(){
        Player me = new Player("me", "password");
        Player p1 = new Player("p1", "password");
        Player p2 = new Player("p2", "password");
        List<Player> players = new ArrayList<>();
        players.add(me);
        players.add(p1);
        players.add(p2);
        Match match = new Match(players);
        match.createMap(0);
        GameMap gameMap = match.getMap();
        PowerUpCard teletrasporto = getPowerUpByName("Teletrasporto");
        List<PowerUpCard> myPowerups = new ArrayList<>();
        myPowerups.add(teletrasporto);
        me.setPowerUpList(myPowerups);
        Set<SpawnPoint> spawnPoints = gameMap.getSpawnPoints();
        SpawnPoint destSquare = null;
        for(SpawnPoint spawnPoint : spawnPoints){
            if(spawnPoint.getColor().equals(RoomColor.YELLOW)){
                gameMap.spawnPlayer(me, spawnPoint);
            }else if(spawnPoint.getColor().equals(RoomColor.RED)){
                destSquare = spawnPoint;
            }
        }
        match.chooseMapAndStartMatch();
        match.nextTurn();
        while (!match.getCurrentPlayer().equals(me)){
            match.nextTurn();
        }
        me.playPowerUp(teletrasporto, null, null);
        me.playNextPowerUpAction(); //select
        Selectable landingSquares = teletrasporto.getSelectable();
        assertEquals(gameMap.getAllSquares().size(), landingSquares.get().size());
        teletrasporto.select(destSquare);
        me.playNextPowerUpAction(); //move
        assertEquals(destSquare, gameMap.getPlayerPosition(me));
        me.resetWeapon();
    }

    @Test
    public void testThrowbackGrenade(){
        //init
        PowerUpCard granataVenom = getPowerUpByName("Granata Venom");
        assertNotNull(granataVenom);
        List<PowerUpCard> powerUpCardList = new ArrayList<>();
        powerUpCardList.add(granataVenom);
        List<Player> players = createTestPlayers(3);
        Match match = new Match(players);
        match.createMap(3);
        match.chooseMapAndStartMatch();
        Player me = players.get(0);
        me.setPowerUpList(powerUpCardList);
        Player offender = players.get(1);
        GameMap gameMap = match.getMap();

        //spawn players and set up turns
        for(Player p : players){
            spawnPlayer(p, gameMap, RoomColor.YELLOW);
        }
        match.nextTurn();
        while(!match.getCurrentPlayer().equals(offender)){
            match.nextTurn();
        }

        //play the powerup
        me.getDamage(2, offender);
        Player tmp = me.getLastDamager();
        me.playPowerUp(granataVenom, null, null);
        me.playNextPowerUpAction(); //select
        Selectable selectable = granataVenom.getSelectable();
        assertEquals(1, selectable.get().size());
        assertTrue(selectable.get().contains(offender));
        granataVenom.select(offender);
        me.playNextPowerUpAction();
        assertEquals(1, offender.getMarksFromPlayer(me));
    }

    private List<Player> createTestPlayers(int number){
        List<Player> out = new ArrayList<>();
        out.add(new Player("me", "password"));
        for(int i = 1; i<number; i++){
            out.add(new Player("p"+i, "password"));
        }
        return out;
    }

    private PowerUpCard getPowerUpByName(String name, RoomColor color){
        PowerUpDeck powerUpDeck = new CardController().getPowerUpDeck();
        int deckSize = powerUpDeck.size();
        for(int i=0; i<deckSize; i++){
            PowerUpCard card = (PowerUpCard) powerUpDeck.draw();
            if(card.name.equals(name)){
                if(color==null) return card;
                if(color.equals(card.color)) return card;
            }
        }
        return null;
    }

    private PowerUpCard getPowerUpByName(String name){
        return getPowerUpByName(name, null);
    }

    private void spawnPlayer(Player player, GameMap gameMap, RoomColor spawnPointColor){
        Set<SpawnPoint> spawnPoints = gameMap.getSpawnPoints();
        for(SpawnPoint s : spawnPoints){
            if(s.getColor().equals(spawnPointColor)){
                gameMap.spawnPlayer(player, s);
            }
        }
    }
}
