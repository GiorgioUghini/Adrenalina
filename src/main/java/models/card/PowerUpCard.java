package models.card;

import errors.WeaponCardException;
import models.map.RoomColor;
import models.player.Ammo;
import models.player.Player;

import java.util.List;

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
    protected void init(){
        super.init();
        pricePaid = false;
    }

    public boolean hasPrice(){
        return this.hasPrice;
    }

    public void payPrice(Ammo yourAmmo, Ammo whichAmmo, PowerUpCard powerUpCard){
        if(!hasPrice) return;
        if(!hasEnoughAmmo(yourAmmo, whichAmmo, powerUpCard)) throw new WeaponCardException("Not enough ammo to pay");
        if(whichAmmo!=null && (whichAmmo.red+whichAmmo.yellow+whichAmmo.blue) != 1) throw new WeaponCardException("You have to pay exactly one ammo of any color");
        pay(yourAmmo, whichAmmo, powerUpCard);
        this.pricePaid = true;
    }
    public boolean pricePaid(){
        return (!hasPrice || pricePaid);
    }

    @Override
    public void activate(Player me){
        if(me==null) throw new NullPointerException("Player cannot be null");
        if(hasPrice && !pricePaid) throw new WeaponCardException("You need to pay this powerup price to active it. card: " + this.name);
        this.activated = true;
        this.gameMap = me.getGameMap();
        this.me = me;
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

    private boolean hasEnoughAmmo(Ammo ammo, Ammo whichAmmo, PowerUpCard powerUpCard){
        if(!hasPrice) return true;
        if(powerUpCard != null) return true;
        if(whichAmmo==null) return false;
        return ammo.isGreaterThanOrEqual(whichAmmo);
    }

    private void pay(Ammo yourAmmo, Ammo whichAmmo, PowerUpCard powerUpCard){
        if(powerUpCard!=null){
            me.throwPowerUp(powerUpCard);
        }else{
            yourAmmo.remove(whichAmmo);
        }
    }
}