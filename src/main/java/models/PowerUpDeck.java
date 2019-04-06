package models;

import java.util.List;

public class PowerUpDeck extends Deck {

    public PowerUpDeck(List<PowerUpCard> cards){
        super((List<Card>)(List<?>) cards);
    }

}