package models.card;

import controllers.CardController;
import models.map.*;
import models.player.Ammo;
import models.player.Player;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class EffectCardTest {
    private WeaponDeck weaponDeck;

    @Test
    public void testActivation(){
        weaponDeck = new CardController().getWeaponDeck();
        Ammo ammo = new Ammo(3,3,3);
        int deckSize = weaponDeck.size();
        for(int i = 0; i<deckSize; i++){
            WeaponCard weaponCard = (WeaponCard) weaponDeck.draw();
            assertEquals(0, weaponCard.getEffects(ammo, null).getLegitEffects().size());
            weaponCard.setPlayer(new Player("player1", "password"));
            weaponCard.activate();
            LegitEffects effects = weaponCard.getEffects(ammo, new ArrayList<>());
            Ammo effectPrice;
            Effect chosenEffect;
            if(weaponCard.exclusive){
                assertEquals(effects.getAllEffects().size(), effects.getLegitEffects().size());
                chosenEffect = effects.getLegitEffects().get(0);
                effectPrice = chosenEffect.price;
                weaponCard.playEffect(effects.getLegitEffects().get(0), ammo, null);
                effects = weaponCard.getEffects(ammo, new ArrayList<>());
                assertEquals(0, effects.getLegitEffects().size());
            }else{
                for(Effect e : effects.getLegitEffects()){
                    assertTrue(e.orderId==0 || e.orderId==-1);
                }
                chosenEffect = effects.getLegitEffects().get(0);
                effectPrice = chosenEffect.price;
                weaponCard.playEffect(chosenEffect, ammo, null);
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
    public void testMandatoryFields(){
        WeaponDeck deck = new CardController().getWeaponDeck();
        int deckSize = deck.size();
        for(int i = 0; i<deckSize; i++){
            WeaponCard card = (WeaponCard) deck.draw();
            for(Effect effect : card.effects){
                for(Action action : effect.actions){
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
        WeaponCard lanciarazzi = getWeaponCard("Lanciarazzi");
        lanciarazzi.setPlayer(player1);
        lanciarazzi.activate();
        Ammo ammo = new Ammo(3, 3, 3);
        List<Effect> effects = lanciarazzi.getEffects(ammo, new ArrayList<>()).getLegitEffects();
        for(Effect e : effects){
            if(e.name.equals("Effetto base")){
                lanciarazzi.playEffect(e, ammo, null);
            }
        }
        lanciarazzi.playNextAction(); //select
        Set<Taggable> selectablePlayers = lanciarazzi.getSelectable().get();
        assertEquals(1, selectablePlayers.size());
        Player selectablePlayer = (Player) selectablePlayers.toArray()[0];
        assertEquals(player2, selectablePlayer);
        lanciarazzi.select(player2);
        lanciarazzi.playNextAction(); //autoselect
        Set<Taggable> selectableSquares = lanciarazzi.getSelectable().get();
        assertEquals(1, selectableSquares.size());
        Square zeroSquare = (Square) selectableSquares.toArray()[0];
        assertEquals(gameMap.getPlayerPosition(player2), zeroSquare);
        lanciarazzi.select(zeroSquare);
        lanciarazzi.playNextAction(); //damage
        Map<Player, Integer> playersToDamage = lanciarazzi.getPlayersToDamage();
        assertTrue(playersToDamage.containsKey(player2));
        assertEquals(1, playersToDamage.size());
        boolean effectFound = false;
        for(Effect e : lanciarazzi.getEffects(ammo, new ArrayList<>()).getLegitEffects()){
            if(e.name.equals("Testata a frammentazione")){
                lanciarazzi.playEffect(e, ammo, null);
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

    @Test
    public void testAreaVisibile(){
        WeaponCard distruttore = getWeaponCard("Distruttore");
        List<WeaponCard> weaponCards = new ArrayList<>();
        weaponCards.add(distruttore);
        Player me = new Player("a", "password");
        Player p1 = new Player("b", "password");
        Player p2 = new Player("c", "password");
        Player p3 = new Player("d", "password");
        GameMap gameMap = MapGenerator.generate(1);
        Set<SpawnPoint> spawnPoints = gameMap.getSpawnPoints();
        SpawnPoint yellow = null;
        SpawnPoint red = null;
        for(SpawnPoint s : spawnPoints){
            if(s.getColor().equals(RoomColor.YELLOW)){
                yellow = s;
            }else if(s.getColor().equals(RoomColor.RED)){
                red = s;
            }
        }
        if(yellow == null) assert false;
        gameMap.spawnPlayer(me, yellow);
        gameMap.spawnPlayer(p1, yellow);
        gameMap.spawnPlayer(p2, yellow);
        gameMap.spawnPlayer(p3, red);
        gameMap.movePlayer(me, yellow.getNextSquare(CardinalDirection.TOP).getNextSquare(CardinalDirection.LEFT));
        Ammo ammo = new Ammo(3,3,3);
        me.setAmmo(ammo);
        me.setWeaponList(weaponCards);
        me.playWeapon(distruttore);
        List<Effect> activableEffects = me.getWeaponEffects().getLegitEffects();
        assertEquals(1, activableEffects.size());
        Effect effect = activableEffects.get(0);
        assertEquals("Effetto base", effect.name);
        me.playWeaponEffect(effect, null);
        Action nextAction;
        while((nextAction = me.playNextWeaponAction())!=null){
            if(nextAction.type.equals(ActionType.SELECT) && !nextAction.select.auto){
                Selectable selectable = distruttore.getSelectable();
                assertEquals(2, selectable.get().size());
                distruttore.select(p1);
            }
        }
        assertEquals(2, p1.getTotalDamage());
        activableEffects = me.getWeaponEffects().getLegitEffects();
        assertEquals(1, activableEffects.size());
        me.playWeaponEffect(activableEffects.get(0), null);
        assertEquals(2, me.getAmmo().red);
        while((nextAction = me.playNextWeaponAction())!=null){
            if(nextAction.type.equals(ActionType.SELECT) && !nextAction.select.auto){
                Selectable selectable = distruttore.getSelectable();
                assertEquals(1, selectable.get().size());
                assertTrue(selectable.get().contains(p2));
                distruttore.select(p2);
            }
        }
        assertEquals(0, p2.getTotalDamage());
    }

    @Test
    public void testCannoneVortex(){
        GameMap gameMap = MapGenerator.generate(1);
        Player me = new Player("me", "password");
        Player p1 = new Player("p1", "password");
        Player p2 = new Player("p2", "password");
        Player p3 = new Player("p3", "password");

        WeaponCard cannoneVortex = getWeaponCard("Cannone vortex");
        SpawnPoint yellow = getSpawnPoint(gameMap, RoomColor.YELLOW);
        SpawnPoint red = getSpawnPoint(gameMap, RoomColor.RED);
        SpawnPoint blue = getSpawnPoint(gameMap, RoomColor.BLUE);

        gameMap.spawnPlayer(me, yellow);
        gameMap.spawnPlayer(p1, yellow);
        gameMap.movePlayer(p1, yellow.getNextSquare(CardinalDirection.TOP));
        gameMap.spawnPlayer(p2, blue);
        gameMap.spawnPlayer(p3, red);
        List<WeaponCard> myWeapons = new ArrayList<>();
        myWeapons.add(cannoneVortex);
        me.setWeaponList(myWeapons);
        me.setAmmo(new Ammo(3,3,3));
        me.playWeapon(cannoneVortex);
        List<Effect> effects = me.getWeaponEffects().getLegitEffects();
        assertEquals(1, effects.size());
        Effect effect = effects.get(0);
        assertEquals("Effetto base", effect.name);
        me.playWeaponEffect(effect, null);

        me.playNextWeaponAction(); //select square
        Selectable selectable = cannoneVortex.getSelectable();
        assertEquals(3, selectable.get().size());
        Square selectedSquare = yellow.getNextSquare(CardinalDirection.TOP).getNextSquare(CardinalDirection.LEFT);
        assertTrue(selectable.get().contains(selectedSquare));
        cannoneVortex.select(selectedSquare);
        me.playNextWeaponAction(); //select player
        selectable = cannoneVortex.getSelectable();
        assertEquals(2, selectable.get().size());
        assertTrue(selectable.get().contains(p1));
        assertTrue(selectable.get().contains(p2));
        cannoneVortex.select(p2);
        me.playNextWeaponAction(); //move P2 into vortex
        assertEquals(selectedSquare, gameMap.getPlayerPosition(p2));
        me.playNextWeaponAction(); // damage
        assertEquals(2, p2.getTotalDamage());
        assertEquals(0, p1.getTotalDamage());
        assertEquals(0, p3.getTotalDamage());
    }

    @Test
    public void testVulcanizzatore(){
        GameMap gameMap = MapGenerator.generate(1);
        List<Player> players = createTestPlayers(4);
        WeaponCard vulcanizzatore = getWeaponCard("Vulcanizzatore");
        List<WeaponCard> myCards = new ArrayList<>();
        myCards.add(vulcanizzatore);
        Player me = players.get(0);
        me.setWeaponList(myCards);
        SpawnPoint yellow = getSpawnPoint(gameMap, RoomColor.YELLOW);
        SpawnPoint red = getSpawnPoint(gameMap, RoomColor.RED);
        gameMap.spawnPlayer(me, yellow);
        gameMap.spawnPlayer(players.get(1), yellow);
        gameMap.spawnPlayer(players.get(2), red);
        gameMap.spawnPlayer(players.get(3), red);
        gameMap.movePlayer(me, yellow.getNextSquare(CardinalDirection.TOP).getNextSquare(CardinalDirection.LEFT));
        gameMap.movePlayer(players.get(2), red.getNextSquare(CardinalDirection.TOP));
        gameMap.movePlayer(players.get(3), red.getNextSquare(CardinalDirection.TOP));
        assertEquals(RoomColor.BLUE, gameMap.getPlayerPosition(players.get(2)).getColor());
        assertEquals(RoomColor.BLUE, gameMap.getPlayerPosition(players.get(3)).getColor());
        assertTrue(gameMap.getPlayerPosition(me).hasDoors());
        me.setAmmo(new Ammo(3,3,3));
        me.playWeapon(vulcanizzatore);
        List<Effect> effects = me.getWeaponEffects().getLegitEffects();
        assertEquals(2, effects.size());
        for(Effect e : effects){
            if(e.name.equals("Modalità base")){
                me.playWeaponEffect(e, null);
            }
        }
        vulcanizzatore.playNextAction(); //select room
        Selectable selectableRooms = vulcanizzatore.getSelectable();
        assertEquals(1, selectableRooms.get().size());
        assertTrue(selectableRooms.get().contains(RoomColor.BLUE));
        vulcanizzatore.select(RoomColor.BLUE);
        me.playNextWeaponAction(); //damage
        assertEquals(0, players.get(1).getTotalDamage());
        assertEquals(1, players.get(2).getTotalDamage());
        assertEquals(1, players.get(3).getTotalDamage());
    }

    @Test
    public void testNoSelection(){
        WeaponCard razzoTermico = getWeaponCard("Razzo termico");
        List<WeaponCard> myWeapons = new ArrayList<>();
        myWeapons.add(razzoTermico);
        List<Player> players = createTestPlayers(3);
        GameMap gameMap = MapGenerator.generate(1);
        SpawnPoint yellow = getSpawnPoint(gameMap, RoomColor.YELLOW);
        SpawnPoint red = getSpawnPoint(gameMap, RoomColor.RED);
        Player me = players.get(0);
        me.setWeaponList(myWeapons);
        gameMap.spawnPlayer(me, yellow);
        gameMap.spawnPlayer(players.get(1), yellow);
        gameMap.spawnPlayer(players.get(2), red);
        me.setAmmo(new Ammo(3,3,3));
        me.playWeapon(razzoTermico);
        Effect effect = me.getWeaponEffects().getLegitEffects().get(0);
        me.playWeaponEffect(effect, null);
        me.playNextWeaponAction();
        Selectable selectable = razzoTermico.getSelectable();
        assertEquals(1, selectable.get().size());
        assertTrue(selectable.get().contains(players.get(2)));
        me.playNextWeaponAction();
        for (Player p : players){
            assertEquals(0, p.getTotalDamage());
        }
    }

    @Test
    public void testPayWithPowerups(){
        //init vars
        List<Player> players = createTestPlayers(3);
        GameMap gameMap = MapGenerator.generate(1);
        SpawnPoint yellow = getSpawnPoint(gameMap, RoomColor.YELLOW);
        SpawnPoint blue = getSpawnPoint(gameMap, RoomColor.BLUE);
        WeaponCard lanciafiamme = getWeaponCard("Lanciafiamme");
        List<WeaponCard> weaponCardList = new ArrayList<>();
        weaponCardList.add(lanciafiamme);
        PowerUpCard powerUpCard = getPowerupCard("Teletrasporto", RoomColor.YELLOW);
        List<PowerUpCard> powerUpCardList = new ArrayList<>();
        powerUpCardList.add(powerUpCard);
        Player me = players.get(0);
        me.setWeaponList(weaponCardList);
        me.setPowerUpList(powerUpCardList);
        me.setAmmo(new Ammo(0, 0, 1));

        //position players
        gameMap.spawnPlayer(me, yellow);
        gameMap.spawnPlayer(players.get(1), blue);
        gameMap.spawnPlayer(players.get(2), blue);
        gameMap.movePlayer(me, yellow.getNextSquare(CardinalDirection.LEFT));
        gameMap.movePlayer(players.get(1), blue.getNextSquare(CardinalDirection.BOTTOM));

        //play the card
        me.playWeapon(lanciafiamme);

        //chose the effect
        LegitEffects effects = me.getWeaponEffects();
        assertEquals(2, effects.getLegitEffects().size());
        for(Effect e : effects.getLegitEffects()){
            if(e.name.equals("Modalità barbecue")){
                me.playWeaponEffect(e, powerUpCard);
            }
        }

        assertEquals(new Ammo(), me.getAmmo());
        assertTrue(me.getPowerUpList().isEmpty());

        //play the effect
        me.playNextWeaponAction(); //select
        Selectable selectable = lanciafiamme.getSelectable();
        assertEquals(3, selectable.get().size());
        Square squareToSelect = gameMap.getPlayerPosition(me).getNextSquare(CardinalDirection.TOP);
        lanciafiamme.select(squareToSelect);
        me.playNextWeaponAction(); //select
        selectable = lanciafiamme.getSelectable();
        assertEquals(1, selectable.get().size());
        lanciafiamme.select((Taggable) selectable.get().toArray()[0]);
        me.playNextWeaponAction(); //damage square 1
        me.playNextWeaponAction(); //damage square 2
        assertEquals(2, players.get(1).getTotalDamage());
        assertEquals(1, players.get(2).getTotalDamage());
    }

    @Test
    public void testCardinalDirection(){
        WeaponCard fucileLaser = getWeaponCard("Fucile laser");
        List<WeaponCard> weaponCardList = new ArrayList<>();
        weaponCardList.add(fucileLaser);
        assertNotNull(fucileLaser);
        List<Player> players = createTestPlayers(4);
        GameMap gameMap = MapGenerator.generate(1);
        Player me = players.get(0);
        SpawnPoint yellow = getSpawnPoint(gameMap, RoomColor.YELLOW);
        for(Player player : players){
            gameMap.spawnPlayer(player, yellow);
        }
        me.setWeaponList(weaponCardList);
        gameMap.movePlayer(me, yellow.getNextSquare(CardinalDirection.LEFT));
        gameMap.movePlayer(players.get(1), yellow.getNextSquare(CardinalDirection.TOP).getNextSquare(CardinalDirection.LEFT));
        gameMap.movePlayer(players.get(2), gameMap.getPlayerPosition(players.get(1)).getNextSquare(CardinalDirection.TOP));
        gameMap.movePlayer(players.get(3), yellow.getNextSquare(CardinalDirection.TOP));
        me.playWeapon(fucileLaser);
        List<Effect> legitEffects = me.getWeaponEffects().getLegitEffects();
        assertEquals(2, legitEffects.size());
        Effect effect = legitEffects.stream().filter(e -> e.name.equals("Modalità base")).findFirst().orElse(null);
        assertEquals("Modalità base", effect.name);
        me.playWeaponEffect(effect, null);
        me.playNextWeaponAction(); //select
        Selectable selectable = fucileLaser.getSelectable();
        Set<Taggable> taggables = selectable.get();
        assertEquals(2, taggables.size());
        assertTrue(taggables.contains(players.get(1)));
        assertTrue(taggables.contains(players.get(2)));
    }

    @Test
    public void testRaggioSolare(){
        WeaponCard raggioSolare = getWeaponCard("Raggio solare");
        List<WeaponCard> weaponCardList = new ArrayList<>();
        weaponCardList.add(raggioSolare);
        assertNotNull(raggioSolare);
        List<Player> players = createTestPlayers(4);
        GameMap gameMap = MapGenerator.generate(1);
        Player me = players.get(0);
        Player enemy = players.get(1);
        SpawnPoint yellow = getSpawnPoint(gameMap, RoomColor.YELLOW);
        for(Player player : players){
            gameMap.spawnPlayer(player, yellow);
        }
        me.setWeaponList(weaponCardList);
        gameMap.movePlayer(enemy, yellow.getNextSquare(CardinalDirection.TOP));
        me.playWeapon(raggioSolare);
        List<Effect> legitEffects = me.getWeaponEffects().getLegitEffects();
        assertEquals(1, legitEffects.size());
        Effect effect = legitEffects.stream().filter(e -> e.name.equals("Modalità base")).findFirst().orElse(null);
        assertEquals("Modalità base", effect.name);
        me.playWeaponEffect(effect, null);
        me.playNextWeaponAction(); //select
        Selectable selectable = raggioSolare.getSelectable();
        Set<Taggable> taggables = selectable.get();
        assertEquals(1, taggables.size());
        assertTrue(taggables.contains(enemy));
        raggioSolare.select(enemy);
        me.playNextWeaponAction(); //damage
        assertEquals(1, enemy.getTotalDamage());
        me.playNextWeaponAction(); //mark
        assertEquals(1, enemy.getMarksFromPlayer(me));
    }

    @Test
    public void testTorpedine(){
        WeaponCard torpedine = getWeaponCard("Torpedine");
        List<WeaponCard> weaponCardList = new ArrayList<>();
        weaponCardList.add(torpedine);
        assertNotNull(torpedine);
        List<Player> players = createTestPlayers(4);
        GameMap gameMap = MapGenerator.generate(1);
        Player me = players.get(0);
        Player enemy1 = players.get(1);
        Player enemy2 = players.get(2);
        Player enemy3 = players.get(3);
        me.setAmmo(new Ammo(3,3,3));

        SpawnPoint yellow = getSpawnPoint(gameMap, RoomColor.YELLOW);
        SpawnPoint blue = getSpawnPoint(gameMap, RoomColor.BLUE);
        SpawnPoint red = getSpawnPoint(gameMap, RoomColor.RED);

        gameMap.spawnPlayer(me, yellow);
        gameMap.spawnPlayer(enemy1,blue);
        gameMap.movePlayer(enemy1, blue.getNextSquare(CardinalDirection.BOTTOM));
        gameMap.spawnPlayer(enemy2, blue);
        gameMap.movePlayer(enemy2, blue.getNextSquare(CardinalDirection.LEFT).getNextSquare(CardinalDirection.LEFT));
        gameMap.spawnPlayer(enemy3, red);
        gameMap.movePlayer(enemy3, red.getNextSquare(CardinalDirection.RIGHT));

        me.setWeaponList(weaponCardList);
        me.playWeapon(torpedine);
        List<Effect> legitEffects = me.getWeaponEffects().getLegitEffects();
        assertEquals(1, legitEffects.size()); //effetto base
        Effect effect = legitEffects.stream().filter(e -> e.name.equals("Effetto base")).findFirst().orElse(null);
        assertEquals("Effetto base", effect.name);
        me.playWeaponEffect(effect, null);
        me.playNextWeaponAction(); //select
        Selectable selectable = torpedine.getSelectable();
        Set<Taggable> taggables = selectable.get();
        assertEquals(1, taggables.size());
        assertTrue(taggables.contains(enemy1));
        torpedine.select(enemy1);
        me.playNextWeaponAction(); //damage
        assertEquals(2, enemy1.getTotalDamage());
        assertEquals(0, enemy2.getTotalDamage());
        assertEquals(0, enemy3.getTotalDamage());

        legitEffects = me.getWeaponEffects().getLegitEffects();
        assertEquals(1, legitEffects.size());
        effect = legitEffects.stream().filter(e -> e.name.equals("Reazione a catena")).findFirst().orElse(null);
        assertEquals("Reazione a catena", effect.name);
        me.playWeaponEffect(effect, null);
        me.playNextWeaponAction(); //select
        selectable = torpedine.getSelectable();
        taggables = selectable.get();
        assertEquals(1, taggables.size());
        assertTrue(taggables.contains(enemy2));
        torpedine.select(enemy2);
        me.playNextWeaponAction(); //damage
        assertEquals(2, enemy1.getTotalDamage());
        assertEquals(1, enemy2.getTotalDamage());
        assertEquals(0, enemy3.getTotalDamage());

        legitEffects = me.getWeaponEffects().getLegitEffects();
        assertEquals(1, legitEffects.size());
        effect = legitEffects.stream().filter(e -> e.name.equals("Alta tensione")).findFirst().orElse(null);
        assertEquals("Alta tensione", effect.name);
        me.playWeaponEffect(effect, null);
        me.playNextWeaponAction(); //select
        selectable = torpedine.getSelectable();
        taggables = selectable.get();
        assertEquals(1, taggables.size());
        assertTrue(taggables.contains(enemy3));
        torpedine.select(enemy3);
        me.playNextWeaponAction(); //damage
        assertEquals(2, enemy1.getTotalDamage());
        assertEquals(1, enemy2.getTotalDamage());
        assertEquals(2, enemy3.getTotalDamage());
    }

    private SpawnPoint getSpawnPoint(GameMap gameMap, RoomColor color){
        for(SpawnPoint s : gameMap.getSpawnPoints()){
            if(s.getColor().equals(color)){
                return s;
            }
        }
        return null;
    }

    private List<Player> createTestPlayers(int number){
        List<Player> out = new ArrayList<>();
        out.add(new Player("me", "password"));
        for(int i = 1; i<number; i++){
            out.add(new Player("p"+i, "password"));
        }
        return out;
    }

    private WeaponCard getWeaponCard(String cardName){
        weaponDeck = new CardController().getWeaponDeck();
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

    private PowerUpCard getPowerupCard(String cardName, RoomColor color){
        PowerUpDeck powerUpDeck = new CardController().getPowerUpDeck();
        int deckSize = powerUpDeck.size();
        for(int i=0; i<deckSize; i++){
            PowerUpCard powerUpCard = (PowerUpCard) powerUpDeck.draw();
            if(powerUpCard.name.equals(cardName) && powerUpCard.color == color){
                return powerUpCard;
            }
        }
        return null;
    }
}