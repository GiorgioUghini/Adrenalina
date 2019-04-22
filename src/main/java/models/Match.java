package models;

import errors.MapNotExistsException;
import models.map.GameMap;
import models.map.MapGenerator;
import models.player.Player;
import models.turn.ActionElement;
import models.turn.ActionGroup;
import models.turn.Turn;

import java.util.*;

public class Match {
    private Player firstPlayer;
    private List<Player> playerList;
    private int actualPlayerIndex = 0;
    private PowerUpDeck powerUpDeck;
    private WeaponDeck weaponDeck;
    private GameMap gameMap;
    private Turn actualTurn;
    private boolean frenzy;
    private boolean frenzyType1 = true;

    public Match(){
        playerList = new ArrayList<>();
        actualTurn  = new Turn(true);
    }

    @Override
    public boolean equals(Object o) {
        // self check
        if (this == o)
            return true;
        // null check
        if (o == null)
            return false;
        // type check and cast
        if (getClass() != o.getClass())
            return false;
        //Match match = (Match) o;
        // field comparison
        //return firstPlayer.equals(match.firstPlayer);
        return true;
    }

    /** Activate frenzy from now on
     * @param player the player who is activating frenzy mode.*/
    public void activateFrenzy(Player player) {
        frenzy = true;
    }

    /** Get the first player on this match
     * @return the first player.*/
    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(Player firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    /** Add a player into this match
     * @param p the player who wants to join this match.*/
    public void addPlayer(Player p) {
        playerList.add(p);
    }

    /** Remove a player into this match
     * @param p the player who wants to leave this match.*/
    public void removePlayer(Player p) {
        playerList.remove(p);
    }

    /** Draw a card from this match' power up deck
     * @return the card that is drawn.*/
    public Card drawPowerUp() {
        return powerUpDeck.draw();
    }

    /** Shuffle this match' power up deck*/
    public void shufflePowerUpDeck() {
        powerUpDeck.shuffle();
    }

    /** Get how many cards remains into this match' power up deck
     * @return how many cards remains into this match' power up deck.*/
    public int getSizePowerUpDeck() {
        return powerUpDeck.size();
    }

    /** Draw a card from this match' weapons deck
     * @return the card that is drawn.*/
    public Card drawWeapon() {
        return weaponDeck.draw();
    }

    /*NOT POSSIBLE:
    public void shuffleWeaponDeck() {
        weaponDeck.shuffle();
    }*/

    /** Get how many cards remains into this match' weapons deck
     * @return how many cards remains into this match' weapons deck.*/
    public int getSizeWeaponDeck() {
        return weaponDeck.size();
    }

    /** given an integer from 0 to 3, generates a map based on the order on which they appear on the instructions. Map 3 is the map that is not shown in the instructions
     * @param mapID from 0 to 3
     * @throws MapNotExistsException if mapNumber is not in {0,1,2,3} */
    public void createMap(int mapID)  throws MapNotExistsException {
        gameMap = MapGenerator.generate(mapID);
    }

    public void startMatch() {
        actualTurn = new Turn();
        actualPlayerIndex = playerList.indexOf(firstPlayer);
    }

    /** Method that signal the start of the turn of a player. This method SHOULD be called each time a player starts his turn*/
    public void nextTurn() {
        if (actualTurn.hasFinished()) {
            if (frenzy && (playerList.indexOf(firstPlayer) == actualPlayerIndex)) {
                frenzyType1 = false;
            }
            actualPlayerIndex = (actualPlayerIndex == playerList.size() - 1) ? 0 : actualPlayerIndex + 1 ;
            actualTurn = new Turn();
        }
    }

    public void endTurn() {
        actualTurn.endTurn();
    }

    /** given an integer from 0 to 3, generates a map based on the order on which they appear on the instructions. Map 3 is the map that is not shown in the instructions
     * @param p any player
     * @return a Set of possible moves of the player */
    public Set getPossibleAction(Player p) {
        if (!p.equals(playerList.get(actualPlayerIndex))) {
            return new HashSet<>();
        }
        if (frenzy) {
            if (frenzyType1) {
                return (HashSet<LinkedList<ActionElement>>) actualTurn.getCompositions().get(ActionGroup.FRENZY_TYPE_1);
            } else {
                return (HashSet<LinkedList<ActionElement>>) actualTurn.getCompositions().get(ActionGroup.FRENZY_TYPE_2);
            }
        }
        switch (p.getTotalDamage()) {
            case 3: case 4: case 5:
                return (HashSet<LinkedList<ActionElement>>) actualTurn.getCompositions().get(ActionGroup.LOW_LIFE);
            case 6: case 7: case 8: case 9:
                return (HashSet<LinkedList<ActionElement>>) actualTurn.getCompositions().get(ActionGroup.VERY_LOW_LIFE);
            default:
                return (HashSet<LinkedList<ActionElement>>) actualTurn.getCompositions().get(ActionGroup.NORMAL);
        }
    }

}
