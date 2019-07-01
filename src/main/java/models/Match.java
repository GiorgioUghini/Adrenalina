package models;

import controllers.CardController;
import errors.CheatException;
import models.card.*;
import models.map.*;
import models.player.DeathManager;
import models.player.Player;
import models.turn.ActionType;
import models.turn.*;
import network.ConnectionWrapper;
import network.Response;
import network.Server;
import network.updates.ChooseMapUpdate;
import network.updates.EndMatchUpdate;
import network.updates.NextTurnUpdate;

import java.util.*;

public class Match {
    private List<Player> playerList;
    private int actualPlayerIndex = 0;
    private int tmpActualPlayerIndex = -1;
    private int endMatchPlayerIndex = -1;
    private PowerUpDeck powerUpDeck;
    private List<Card> thrownAmmos;
    private List<Card> thrownPowerUps;
    private WeaponDeck weaponDeck;
    private AmmoDeck ammoDeck;
    private GameMap gameMap;
    private int mapIndex;
    private DeathManager deathManager;
    private Config config;
    private Map<ActionType, List<TurnEvent>> currentActions;
    private ActionType currentActionType;
    private boolean frenzy = false;
    private Timer turnTimer;
    private TimerTask turnTimerTask;
    private CardController cardController;
    int round;

    //null -> noFrenzy, Type1, Type2
    public Match(List<Player> players) {
        this(players, new Config(7000, 60000));
    }

    public Match(List<Player> players, Config config) {
        this.config = config;
        deathManager = new DeathManager();
        playerList = new LinkedList<>(players);
        cardController = new CardController();
        powerUpDeck = cardController.getPowerUpDeck();
        thrownPowerUps = new ArrayList<>();
        thrownAmmos = new ArrayList<>();
        weaponDeck = cardController.getWeaponDeck();
        ammoDeck = cardController.getAmmoDeck();
        powerUpDeck.shuffle();
        weaponDeck.shuffle();
        ammoDeck.shuffle();
        int i = 0;
        for (Player p : players) {
            p.setMatch(this);
            p.setDeathManager(deathManager);
            p.setPlayerColor(getColor(i++));
        }
    }

    private String getColor(int index) {
        switch (index) {
            case 0:
                return "GREEN";
            case 1:
                return "BLUE";
            case 2:
                return "PURPLE";
            case 3:
                return "WHITE";
            case 4:
                return "YELLOW";
            default:
                throw new NullPointerException("This color does not exist");
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
     */
    public void activateFrenzy() {
        for(int i = actualPlayerIndex; i < playerList.size(); i++){
            playerList.get(i).setLifeState(ActionGroup.FRENZY_TYPE_1);
        }
        for(int i = actualPlayerIndex-1; i > 0; i--){
            playerList.get(i).setLifeState(ActionGroup.FRENZY_TYPE_2);
        }
        playerList.get(0).setLifeState(ActionGroup.FRENZY_TYPE_2);
        frenzy = true;
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
        if (powerUpDeck.size() == 0) {
            powerUpDeck = new PowerUpDeck(thrownPowerUps);
            powerUpDeck.shuffle();
            thrownPowerUps = new ArrayList<>();
        }
        return card;
    }

    public void throwPowerUp(PowerUpCard powerUpCard) {
        thrownPowerUps.add(powerUpCard);
    }

    public void throwAmmo(AmmoCard ammoCard) {
        thrownAmmos.add(ammoCard);
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

        if (Server.getInstance().isDebug()) {
            for (Player player : playerList) {
                List<WeaponCard> playerCards = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    playerCards.add((WeaponCard) weaponDeck.draw());
                }
                player.setWeaponList(playerCards);
            }
        }
    }

    public void addUpdate(Response update) {
        for (Player player : playerList) {
            if (Server.getInstance().getConnection() != null && player.isOnline())
                Server.getInstance().getConnection().getConnectionWrapper(Server.getInstance().getLobby().getToken(player)).addUpdate(update);
        }
    }

    /**
     * Method that signal the start of the turn of a player. This method SHOULD be called each time a player starts his turn
     */
    public void nextTurn() {
        stopTurnTimer();
        if (gameMap != null) {
            refillCards();
        }
        for (Player player : playerList) {
            player.onTurnEnded();
        }

        playerList.stream().filter(Player::isDead).forEach(p->gameMap.removePlayer(p));

        Player firstDeadPlayer = playerList.stream().filter(Player::isDead).findFirst().orElse(null);

        if (tmpActualPlayerIndex >= 0) {
            actualPlayerIndex = tmpActualPlayerIndex;
            tmpActualPlayerIndex = -1;
        }

        if (firstDeadPlayer != null) {
            tmpActualPlayerIndex = actualPlayerIndex;
            actualPlayerIndex = playerList.indexOf(firstDeadPlayer);
        } else {
            if(frenzy && endMatchPlayerIndex < 0){
                endMatchPlayerIndex = actualPlayerIndex + 1;
            }
            else if(frenzy && endMatchPlayerIndex == actualPlayerIndex){
                addUpdate(new EndMatchUpdate(getTotalPoints()));
                return;
            }
            actualPlayerIndex = (actualPlayerIndex == playerList.size() - 1) ? 0 : actualPlayerIndex + 1;
            while (!playerList.get(actualPlayerIndex).isOnline()) {
                actualPlayerIndex = (actualPlayerIndex == playerList.size() - 1) ? 0 : actualPlayerIndex + 1;
            }
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
        round = (turnType == TurnType.IN_GAME && currentPlayer.getLifeState() != ActionGroup.FRENZY_TYPE_2) ? 2 : 1;
        addUpdate(new NextTurnUpdate(playerList.get(actualPlayerIndex).getName()));
        startTurnTimer();
    }

    public void action(ActionType actionType) {
        if (!currentActions.keySet().contains(actionType))
            throw new CheatException();
        if (currentActionType == null) {
            currentActionType = actionType;
        }
    }

    public ActionType getCurrentActionType() {
        return currentActionType;
    }

    public void turnEvent(TurnEvent turnEvent) {
        if (!currentActions.get(currentActionType).contains(turnEvent))
            throw new CheatException("Current action type: " + currentActionType + "; turn event: " + turnEvent);
        currentActions.get(currentActionType).subList(0, currentActions.get(currentActionType).indexOf(turnEvent) + 1).clear();
        if (currentActions.get(currentActionType).isEmpty()) {
            currentActionType = null;
            round = round - 1;
            if (round <= 0) {
                currentActions = new HashMap<>();
            } else {
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
    public Map<ActionType, List<TurnEvent>> getPossibleAction(Player p) {
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

    public Player getCurrentPlayer() {
        return playerList.get(actualPlayerIndex);
    }

    public boolean isDoingAction() {
        return currentActionType != null;
    }

    private void refillCards() {
        Set<Square> toRefill = gameMap.getSquaresToRefill();
        for (Square s : toRefill) {
            if (s.isSpawnPoint()) {
                SpawnPoint spawnPoint = (SpawnPoint) s;
                while (spawnPoint.showCards().size() < 3) {
                    Card weaponCard = weaponDeck.draw();
                    spawnPoint.addCard(weaponCard);
                }
            } else {
                AmmoPoint ammoPoint = (AmmoPoint) s;
                if(ammoDeck.size() == 0){
                    ammoDeck = new AmmoDeck(thrownAmmos);
                    thrownAmmos = new ArrayList<>();
                    ammoDeck.shuffle();
                }
                Card ammoCard = ammoDeck.draw();
                ammoPoint.addCard(ammoCard);
            }
        }
    }

    public void addPartialPointsCount(Player player) {
        deathManager.addPartialPointsCount(player, player.countPoints());
    }

    public int getDeathCount(Player player) {
        return deathManager.getDeathCount(player);
    }

    public Map<Player, Integer> getTotalPoints() {
        return deathManager.getTotalPoints(playerList);
    }

    private void stopTurnTimer() {
        if (turnTimerTask != null) {
            turnTimerTask.cancel();
        }
        if (turnTimer != null) {
            turnTimer.cancel();
            turnTimer.purge();
        }
    }

    private void startTurnTimer() {
        turnTimer = new Timer();
        turnTimerTask = new TimerTask() {
            @Override
            public void run() {
                Player currentPlayer = getCurrentPlayer();
                if (Server.getInstance() != null) {
                    String token = Server.getInstance().getLobby().getToken(currentPlayer);
                    ConnectionWrapper connectionWrapper = Server.getInstance().getConnection().getConnectionWrapper(token);
                    connectionWrapper.stop();
                }
            }
        };
        turnTimer.schedule(turnTimerTask, config.getTurnTimeout());
    }

    public int getSkullCount(){
        return deathManager.getSkullCount(playerList);
    }

    public int getMapIndex() {
        return mapIndex;
    }

    public void setMapIndex(int mapIndex) {
        this.mapIndex = mapIndex;
    }
}
