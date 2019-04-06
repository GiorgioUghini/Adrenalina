package models;

import java.util.List;

public class WeaponDeck extends Deck {

    public WeaponDeck(List<WeaponCard> cards){
        super((List<Card>)(List<?>) cards);
    }

}