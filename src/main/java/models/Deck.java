package models;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class Deck {

    private LinkedList<Card> cards = new LinkedList<>();

    Deck(List<Card> cards){
        this.cards.addAll(cards);
    }

    public void shuffle(){
        Collections.shuffle(cards);
    }

    public Card draw(){
        return cards.pop();
    }

    public int size(){
        return cards.size();
    }
}