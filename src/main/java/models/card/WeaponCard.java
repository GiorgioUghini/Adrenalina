package models.card;

import errors.NoActiveEffectException;
import errors.NotEnoughAmmoException;
import errors.UnloadedWeaponException;
import errors.WeaponCardException;
import models.player.Ammo;

import java.util.*;

public class WeaponCard extends EffectCard {
    public Boolean exclusive;
    public Ammo drawPrice;
    public Ammo reloadPrice;

    private boolean loaded;
    public List<Effect> effects;
    private List<Effect> activatedEffects;
    private Effect activeEffect;

    public WeaponCard(){
        super();
        this.loaded = true;
    }

    @Override
    protected void init(){
        super.init();
        this.activatedEffects = new ArrayList<>();
        activeEffect = null;
    }

    public String getName(){
        return name;
    }

    /** @return the price to reload the ammo, as indicated in the top-left corner of the card */
    public Ammo getReloadPrice(){
        return this.reloadPrice;
    }

    /** @return the price to draw the ammo */
    public Ammo getDrawPrice(){
        return this.drawPrice;
    }

    public boolean isLoaded(){
        return loaded;
    }

    /** Removes the necessary ammos from the "ammo" param to reload the card. You can check if you have enough ammo to reload by calling canReload(ammo) method
     * @param ammo the ammo the player has, the reload cost will be deducted from here. ATTENTION: this function modifies the param
     * @throws NotEnoughAmmoException if the given ammo is not enough to reload. */
    public void load(Ammo ammo, PowerUpCard powerUpCard){
        if(!hasEnoughAmmo(ammo, powerUpCard, reloadPrice)) throw new NotEnoughAmmoException();
        pay(reloadPrice, ammo, powerUpCard);
        loaded = true;
    }

    /** @param ammo your ammo availability
     * @param powerUpCards PowerUp cards you can pay with
     *  @return true if the provided ammos are enough to reload. */
    public boolean canReload(Ammo ammo, List<PowerUpCard> powerUpCards){
        return hasEnoughAmmo(ammo, powerUpCards, reloadPrice);
    }

    /** @param ammo your ammo availability
     * @param powerUpCard the PowerUp card you want to pay with
     *  @return true if the provided ammos are enough to reload. */
    public boolean canReload(Ammo ammo, PowerUpCard powerUpCard){
        return hasEnoughAmmo(ammo, powerUpCard, reloadPrice);
    }

    /** Check that you have enough money to draw the card */
    public boolean canDraw(Ammo ammo, List<PowerUpCard> powerUpCards){
        return hasEnoughAmmo(ammo, powerUpCards, drawPrice);
    }

    /** Check that you have enough money to draw the card */
    public boolean canDraw(Ammo ammo, PowerUpCard powerUpCard){
        return hasEnoughAmmo(ammo, powerUpCard, drawPrice);
    }

    /** If the card is loaded, activate it and unload it
     * @throws WeaponCardException if the weapon is already active
     * @throws UnloadedWeaponException if the weapon is not loaded
     * @throws NullPointerException if player is null */
    @Override
    public void activate(){
        if(me==null) throw new NullPointerException("Player cannot be null");
        if(activated) throw new WeaponCardException("Weapon is already active");
        if(!isLoaded()) throw new UnloadedWeaponException();
        this.activated = true;
        this.loaded = false;
        this.gameMap = me.getGameMap();
    }

    /** Get all effects, with a TRUE flag on the ones that can be used. The card must have been activated
     * to get any activable effect
     * @param ammo your ammo
     * @return all the effects with the flag set to false if the card has not been activated */
    public LegitEffects getEffects(Ammo ammo, List<PowerUpCard> powerUpCards){
        if(!activated) return noActivableEffects();
        LegitEffects out = getAffordableEffects(ammo, powerUpCards);
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

    public Effect getEffectByName(String name){
        return effects.stream().filter(e -> e.name.equals(name)).findFirst().orElse(null);
    }

    /** Activates the chosen effect, paying it with the ammos given as param
     * @param effect the effect to play
     * @param ammo ATTENTION: this param will be modified if the effect activation was successfull. The effect price will be deducted from it
     * @throws WeaponCardException if you do not have enough ammo to activate the effect */
    public void playEffect(Effect effect, Ammo ammo, PowerUpCard powerUpCard){
        if(!hasEnoughAmmo(ammo, powerUpCard, effect.price))throw new WeaponCardException("Not enough ammo to activate this effect");
        pay(effect.price, ammo, powerUpCard);
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
    @Override
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

    /** @return All effects and set the 'activable' flag to FALSE */
    private LegitEffects noActivableEffects(){
        LegitEffects out = new LegitEffects();
        for(Effect e : effects){
            out.addEffect(e, false);
        }
        return out;
    }

    /** Loops all effects and returns them, those that can be bought will have a TRUE flag */
    private LegitEffects getAffordableEffects(Ammo ammo, List<PowerUpCard> powerUpCards){
        LegitEffects out = new LegitEffects();
        for(Effect e : effects){
            boolean affordable = hasEnoughAmmo(ammo, powerUpCards, e.price);
            out.addEffect(e, affordable);
        }
        return out;
    }

    /** Check if you have enough ammo to activate this card */
    private boolean hasEnoughAmmo(Ammo ammo, List<PowerUpCard> powerUpCards, Ammo price){
        if(powerUpCards.isEmpty()) return ammo.isGreaterThanOrEqual(price);
        for(PowerUpCard card : powerUpCards){
            Ammo tot = ammo.getCopy();
            tot.add(new Ammo(card));
            if(tot.isGreaterThanOrEqual(price)) return true;
        }
        return false;
    }
    private boolean hasEnoughAmmo(Ammo ammo, PowerUpCard powerUpCard, Ammo price){
        List<PowerUpCard> powerUpCards = new ArrayList<>();
        if(powerUpCard!=null) powerUpCards.add(powerUpCard);
        return hasEnoughAmmo(ammo, powerUpCards, price);
    }

    /** Get all effects with orderId equal to the one given as param or orderId=-1 */
    private LegitEffects getByOrderId(int orderId){
        Map<Effect, Boolean> map = new HashMap<>();
        boolean baseEffectActivated = true;
        if(orderId == 0 && !exclusive && !activatedEffects.contains(effects.get(0))){
            baseEffectActivated = false;
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

    /** Remove the ammos from your reserve and throws the powerUp */
    private void pay(Ammo price, Ammo ammo, PowerUpCard powerUpCard){
        Ammo newPrice = price.getCopy();
        if(powerUpCard!=null){
            Ammo powerAmmo = new Ammo(powerUpCard);
            newPrice.remove(powerAmmo);
            me.throwPowerUp(powerUpCard);
        }
        ammo.remove(newPrice);
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
        WeaponCard weaponCard = (WeaponCard) o;
        // field comparison
        return name.equals(weaponCard.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
