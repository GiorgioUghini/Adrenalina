package models;

import java.util.ArrayList;

class Ammo {
    public Ammo() {
        red = 0;
        green = 0;
        yellow = 0;
    }
    int red;
    int green;
    int yellow;
}

public class Player {

    private boolean isFirstPlayer;
    //private Miniatura miniatura;
    private Ammo ammo = new Ammo();
    private ArrayList<WeaponCard> weaponList = new ArrayList<>();
    private ArrayList<PowerUpCard> powerUpList = new ArrayList<>();


}
