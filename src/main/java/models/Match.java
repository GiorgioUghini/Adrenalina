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
    private ActionGroup frenzy = null;
    //null -> noFrenzy, Type1, Type2

    public Match(){
        playerList = new ArrayList<>();
        actualTurn  = new Turn();
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
        Match match = (Match) o;
        // field comparison
        return playerList.equals(match.playerList);
    }

    /** Activate frenzy from now on
     * @param player the player who is activating frenzy mode.*/
    public void activateFrenzy(Player player) {
        frenzy = ActionGroup.FRENZY_TYPE_1;
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
     * @param mapID from 0 to 3 */
    public void createMap(int mapID)  {
        gameMap = MapGenerator.generate(mapID);
    }

    /** Method that signal the start of the match. This method SHOULD be called once when the match is ready to start.*/
    public void startMatch() {
        actualPlayerIndex = playerList.indexOf(firstPlayer);
    }

    /** Method that signal the start of the turn of a player. This method SHOULD be called each time a player starts his turn*/
    public void nextTurn() {
        if (actualTurn.hasFinished()) {
            actualPlayerIndex = (actualPlayerIndex == playerList.size() - 1) ? 0 : actualPlayerIndex + 1 ;
            if ((frenzy != null) && (playerList.indexOf(firstPlayer) == actualPlayerIndex)) {
                frenzy = ActionGroup.FRENZY_TYPE_2;
            }
            actualTurn = new Turn();
        }
    }

    /** Method that signal the end of the turn.*/
    public void endTurn() {
        actualTurn.endTurn();
    }

    /** returns the set of possible moves (as a list) of the given player.
     * @param p any player
     * @return a Set of possible moves of the player */
    public Set getPossibleAction(Player p) {
        if (!p.equals(playerList.get(actualPlayerIndex))) {
            return new HashSet<>();
        }
        if (frenzy != null) {
            return (HashSet<LinkedList<ActionElement>>) actualTurn.getCompositions().get(frenzy);
        }

        return (HashSet<LinkedList<ActionElement>>) actualTurn.getCompositions().get(p.getLifeState());

    }

    /** given a player object and the list of moves he wants to do, returns true if he's allowed to do this moves
     * @param p any player
     * @param actionList list of action the players wants to do
     * @return true if he's allowed, false otherwise */
    public boolean confirmActions(Player p, List actionList) {
        Set possibleActions = this.getPossibleAction(p);
        for (Object o : possibleActions) {
            if (isOrderedSubset(actionList, (List) o)) {
                return true;
            }
        }
        return false;
    }

    private boolean isOrderedSubset(List biggerList, List smallerList) {
        int indexSmaller = 0, indexBigger = 0;
        while (indexSmaller < smallerList.size() && indexBigger < biggerList.size()) {
            if (smallerList.get(indexSmaller) == biggerList.get(indexBigger)) {
                indexBigger += 1;
            }
            indexSmaller += 1;
        }

        return (indexBigger == biggerList.size());
    }

}
