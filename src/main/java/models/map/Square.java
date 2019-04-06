package models.GameMap;

import models.AmmoCard;
import models.Card;

public class Square extends AbstractSquare{

    private AmmoCard ammoCard;

    public Square(Room room, Coordinate coordinate){
        super(room, coordinate);
    }

    public void spawnAmmo(AmmoCard ammoCard) {
        this.ammoCard = ammoCard;
    }

}
