package models.card;

import java.io.Serializable;
import java.util.List;

public class AmmoDeck extends Deck implements Serializable {
    public AmmoDeck(List<Card> cards){
        super(cards);
    }
}
