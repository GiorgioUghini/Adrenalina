package models.map;

import errors.CardAlreadyExistsException;
import errors.NothingToGrabException;
import models.card.Card;
import models.card.WeaponCard;
import models.player.Ammo;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class SpawnPoint extends Square implements Serializable {
    private Set<WeaponCard> cards;

    public SpawnPoint(RoomColor color, int id, UUID mapId) {
        super(color, true, id, mapId);
        this.cards = new HashSet<>();
    }

    /**
     * Add the card to the set of ammo cards on this square
     *
     * @throws CardAlreadyExistsException if there are already 3 cards here
     * @throws NullPointerException       if card is null
     * @throws ClassCastException         if the card is not an instance of AmmoCard
     */
    @Override
    public void addCard(Card card) {
        if (card == null) throw new NullPointerException();
        if (cards.size() > 2) throw new CardAlreadyExistsException();
        WeaponCard toAdd = (WeaponCard) card;
        toAdd.reset();
        if (!toAdd.isLoaded()) {
            toAdd.load(new Ammo(3, 3, 3), null);
        }
        cards.add((WeaponCard) card);
    }

    /**
     * Get the weapon cards on this spawn point
     *
     * @return a set containing all the cards on this square. Could be an empty set if there are none
     */
    public List<WeaponCard> showCards() {
        return cards.stream().sorted(Comparator.comparing(WeaponCard::getName)).collect(Collectors.toList());
    }

    /**
     * Draws a card and removes it from the list
     *
     * @param card the card to draw
     * @throws NothingToGrabException if it does not have that card
     */
    public Card drawCard(WeaponCard card) {
        if (!cards.contains(card)) throw new NothingToGrabException();
        cards.remove(card);
        return card;
    }

    /**
     * @return the weapon card with the given name or null if there is not that card on this square
     */
    public Card getByName(String cardName) {
        Card out = cards.stream().filter(c -> c.name.equals(cardName)).findFirst().orElse(null);
        return out;
    }
}
