package models.map;

import errors.CardAlreadyExistsException;
import errors.NothingToGrabException;
import models.card.AmmoCard;
import models.card.Card;

import java.io.Serializable;
import java.util.UUID;

public class AmmoPoint extends Square implements Serializable {
    private AmmoCard ammoCard;

    public AmmoPoint(RoomColor color, int id, UUID mapId){
        super(color, false, id, mapId);
        this.ammoCard = null;
    }

    /** Adds a new ammo card to this ammo point
     * @param card
     * @throws CardAlreadyExistsException if there was already a card on the square
     * @throws NullPointerException if card is null
     * @throws ClassCastException if card is not an instance of AmmoCard
     * */
    @Override
    public void addCard(Card card){
        if(this.ammoCard!=null) throw new CardAlreadyExistsException();
        if(card == null) throw new NullPointerException();
        this.ammoCard = (AmmoCard) card;
    }

    /** @returns the ammo card on this ammo point if there is one, null otherwise. does NOT draw it!
     * */
    public AmmoCard showCard(){
        return this.ammoCard;
    }

    /** draws the ammo card, that will no longer be available on the square and must be replaced at the end of the turn
     * @return the ammocard if there is one
     * @throws NothingToGrabException if there is not an ammo card on this square */
    public AmmoCard drawCard(){
        if(!hasAmmoCard()) throw new NothingToGrabException();
        AmmoCard tmp = ammoCard;
        this.ammoCard = null;
        return tmp;
    }

    private boolean hasAmmoCard(){
        return (this.ammoCard != null);
    }
}
