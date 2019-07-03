package models.card;

import errors.WeaponCardException;
import models.map.RoomColor;
import models.player.Ammo;

import java.util.List;
import java.util.Objects;

public class PowerUpCard extends EffectCard {
    public RoomColor color;
    public boolean hasPrice = false;
    private boolean pricePaid = false;
    public String when;
    public List<Action> effects;

    public PowerUpCard(){
        super();
    }

    @Override
    /** {@inheritDoc} */
    protected void init(){
        super.init();
        pricePaid = false;
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
        PowerUpCard powerUpCard = (PowerUpCard) o;
        // field comparison
        return (powerUpCard.name.equals(name) && powerUpCard.color.equals(color));
    }

    @Override
    public int hashCode() {
        return Objects.hash(name,color);
    }

    /** @return the name of the card and a character representing its color (e.g Teletrasporto(B)) */
    public String getFullName(){
        String color = "(" + this.color.toString().charAt(0) + ")";
        return this.name + color;
    }

    /** @return true if the card has a price */
    public boolean getHasPrice(){
        return this.hasPrice;
    }

    /**
     * @param yourAmmo the total ammos of the player
     * @param whichAmmo an Ammo object in which only one of the color should value 1, represents the ammo you want to pay with
     * @param powerUpCard if you want to pay with a powerUp, in this case whichAmmo can be null */
    public void payPrice(Ammo yourAmmo, Ammo whichAmmo, PowerUpCard powerUpCard){
        if(!hasPrice) return;
        if(!hasEnoughAmmo(yourAmmo, whichAmmo, powerUpCard)) throw new WeaponCardException("Not enough ammo to pay");
        if(whichAmmo!=null && (whichAmmo.red+whichAmmo.yellow+whichAmmo.blue) != 1) throw new WeaponCardException("You have to pay exactly one ammo of any color");
        pay(yourAmmo, whichAmmo, powerUpCard);
        this.pricePaid = true;
    }

    @Override
    /** {@inheritDoc} */
    public void activate(){
        if(me==null) throw new NullPointerException("Player cannot be null");
        if(hasPrice && !pricePaid) throw new WeaponCardException("You need to pay this powerup price to active it. card: " + this.name);
        this.activated = true;
        this.gameMap = me.getGameMap();
    }

    /** activates the next action inside the effect. Must be called for the first action too. It also cancels the active effect and action if
     * there are no more actions. In this case, on the last call it returned null
     * @return the action that has just been activated, or null if there are no more actions
     * @throws WeaponCardException if the card has not been activated
     * */
    @Override
    public Action playNextAction(){
        if (!this.activated) throw new WeaponCardException("Card must be activated before it can play any action");
        if(activeAction == null) {
            activeAction = effects.get(0);
            return activeAction;
        }
        int lastActionIndex = effects.indexOf(activeAction);
        if(lastActionIndex == effects.size()-1){
            activeAction = null;
            activated = false;
            return null;
        }
        activeAction = effects.get(lastActionIndex + 1);
        return activeAction;
    }

    /** if the ammo is free return true, otherwise check that you have at least the ammo you want to pay with or a powerUp */
    private boolean hasEnoughAmmo(Ammo ammo, Ammo whichAmmo, PowerUpCard powerUpCard){
        if(!hasPrice) return true;
        if(powerUpCard != null) return true;
        if(whichAmmo==null) return false;
        return ammo.isGreaterThanOrEqual(whichAmmo);
    }

    /** Removes the ammo or throws the powerUp based on what you wanted to pay with */
    private void pay(Ammo yourAmmo, Ammo whichAmmo, PowerUpCard powerUpCard){
        if(powerUpCard!=null){
            me.throwPowerUp(powerUpCard);
        }else{
            yourAmmo.remove(whichAmmo);
        }
    }
}