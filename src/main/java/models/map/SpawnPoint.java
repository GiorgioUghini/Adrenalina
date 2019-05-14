package models.map;

import errors.CardAlreadyExistsException;
import models.AmmoCard;
import models.Card;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SpawnPoint extends Square {
    private Set<AmmoCard> cards;

    public SpawnPoint(RoomColor color, int id, UUID mapId){
        super(color, true, id, mapId);
        this.cards = new HashSet<>();
    }

    /** Add the card to the set of ammo cards on this square
     * @throws CardAlreadyExistsException if there are already 3 cards here
     * @throws NullPointerException if card is null
     * @throws ClassCastException if the card is not an instance of AmmoCard */
    @Override
    public void addCard(Card card){
        if(card == null) throw new NullPointerException();
        if(cards.size()>2)  throw new CardAlreadyExistsException();
        cards.add((AmmoCard) card);
    }

    /** Get the weapon cards on this spawn point
     * @return a set containing all the cards on this square. Could be an empty set if there are none
     * */
    public Set<AmmoCard> showCards(){
        return cards;
    }

    /** Draws a card and removes it from the list
     * @param card the card to draw
     * @throws NullPointerException if it does not have that card */
    public Card drawCard(AmmoCard card){
        if(!cards.contains(card)) throw new NullPointerException();
        cards.remove(card);
        return card;
    }
}
