package models.card;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class Deck {

    private LinkedList<Card> cards = new LinkedList<>();

    Deck(List<Card> cards) {
        this.cards.addAll(cards);
    }

    /**
     * shuffles the deck, does not remove nor add cards
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * @return the first card of the deck and removes it
     */
    public Card draw() {
        return cards.pop();
    }

    public int size() {
        return cards.size();
    }
}