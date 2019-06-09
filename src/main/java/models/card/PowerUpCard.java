package models.card;

import errors.NoActiveEffectException;
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
    public List<Action> effect;

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

    public void payPrice(Ammo yourAmmo, Ammo whichAmmo){
        if(!hasEnoughAmmo(yourAmmo)) throw new WeaponCardException("Not enough ammo to pay");
        pay(yourAmmo, whichAmmo);
        this.pricePaid = true;
    }
    public boolean pricePaid(){
        return (!hasPrice || pricePaid);
    }

    @Override
    public void activate(Player me){
        if(me==null) throw new NullPointerException("Player cannot be null");
        if(!pricePaid) throw new WeaponCardException("You need to pay this powerup price to active it. card: " + this.name);
        this.activated = true;
        this.gameMap = me.getGameMap();
        this.me = me;
    }

    /** activates the next action inside the effect. Must be called for the first action too. It also cancels the active effect and action if
     * there are no more actions. In this case, on the last call it returned null
     * @return the action that has just been activated, or null if there are no more actions
     * @throws NoActiveEffectException if no effect is active at the moment
     * */
    @Override
    public Action playNextAction(){
        if (!this.activated) throw new WeaponCardException("Card must be activated before it can play any action");
        if(activeAction == null) {
            activeAction = effect.get(0);
            return activeAction;
        }
        int lastActionIndex = effect.indexOf(activeAction);
        if(lastActionIndex == effect.size()-1){
            activeAction = null;
            return null;
        }
        activeAction = effect.get(lastActionIndex + 1);
        return activeAction;
    }

    private boolean hasEnoughAmmo(Ammo ammo){
        return (!hasPrice ||
                ammo.red > 0 ||
                ammo.blue > 0 ||
                ammo.yellow > 0
        );
    }

    private void pay(Ammo yourAmmo, Ammo whichAmmo){
        yourAmmo.yellow -= whichAmmo.yellow;
        yourAmmo.red -= whichAmmo.red;
        yourAmmo.blue -= whichAmmo.blue;
    }
}