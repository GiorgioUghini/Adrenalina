package models;

import models.card.Card;
import models.card.Deck;
import models.card.EffectCard;
import models.card.WeaponDeck;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class DeckTest {

    @Test
    public void draw() throws IOException {
        List<Card> cards = new LinkedList<>();
        cards.add(new EffectCard());
        cards.add(new EffectCard());
        cards.add(new EffectCard());
        Deck deck = new WeaponDeck(cards);
        int size = deck.size();
        Card card1 = deck.draw();
        Card card2 = deck.draw();
        assertNotSame(card1, card2);
        assertTrue(size == deck.size()+ 2);
    }

    @Test
    public void drawEmptyDeck() throws IOException {
        Deck emptyDeck = new WeaponDeck(new LinkedList<Card>());
        try{
            emptyDeck.draw();
            assert false;
        }
        catch (NoSuchElementException ex){
            assert true;
        }
    }

    @Test
    public void shuffleDeck(){
        List<Card> cards = new LinkedList<>();
        cards.add(new EffectCard());
        cards.add(new EffectCard());
        cards.add(new EffectCard());
        Deck deck = new WeaponDeck(cards);
        int size1 = deck.size();
        deck.shuffle();
        int size2 = deck.size();
        assertTrue(size1 == size2);
    }
}
