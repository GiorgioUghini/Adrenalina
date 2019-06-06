package models.card;

import controllers.CardController;
import models.map.*;
import models.player.Ammo;
import models.player.Player;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class EffectCardTest {
    private WeaponDeck weaponDeck;

    @Test
    public void testActivation(){
        weaponDeck = new CardController().getWeaponDeck();
        Ammo ammo = new Ammo();
        ammo.blue = 3;
        ammo.yellow = 3;
        ammo.red = 3;
        int deckSize = weaponDeck.size();
        for(int i = 0; i<deckSize; i++){
            WeaponCard weaponCard = (WeaponCard) weaponDeck.draw();
            assertEquals(0, weaponCard.getEffects(ammo).getLegitEffects().size());
            weaponCard.activate(new Player("player1", "password"));
            LegitEffects effects = weaponCard.getEffects(ammo);
            Ammo effectPrice;
            Effect chosenEffect;
            if(weaponCard.exclusive){
                assertEquals(effects.getAllEffects().size(), effects.getLegitEffects().size());
                chosenEffect = effects.getLegitEffects().get(0);
                effectPrice = chosenEffect.price;
                weaponCard.playEffect(effects.getLegitEffects().get(0), ammo);
                effects = weaponCard.getEffects(ammo);
                assertEquals(0, effects.getLegitEffects().size());
            }else{
                for(Effect e : effects.getLegitEffects()){
                    assertTrue(e.orderId==0 || e.orderId==-1);
                }
                chosenEffect = effects.getLegitEffects().get(0);
                effectPrice = chosenEffect.price;
                weaponCard.playEffect(chosenEffect, ammo);
                for(Effect e : effects.getLegitEffects()){
                    assertTrue(e.orderId==0 || e.orderId==1 || e.orderId == -1);
                }
            }
            assertEquals(ammo.blue, 3 - effectPrice.blue);
            assertEquals(ammo.red, 3 - effectPrice.red);
            assertEquals(ammo.yellow, 3 - effectPrice.yellow);
            ammo = new Ammo(3,3,3);
        }
    }

    @Test
    public void testDynamicBinding() {
        GameMap gameMap = MapGenerator.generate(1);
        Player player1 = new Player("alfa", "kek");
        Player player2 = new Player("bravo", "kek");
        SpawnPoint spawnPoint = (SpawnPoint) gameMap.getSpawnPoints().toArray()[0];
        gameMap.spawnPlayer(player1, spawnPoint);
        gameMap.spawnPlayer(player2, spawnPoint);
        if(spawnPoint.hasNext(CardinalDirection.RIGHT)){
            gameMap.movePlayer(player2, spawnPoint.getNextSquare(CardinalDirection.RIGHT));
        }else{
            gameMap.movePlayer(player2, spawnPoint.getNextSquare(CardinalDirection.LEFT));
        }


        WeaponCard lanciarazzi = getCard("Lanciarazzi");
        lanciarazzi.activate(player1);
        Ammo ammo = new Ammo(3, 3, 3);
        List<Effect> effects = lanciarazzi.getEffects(ammo).getLegitEffects();
        for(Effect e : effects){
            if(e.name.equals("Effetto base")){
                lanciarazzi.playEffect(e, ammo);
            }
        }
        lanciarazzi.playNextAction(); //select
        Set<Player> selectablePlayers = lanciarazzi.getSelectablePlayers().getPlayers();
        assertEquals(1, selectablePlayers.size());
        Player selectablePlayer = (Player) selectablePlayers.toArray()[0];
        assertEquals(player2, selectablePlayer);
        lanciarazzi.selectPlayer(player2);
        lanciarazzi.playNextAction(); //autoselect
        Set<Square> selectableSquares = lanciarazzi.getSelectableSquares().getSquares();
        assertEquals(1, selectableSquares.size());
        Square zeroSquare = (Square) selectableSquares.toArray()[0];
        assertEquals(gameMap.getPlayerPosition(player2), zeroSquare);
        lanciarazzi.selectSquare(zeroSquare);
        lanciarazzi.playNextAction(); //damage
        Map<Player, Integer> playersToDamage = lanciarazzi.getPlayersToDamage();
        assertTrue(playersToDamage.containsKey(player2));
        assertEquals(1, playersToDamage.size());
        boolean effectFound = false;
        for(Effect e : lanciarazzi.getEffects(ammo).getLegitEffects()){
            if(e.name.equals("Testata a frammentazione")){
                lanciarazzi.playEffect(e, ammo);
                effectFound = true;
            }
        }
        assertTrue(effectFound);
        lanciarazzi.playNextAction(); //damage
        playersToDamage = lanciarazzi.getPlayersToDamage();
        assertEquals(0, playersToDamage.size());
        lanciarazzi.playNextAction();
        playersToDamage = lanciarazzi.getPlayersToDamage();
        assertEquals(1, playersToDamage.size());
        assertTrue(playersToDamage.containsKey(player2));
        lanciarazzi.reset();
    }

    private WeaponCard getCard(String cardName){
        weaponDeck = new CardController().getWeaponDeck();
        Ammo ammo = new Ammo(3,3,3);
        int deckSize = weaponDeck.size();
        WeaponCard weaponCard;
        for(int i = 0; i<deckSize; i++){
            weaponCard = (WeaponCard) weaponDeck.draw();
            if(weaponCard.name.equals(cardName)){
                return weaponCard;
            }

        }
        return null;
    }
}
