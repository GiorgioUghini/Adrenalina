package models.card;

import controllers.CardController;
import models.Match;
import models.map.GameMap;
import models.map.RoomColor;
import models.map.SpawnPoint;
import models.player.Player;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PowerupTest {
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

    private PowerUpCard getPowerUpByName(String name){
        PowerUpDeck powerUpDeck = new CardController().getPowerUpDeck();
        int deckSize = powerUpDeck.size();
        for(int i=0; i<deckSize; i++){
            PowerUpCard card = (PowerUpCard) powerUpDeck.draw();
            if(card.name.equals(name)) return card;
        }
        return null;
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
