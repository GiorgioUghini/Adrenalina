package models.map;

import models.WeaponCard;

import java.util.ArrayList;
import java.util.List;

public class SpawnPoint extends SquareDecorator {

    private List<WeaponCard> weaponList;

    public SpawnPoint(AbstractSquare abstractSquare){
        this.abstractSquare = abstractSquare;
        this.weaponList = new ArrayList<>();
    }

    public void spawnWeapon(WeaponCard weaponCard) {
        if (weaponList.size() < 3) {
            weaponList.add(weaponCard);
        }
    }
}
