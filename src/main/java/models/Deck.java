package models;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

abstract class Deck {

    LinkedList<Card> cards = new LinkedList<>();

    Deck(List<Card> cards){
        this.cards.addAll(cards);
    }

    public void shuffle(){
        Collections.shuffle(cards);
    }

    public Card draw(){
        return cards.pop();
    }
}