package models.card;

import models.player.Ammo;

import java.util.ArrayList;
import java.util.List;

public class EffectCard extends Card  {
    public String name;
    public Boolean exclusive;
    public Ammo price;
    public List<Effect> effects;
    private List<Effect> activatedEffects;
    private boolean activated = false;

    public EffectCard(){
        this.activatedEffects = new ArrayList<>();
    }

    public Ammo getPrice(){
        return this.price;
    }

    /** Check if you have enough ammo to activate the card
     * @param ammo your ammo
     * @return true iff you have enough ammo */
    public boolean isActivable(Ammo ammo){
        return hasEnoughAmmo(ammo, getPrice());
    }

    /** The price of the card has been paid, activate the card */
    public void activate(){
        this.activated = true;
    }

    /** Get all effect, with a TRUE flag on the ones that can be activated
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

    public void playEffect(Effect effect){
        activatedEffects.add(effect);
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
        LegitEffects out = new LegitEffects();
        effects.stream().forEach(e -> {
            boolean activable = (e.orderId==orderId || e.orderId == -1);
            out.addEffect(e, activable);
        });
        return out;
    }

    private int getLastOrderId(){
        if(activatedEffects.isEmpty()) return 0;
        return activatedEffects.get(activatedEffects.size()-1).orderId;
    }
}