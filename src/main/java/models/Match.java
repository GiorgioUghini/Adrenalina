package models;

import controllers.CardController;
import errors.CheatException;
import models.card.*;
import models.map.*;
import models.player.Player;
import models.turn.*;
import models.turn.ActionType;
import network.Response;
import network.Server;
import network.updates.ChooseMapUpdate;
import network.updates.NextTurnUpdate;

import java.io.Serializable;
import java.util.*;

public class Match {
    private List<Player> playerList;
    private int actualPlayerIndex = 0;
    private PowerUpDeck powerUpDeck;
    private List<Card> thrownPowerUps;
    private WeaponDeck weaponDeck;
    private AmmoDeck ammoDeck;
    private GameMap gameMap;
    private  Map<ActionType,  List<TurnEvent>> currentActions;
    private ActionType currentActionType;
    //private Turn actualTurn;
    private ActionGroup frenzy = null;
    private CardController cardController;
    int round;
    //null -> noFrenzy, Type1, Type2

    public Match(List<Player> players) {
        playerList = new LinkedList<>(players);
        cardController = new CardController();
        powerUpDeck = cardController.getPowerUpDeck();
        thrownPowerUps = new ArrayList<>();
        weaponDeck = cardController.getWeaponDeck();
        ammoDeck = cardController.getAmmoDeck();
        for(Player p : players){
            p.setMatch(this);
        }
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

    /**
     * Activate frenzy from now on
     *
     * @param player the player who is activating frenzy mode.
     */
    public void activateFrenzy(Player player) {
        frenzy = ActionGroup.FRENZY_TYPE_1;
    }

    /**
     * Get the first player on this match
     *
     * @return the first player.
     */
    public Player getFirstPlayer() {
        return playerList.get(0);
    }

    public void setFirstPlayer(Player firstPlayer) {
        playerList.remove(firstPlayer);
        playerList.add(0, firstPlayer);
    }

    /**
     * Add a player into this match
     *
     * @param p the player who wants to join this match.
     */
    public void addPlayer(Player p) {
        playerList.add(p);
    }

    /**
     * Remove a player into this match
     *
     * @param p the player who wants to leave this match.
     */
    public void removePlayer(Player p) {
        playerList.remove(p);
    }

    /**
     * Draw a card from this match' power up deck
     *
     * @return the card that is drawn.
     */
    public Card drawPowerUp() {
        Card card = powerUpDeck.draw();
        if(powerUpDeck.size()==0){
            powerUpDeck = new PowerUpDeck(thrownPowerUps);
            powerUpDeck.shuffle();
            thrownPowerUps = new ArrayList<>();
        }
        return card;
    }

    public void throwPowerUp(PowerUpCard powerUpCard){
        thrownPowerUps.add(powerUpCard);
    }

    /**
     * Shuffle this match' power up deck
     */
    public void shufflePowerUpDeck() {
        powerUpDeck.shuffle();
    }

    /**
     * Get how many cards remains into this match' power up deck
     *
     * @return how many cards remains into this match' power up deck.
     */
    public int getSizePowerUpDeck() {
        return powerUpDeck.size();
    }

    /**
     * Draw a card from this match' weapons deck
     *
     * @return the card that is drawn.
     */
    public Card drawWeapon() {
        return weaponDeck.draw();
    }

    /**
     * Get how many cards remains into this match' weapons deck
     *
     * @return how many cards remains into this match' weapons deck.
     */
    public int getSizeWeaponDeck() {
        return weaponDeck.size();
    }

    /**
     * given an integer from 0 to 3, generates a map based on the order on which they appear on the instructions. Map 3 is the map that is not shown in the instructions
     *
     * @param mapID from 0 to 3
     */
    public void createMap(int mapID) {
        gameMap = MapGenerator.generate(mapID);
        MapGenerator.initCards(ammoDeck, weaponDeck, gameMap);
    }

    public GameMap getMap() {
        return gameMap;
    }

    /**
     * Method that signal the start of the match. This method SHOULD be called once when the match is ready to start.
     */
    public void chooseMapAndStartMatch() {
        actualPlayerIndex = -1;
        ChooseMapUpdate update = new ChooseMapUpdate(playerList.get(0).getName());
        addUpdate(update);
        nextTurn();
    }

    public void addUpdate(Response update) {
        for (Player player : playerList) {
            if (Server.getInstance().getConnection() != null) // IF SOLO PER I TEST!!
                Server.getInstance().getConnection().getConnectionWrapper(Server.getInstance().getLobby().getToken(player)).addUpdate(update);
        }
    }

    /**
     * Method that signal the start of the turn of a player. This method SHOULD be called each time a player starts his turn
     */
    public void nextTurn() {
        if(gameMap!=null){
            refillCards();
        }
        for(Player player : playerList){
            player.onTurnEnded();
        }
        actualPlayerIndex = (actualPlayerIndex == playerList.size() - 1) ? 0 : actualPlayerIndex + 1;
        if ((frenzy != null) && (actualPlayerIndex == 0)) { //actualPlayerIndex == firstPlayerIndex
            frenzy = ActionGroup.FRENZY_TYPE_2;
        }
        Player currentPlayer = playerList.get(actualPlayerIndex);
        TurnType turnType = null;
        if (currentPlayer.hasJustStarted()) {
            turnType = TurnType.START_GAME;
        } else if (currentPlayer.isDead()) {
            turnType = TurnType.RESPAWN;
        } else {
            turnType = TurnType.IN_GAME;
        }
        currentActions = new TurnEngine().getValidActions(turnType, currentPlayer.getLifeState());
        currentActionType = null;
        round = (turnType == TurnType.IN_GAME && currentPlayer.getLifeState() != ActionGroup.FRENZY_TYPE_2) ? 2: 1;
        addUpdate(new NextTurnUpdate(playerList.get(actualPlayerIndex).getName()));
    }

    public void action(ActionType actionType) {
        if(!currentActions.keySet().contains(actionType))
            throw new CheatException();
        if(currentActionType == null){
            currentActionType = actionType;
        }
    }

    public ActionType getCurrentActionType()
    {
        return currentActionType;
    }

    public void turnEvent(TurnEvent turnEvent) {
        if(!currentActions.get(currentActionType).contains(turnEvent))
            throw new CheatException();
        currentActions.get(currentActionType).subList(0, currentActions.get(currentActionType).indexOf(turnEvent) + 1).clear();
        if(currentActions.get(currentActionType).isEmpty()){
            currentActionType = null;
            round = round -1;
            if(round <= 0){
                currentActions = new HashMap<>();
            }
            else{
                Player currentPlayer = playerList.get(actualPlayerIndex);
                TurnType turnType = null;
                if (currentPlayer.hasJustStarted()) {
                    turnType = TurnType.START_GAME;
                } else if (currentPlayer.isDead()) {
                    turnType = TurnType.RESPAWN;
                } else {
                    turnType = TurnType.IN_GAME;
                }
                currentActions = new TurnEngine().getValidActions(turnType, currentPlayer.getLifeState());
            }
        }
    }
    /**
     * returns the set of possible moves (as a list) of the given player.
     *
     * @param p any player
     * @return a Set of possible moves of the player
     */
    public Map<ActionType,  List<TurnEvent>> getPossibleAction(Player p) {
        if (playerList.get(actualPlayerIndex).equals(p)) {
            return currentActions;
        } else {
            return new HashMap<>();
        }
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

    public List<Player> getPlayers() {
        return playerList;
    }

    public Player getPlayerByUsername(String username) {
        return playerList.stream().filter(f -> f.getName().equals(username)).findFirst().orElse(null);
    }

    public Player getCurrentPlayer(){
        return playerList.get(actualPlayerIndex);
    }

    public boolean isDoingAction(){
        return currentActionType!=null;
    }

    private void refillCards(){
        Set<Square> toRefill = gameMap.getSquaresToRefill();
        for(Square s : toRefill){
            if(s.isSpawnPoint()){
                SpawnPoint spawnPoint = (SpawnPoint) s;
                while(spawnPoint.showCards().size()<3){
                    Card weaponCard = weaponDeck.draw();
                    spawnPoint.addCard(weaponCard);
                }
            }else{
                AmmoPoint ammoPoint = (AmmoPoint) s;
                Card ammoCard = ammoDeck.draw();
                ammoPoint.addCard(ammoCard);
            }
        }
    }

}
