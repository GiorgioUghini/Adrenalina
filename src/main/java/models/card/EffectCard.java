package models.card;

import errors.WeaponCardException;
import models.map.GameMap;
import models.map.RoomColor;
import models.map.Square;
import models.player.Player;

import java.util.*;

public abstract class EffectCard extends Card {
    public String name;
    public String image;
    protected boolean activated = false;
    protected Action activeAction;
    protected Map<String, Player> selectedPlayers;
    protected Map<String, Square> selectedSquares;
    protected Map<String, RoomColor> selectedRooms;
    protected GameMap gameMap;
    protected Player me;
    public EffectCard() {
        init();
    }

    protected void init() {
        activeAction = null;
        selectedPlayers = new HashMap<>();
        selectedSquares = new HashMap<>();
        selectedRooms = new HashMap<>();
        activated = false;
    }

    public abstract void activate(Player me);

    /**
     * @return an object containing a set of taggable that can be selected, boolean that indicates if the select is mandatory
     * and a TargetType that specifies the type of the select
     * @throws WeaponCardException if the current action is not a select
     */
    public Selectable getSelectable() {
        checkActiveAction(ActionType.SELECT);
        Selectable out = new Selectable(activeAction.select.optional);
        Set<Taggable> taggables = new SelectorEngine(gameMap, me, activeAction.select, selectedPlayers, selectedSquares).getSelectable();
        out.add(taggables, activeAction.select.type);
        return out;
    }

    /**
     * @param taggable the taggable element that will be tagged with this select
     * @throws WeaponCardException if the current action is not a select
     * @throws WeaponCardException if you are trying to tag a player but the tag was for a square or a room
     * @throws ClassCastException  if the select type differs from the class of the taggable object
     */
    public void select(Taggable taggable) {
        checkActiveAction(ActionType.SELECT);
        if(taggable == null) return;
        switch (activeAction.select.type) {
            case PLAYER:
                Player taggedPlayer = (Player) taggable;
                if(me.getMatch()!=null){
                    taggedPlayer = me.getMatch().getPlayerByUsername(taggedPlayer.getName());
                }
                selectedPlayers.put(activeAction.select.id, taggedPlayer);
                break;
            case SQUARE:
                Square taggedSquare = (Square) taggable;
                taggedSquare = me.getGameMap().getSquareById(taggedSquare.getId());
                selectedSquares.put(activeAction.select.id, taggedSquare);
                break;
            case ROOM:
                selectedRooms.put(activeAction.select.id, (RoomColor) taggable);
                break;
        }
    }

    public abstract Action playNextAction();

    /**
     * @return a map with the players as keys and the damages to give to each one as the value
     * @throws WeaponCardException if the current action is not a damage
     */
    public Map<Player, Integer> getPlayersToDamage() {
        checkActiveAction(ActionType.DAMAGE);
        return new DamageEngine(activeAction.damage, selectedPlayers, selectedSquares, selectedRooms, gameMap, me).getDamages();
    }

    /**
     * @return a map with the players as keys and the marks to give to each one as the value
     * @throws WeaponCardException if the current action is not a mark
     */
    public Map<Player, Integer> getPlayersToMark() {
        checkActiveAction(ActionType.MARK);
        return new DamageEngine(activeAction.mark, selectedPlayers, selectedSquares, selectedRooms, gameMap, me).getDamages();
    }

    public Map<Player, Square> getPlayersMoves() {
        checkActiveAction(ActionType.MOVE);
        return new MoveEngine(activeAction.move, selectedPlayers, selectedSquares, gameMap, me).getNewPositions();
    }

    /**
     * Must be called at the end of the card usage. resets the card to its initial status (but unloaded)
     */
    public void reset() {
        init();
        this.gameMap = null;
    }

    private void checkActiveAction(ActionType expected) {
        ActionType actual = activeAction.type;
        if (!actual.equals(expected))
            throw new WeaponCardException("Expected active action " + expected + " differs from found active action " + actual);
    }
}