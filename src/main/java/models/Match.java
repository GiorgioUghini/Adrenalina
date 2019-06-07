package models;

import controllers.CardController;
import errors.PlayerNotOnMapException;
import javafx.application.Platform;
import models.card.AmmoDeck;
import models.card.Card;
import models.card.PowerUpDeck;
import models.card.WeaponDeck;
import models.map.GameMap;
import models.map.MapGenerator;
import models.player.Player;
import models.turn.*;
import network.Response;
import network.Server;
import network.updates.ChooseMapUpdate;
import network.updates.StartGameUpdate;

import java.util.*;
import java.util.stream.Collectors;

public class Match {
    private List<Player> playerList;
    private int actualPlayerIndex = 0;
    private PowerUpDeck powerUpDeck;
    private WeaponDeck weaponDeck;
    private AmmoDeck ammoDeck;
    private GameMap gameMap;
    //private Turn actualTurn;
    private List<TurnEngine> turnEngines;
    private boolean turnActive = false;
    private ActionGroup frenzy = null;
    private CardController cardController;
    //null -> noFrenzy, Type1, Type2

    public Match( List<Player> players ){
        playerList = new LinkedList<>(players);
        cardController = new CardController();
        turnEngines = new ArrayList<>();
        powerUpDeck = cardController.getPowerUpDeck();
        weaponDeck = cardController.getWeaponDeck();
        ammoDeck = cardController.getAmmoDeck();
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

    @Override
    public int hashCode() {
        return Objects.hash(playerList);
    }

    public int getPlayersNumber() {
        return playerList.size();
    }

    /** Activate frenzy from now on
     * @param player the player who is activating frenzy mode.*/
    public void activateFrenzy(Player player) {
        frenzy = ActionGroup.FRENZY_TYPE_1;
    }

    /** Get the first player on this match
     * @return the first player.*/
    public Player getFirstPlayer() {
        return playerList.get(0);
    }

    public void setFirstPlayer(Player firstPlayer) {
        playerList.remove(firstPlayer);
        playerList.add(0, firstPlayer);
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

    /** Get how many cards remains into this match' weapons deck
     * @return how many cards remains into this match' weapons deck.*/
    public int getSizeWeaponDeck() {
        return weaponDeck.size();
    }

    /** given an integer from 0 to 3, generates a map based on the order on which they appear on the instructions. Map 3 is the map that is not shown in the instructions
     * @param mapID from 0 to 3 */
    public void createMap(int mapID)  {
        gameMap = MapGenerator.generate(mapID);
        MapGenerator.initCards(ammoDeck, weaponDeck, gameMap);
    }

    public GameMap getMap(){
        return gameMap;
    }

    /** Method that signal the start of the match. This method SHOULD be called once when the match is ready to start.*/
    public void chooseMapAndStartMatch() {
        actualPlayerIndex = 0;
        //actualTurn = new Turn();
        turnActive = true;
        ChooseMapUpdate update = new ChooseMapUpdate(playerList.get(0).getName());
        addUpdate(update);
    }

    public void addUpdate(Response update){
        for(Player player : playerList){
            if(Server.getInstance().getConnection() != null) // IF SOLO PER I TEST!!
                Server.getInstance().getConnection().getConnectionWrapper(Server.getInstance().getLobby().getToken(player)).addUpdate(update);
        }
    }

    /** Method that signal the start of the turn of a player. This method SHOULD be called each time a player starts his turn*/
    public void nextTurn() {
        if (!turnActive) {
            actualPlayerIndex = (actualPlayerIndex == playerList.size() - 1) ? 0 : actualPlayerIndex + 1 ;
            if ((frenzy != null) && (actualPlayerIndex == 0)) { //actualPlayerIndex == firstPlayerIndex
                frenzy = ActionGroup.FRENZY_TYPE_2;
            }
            turnActive = true;
        }
    }

    /** Method that signal the end of the turn.*/
    public void endTurn() {
        turnActive = false;
    }

    /** returns the set of possible moves (as a list) of the given player.
     * @param p any player
     * @return a Set of possible moves of the player */
    public Set getPossibleAction(Player p) {

        /*

        Player currentPlayer = playerList.get(actualPlayerIndex);
        TurnType turnType = null;
        if(p.hasJustStarted()){
            turnType = TurnType.START_GAME;
        }
        else if(p.isDead()){
            turnType = TurnType.RESPAWN;
        }
        else{
            turnType = TurnType.IN_GAME;
        }

        turnEngines.add(new TurnEngine(turnType, p.getLifeState()));
        if(frenzy != ActionGroup.FRENZY_TYPE_2){
            turnEngines.add(new TurnEngine(turnType, p.getLifeState()));
        }

        TurnEngine engine = null;
        while((engine = turnEngines.stream().findFirst().orElse(null)) != null){

        }

        for(TurnEngine engine = turnEngines.stream().findFirst().orElse(null); engine != null; )

        if ((!p.equals(playerList.get(actualPlayerIndex))) || actualTurn == null) {
            return new HashSet<>();
        }


        if (frenzy != null) {
            return (HashSet) actualTurn.getCompositions().get(frenzy);
        }

        return (HashSet) actualTurn.getCompositions().get(p.getLifeState());
*/
        return new HashSet<>();
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
        int indexSmaller = 0;
        int indexBigger = 0;
        while (indexSmaller < smallerList.size() && indexBigger < biggerList.size()) {
            if (smallerList.get(indexSmaller) == biggerList.get(indexBigger)) {
                indexBigger += 1;
            }
            indexSmaller += 1;
        }

        return (indexBigger == biggerList.size());
    }

    public List<Player> getPlayers(){
        return playerList;
    }

    public Player getPlayerByUsername(String username){
        return playerList.stream().filter(f -> f.getName().equals(username)).findFirst().orElse(null);
    }

}
