package views;

import models.card.LegitEffects;
import models.card.Selectable;
import models.map.GameMap;
import models.player.Player;
import models.turn.ActionType;
import models.turn.TurnEvent;

import java.util.List;
import java.util.Map;

public interface GameView extends View {

    /**
     * Asks the server for the possible actions the user can do
     */
    void getValidActions();

    /**
     * Handles reconnection
     */
    void reconnect();

    /**
     * Signals that a turn has begun
     *
     * @param name the name of the player that will play the turn
     */
    void startTurn(String name);

    /**
     * Updates the map of the game
     *
     * @param map the updated map received from the server
     */
    void updateMapView(GameMap map);

    /**
     * Updates your player
     *
     * @param newPlayer the new version of your player just received from the server
     */
    void updatePlayerView(Player newPlayer);

    /**
     * Updates the possible actions you player can do (eg run, shoot...)
     *
     * @param actions the map containing the actions
     */
    void updateActions(Map<ActionType, List<TurnEvent>> actions);

    /**
     * Update when a player has been damaged
     *
     * @param damagedPlayer the damaged player
     */
    void onDamage(Player damagedPlayer);

    /**
     * Update when a player receives a mark
     *
     * @param markedPlayer the marked player
     */
    void onMark(Player markedPlayer);

    /**
     * Update the points of all players
     *
     * @param map a map containing for each player its points
     */
    void updatePoints(Map<Player, Integer> map);

    /**
     * When playing a weapons, choose the effect you want to play
     *
     * @param legitEffects an object from which you can get the effects the user can actually play
     */
    void effectChoosingDialog(LegitEffects legitEffects);

    /**
     * When a weapon/powerup requires a select this methods lets the player select
     *
     * @param selectable an object containing everything that can be selected
     */
    void selectTag(Selectable selectable);

    /**
     * Asks the server for the weapon effects
     */
    void continueWeapon();

    /**
     * If there is nothing else to with the weapon this method will end it
     */
    void onEndWeapon();

    /**
     * When the match end, print the result
     *
     * @param winners  a list containing all the players who won
     * @param pointers a map containing for each player its total score
     */
    void onEndMatch(List<Player> winners, Map<Player, Integer> pointers);

}
