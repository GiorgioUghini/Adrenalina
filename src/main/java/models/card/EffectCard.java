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
    private boolean loaded;
    public List<Effect> effects;
    private List<Effect> activatedEffects;
    private boolean activated = false;
    private Effect activeEffect;
    private Action activeAction;
    private Map<String, Player> selectedPlayers;
    private Map<String, Square> selectedSquares;
    private Map<String, RoomColor> selectedRooms;

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

    public Ammo getPrice(){
        return this.price;
    }

    public boolean isLoaded(){
        return loaded;
    }

    public void load(Ammo ammo){
        if(!hasEnoughAmmo(ammo, price)) throw new WeaponCardException("Not enough ammo to reload");
        pay(price, ammo);
        loaded = true;
    }

    public Selectable getSelectablePlayers(GameMap gameMap, Player me){
        Selectable out = new Selectable(activeAction.select.optional);
        Set<Player> players = new SelectorEngine(gameMap, me, activeAction.select, selectedPlayers, selectedSquares).getSelectablePlayers();
        out.addPlayers(players);
        return out;
    }

    public Selectable getSelectableSquares(GameMap gameMap, Player me){
        Selectable out = new Selectable(activeAction.select.optional);
        Set<Square> squares = new SelectorEngine(gameMap, me, activeAction.select, selectedPlayers, selectedSquares).getSelectableSquares();
        out.addSquares(squares);
        return out;
    }

    public Selectable getSelectableRooms(GameMap gameMap, Player me){
        Selectable out = new Selectable(activeAction.select.optional);
        Set<RoomColor> rooms = new SelectorEngine(gameMap, me, activeAction.select, selectedPlayers, selectedSquares).getSelectableRooms();
        out.addRooms(rooms);
        return out;
    }

    public void selectPlayer(Player player){
        checkIfCanTag();
        checkTargetType(TargetType.PLAYER);
        selectedPlayers.put(activeAction.select.id, player);
    }

    public void selectSquare(Square square){
        checkIfCanTag();
        checkTargetType(TargetType.SQUARE);
        selectedSquares.put(activeAction.select.id, square);
    }

    public void selectRoom(RoomColor color){
        checkIfCanTag();
        checkTargetType(TargetType.ROOM);
        selectedRooms.put(activeAction.select.id, color);
    }

    private void checkTargetType(TargetType expected){
        TargetType actual = activeAction.select.type;
        if(!actual.equals(expected)) throw new WeaponCardException("Cannot tag a " + expected + " if the given target type is " + actual);
    }

    private void checkIfCanTag(){
        if(!activeAction.type.equals(ActionType.SELECT)) throw new WeaponCardException("You cannot tag if the action is not a select");
    }

    /** If the card is loaded, activate it */
    public void activate(){
        if(!isLoaded()) throw new WeaponCardException("The weapon is not loaded, cannot activate");
        this.activated = true;
        this.loaded = false;
    }

    /** Get all effects, with a TRUE flag on the ones that can be activated the card must have been activated
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

    public void playEffect(Effect effect, Ammo ammo){
        if(!hasEnoughAmmo(ammo, effect.price))throw new WeaponCardException("Not enough ammo to activate this effect");
        pay(effect.price, ammo);
        activatedEffects.add(effect);
        activeEffect = effect;
    }

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

    public Map<Player, Integer> getPlayersToDamage(GameMap gameMap, Player me){
        checkActiveAction(ActionType.DAMAGE);
        return new DamageEngine(activeAction.damage, selectedPlayers, selectedSquares, selectedRooms, gameMap, me).getDamages();
    }

    public Map<Player, Integer> getPlayersToMark(GameMap gameMap, Player me){
        checkActiveAction(ActionType.MARK);
        return new DamageEngine(activeAction.damage, selectedPlayers, selectedSquares, selectedRooms, gameMap, me).getDamages();
    }

    public void reset(){
        init();
    }

    /** get all effects and set the 'activable' flag to FALSE */
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

    private int getLastOrderId(){
        if(activatedEffects.isEmpty()) return -1;
        return activatedEffects.get(activatedEffects.size()-1).orderId;
    }

    private void pay(Ammo price, Ammo ammo){
        ammo.blue -= price.blue;
        ammo.red -= price.red;
        ammo.yellow -= price.yellow;
    }
}