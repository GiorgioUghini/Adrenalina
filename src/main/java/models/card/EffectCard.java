package models.card;

import errors.NoActiveEffectException;
import errors.WeaponCardException;
import models.map.GameMap;
import models.map.RoomColor;
import models.map.Square;
import models.player.Ammo;
import models.player.Player;

import java.util.*;

public class EffectCard extends Card  {
    public String name;
    public Boolean exclusive;
    public Ammo price;
    public String image;
    private boolean loaded;
    public List<Effect> effects;
    private List<Effect> activatedEffects;
    private boolean activated = false;
    private Effect activeEffect;
    private Action activeAction;
    private Map<String, Player> selectedPlayers;
    private Map<String, Square> selectedSquares;
    private Map<String, RoomColor> selectedRooms;
    private GameMap gameMap;
    private Player me;

    public EffectCard(){
        init();
        loaded = true;
    }

    private void init(){
        this.activatedEffects = new ArrayList<>();
        activeEffect = null;
        activeAction = null;
        selectedPlayers = new HashMap<>();
        selectedSquares = new HashMap<>();
        selectedRooms = new HashMap<>();
    }

    /** @return the price to reload the ammo, as indicated in the top-left corner of the card */
    public Ammo getPrice(){
        return this.price;
    }

    public boolean isLoaded(){
        return loaded;
    }

    /** Removes the necessary ammos from the "ammo" param to reload the card. You can check if you have enough ammo to reload by calling canReload(ammo) method
     * @param ammo the ammo the player has, the reload cost will be deducted from here. ATTENTION: this function modifies the param
     * @throws WeaponCardException if the given ammo is not enough to reload. */
    public void load(Ammo ammo){
        if(!hasEnoughAmmo(ammo, price)) throw new WeaponCardException("Not enough ammo to reload");
        pay(price, ammo);
        loaded = true;
    }

    /** @param ammo your ammo availability
     *  @return true if the provided ammos are enough to reload. */
    public boolean canReload(Ammo ammo){
        return hasEnoughAmmo(ammo, price);
    }

    /** If the card is loaded, activate it
     * @param gameMap
     * @param me the player who is going to use the card
     * @throws WeaponCardException if the weapon is not loaded */
    public void activate(GameMap gameMap, Player me){
        if(!isLoaded()) throw new WeaponCardException("The weapon is not loaded, cannot activate");
        this.activated = true;
        this.loaded = false;
        this.gameMap = gameMap;
        this.me = me;
    }

    /** @return an object containing a set of players that can be selected and boolean that indicates if the select is mandatory
     * @throws WeaponCardException if the current action is not a select */
    public Selectable getSelectablePlayers(){
        checkActiveAction(ActionType.SELECT);
        Selectable out = new Selectable(activeAction.select.optional);
        Set<Player> players = new SelectorEngine(gameMap, me, activeAction.select, selectedPlayers, selectedSquares).getSelectablePlayers();
        out.addPlayers(players);
        return out;
    }

    /** @return an object containing a set of squares that can be selected and boolean that indicates if the select is mandatory
     * @throws WeaponCardException if the current action is not a select */
    public Selectable getSelectableSquares(){
        checkActiveAction(ActionType.SELECT);
        Selectable out = new Selectable(activeAction.select.optional);
        Set<Square> squares = new SelectorEngine(gameMap, me, activeAction.select, selectedPlayers, selectedSquares).getSelectableSquares();
        out.addSquares(squares);
        return out;
    }

    /** @return an object containing a set of rooms that can be selected and boolean that indicates if the select is mandatory
     * @throws WeaponCardException if the current action is not a select */
    public Selectable getSelectableRooms(){
        checkActiveAction(ActionType.SELECT);
        Selectable out = new Selectable(activeAction.select.optional);
        Set<RoomColor> rooms = new SelectorEngine(gameMap, me, activeAction.select, selectedPlayers, selectedSquares).getSelectableRooms();
        out.addRooms(rooms);
        return out;
    }

    /** @param player the player that will be tagged with this select
     *  @throws WeaponCardException if the current action is not a select
     *  @throws WeaponCardException if you are trying to tag a player but the tag was for a square or a room */
    public void selectPlayer(Player player){
        checkActiveAction(ActionType.SELECT);
        checkTargetType(TargetType.PLAYER);
        selectedPlayers.put(activeAction.select.id, player);
    }

    /** @param square the square that will be tagged with this select
     *  @throws WeaponCardException if the current action is not a select
     *  @throws WeaponCardException if you are trying to tag a square but the tag was for a player or a room */
    public void selectSquare(Square square){
        checkActiveAction(ActionType.SELECT);
        checkTargetType(TargetType.SQUARE);
        selectedSquares.put(activeAction.select.id, square);
    }

    /** @param color the room that will be tagged with this select, identified by its color
     *  @throws WeaponCardException if the current action is not a select
     *  @throws WeaponCardException if you are trying to tag a room but the tag was for a player or a square */
    public void selectRoom(RoomColor color){
        checkActiveAction(ActionType.SELECT);
        checkTargetType(TargetType.ROOM);
        selectedRooms.put(activeAction.select.id, color);
    }

    private void checkTargetType(TargetType expected){
        TargetType actual = activeAction.select.type;
        if(!actual.equals(expected)) throw new WeaponCardException("Cannot tag a " + expected + " if the given target type is " + actual);
    }

    /** Get all effects, with a TRUE flag on the ones that can be used. The card must have been activated
     * to get any activable effect
     * @param ammo your ammo
     * @return all the effects with the flag set to false if the card has not been activated */
    public LegitEffects getEffects(Ammo ammo){
        if(!activated) return noActivableEffects();
        LegitEffects out = getAffordableEffects(ammo);
        if(exclusive){
            if(activatedEffects.isEmpty()){
                return out;
            }else{
                return noActivableEffects();
            }
        }else{
            for(Effect e : activatedEffects){
                out.addEffect(e, false);
            }
            out = out.logicalAnd(getByOrderId(getLastOrderId()));
            return out;
        }
    }

    /** Activates the chosen effect, paying it with the ammos given as param
     * @param effect
     * @param ammo ATTENTION: this param will be modified if the effect activation was successfull. The effect price will be deducted from it
     * @throws WeaponCardException if you do not have enough ammo to activate the effect */
    public void playEffect(Effect effect, Ammo ammo){
        if(!hasEnoughAmmo(ammo, effect.price))throw new WeaponCardException("Not enough ammo to activate this effect");
        pay(effect.price, ammo);
        activatedEffects.add(effect);
        activeEffect = effect;
    }

    private int getLastOrderId(){
        if(activatedEffects.isEmpty()) return -1;
        return activatedEffects.get(activatedEffects.size()-1).orderId;
    }

    /** activates the next action inside the effect. Must be called for the first action too. It also cancels the active effect and action if
     * there are no more actions. In this case, on the last call it returned null
     * @return the action that has just been activated, or null if there are no more actions
     * @throws NoActiveEffectException if no effect is active at the moment
     * */
    public Action playNextAction(){
        if(activeEffect == null) throw new NoActiveEffectException();
        if(activeAction == null) {
            activeAction = activeEffect.actions.get(0);
            return activeAction;
        }
        int lastActionIndex = activeEffect.actions.indexOf(activeAction);
        if(lastActionIndex == activeEffect.actions.size()-1){
            activeAction = null;
            activeEffect = null;
            return null;
        }
        activeAction = activeEffect.actions.get(lastActionIndex + 1);
        return activeAction;
    }

    /** @return a map with the players as keys and the damages to give to each one as the value
     * @throws WeaponCardException if the current action is not a damage */
    public Map<Player, Integer> getPlayersToDamage(){
        checkActiveAction(ActionType.DAMAGE);
        return new DamageEngine(activeAction.damage, selectedPlayers, selectedSquares, selectedRooms, gameMap, me).getDamages();
    }

    /** @return a map with the players as keys and the marks to give to each one as the value
     * @throws WeaponCardException if the current action is not a mark */
    public Map<Player, Integer> getPlayersToMark(){
        checkActiveAction(ActionType.MARK);
        return new DamageEngine(activeAction.damage, selectedPlayers, selectedSquares, selectedRooms, gameMap, me).getDamages();
    }

    public Map<Player, Square> getPlayersMoves(){
        checkActiveAction(ActionType.MOVE);
        return new MoveEngine(activeAction.move, selectedPlayers, selectedSquares, gameMap, me).getNewPositions();
    }

    /** Must be called at the end of the card usage. resets the card to its initial status (but unloaded) */
    public void reset(){
        init();
        this.gameMap = null;
        this.me = null;
    }

    /** @return All effects and set the 'activable' flag to FALSE */
    private LegitEffects noActivableEffects(){
        LegitEffects out = new LegitEffects();
        for(Effect e : effects){
            out.addEffect(e, false);
        }
        return out;
    }

    /** Loops all effects and returns them, those that can be bought will have a TRUE flag */
    private LegitEffects getAffordableEffects(Ammo ammo){
        LegitEffects out = new LegitEffects();
        for(Effect e : effects){
            boolean affordable = hasEnoughAmmo(ammo, e.price);
            out.addEffect(e, affordable);
        }
        return out;
    }

    /** Check if you have enough ammo to activate this card */
    private boolean hasEnoughAmmo(Ammo ammo, Ammo price){
        return (
            ammo.red >= price.red &&
            ammo.yellow >= price.yellow &&
            ammo.blue >= price.blue
        );
    }

    /** Get all effects with orderId equal to the one given as param or orderId=-1 */
    private LegitEffects getByOrderId(int orderId){
        Map<Effect, Boolean> map = new HashMap<>();
        boolean baseEffectActivated = true;
        if(orderId == 0 && !exclusive){
            if(!activatedEffects.contains(effects.get(0))){
                baseEffectActivated = false;
            }
        }
        final boolean tmp = baseEffectActivated;
        effects.forEach(e -> {
            if(tmp){
                boolean activable = (e.orderId==orderId || e.orderId==orderId+1 || e.orderId == -1);
                map.put(e, activable);
            }else{
                map.put(e, (e.orderId<1));
            }
        });
        return new LegitEffects(map);
    }

    private void checkActiveAction(ActionType expected){
        ActionType actual = activeAction.type;
        if(!actual.equals(expected)) throw new WeaponCardException("Expected active action " + expected + " differs from found active action " + actual);
    }

    private void pay(Ammo price, Ammo ammo){
        ammo.blue -= price.blue;
        ammo.red -= price.red;
        ammo.yellow -= price.yellow;
    }
}