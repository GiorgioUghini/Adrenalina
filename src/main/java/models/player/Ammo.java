package models.player;

import errors.NotEnoughAmmoException;

import java.io.Serializable;

public class Ammo implements Serializable {
    public Ammo() {
        red = 0;
        blue = 0;
        yellow = 0;
    }
    public Ammo(int red, int blue, int yellow){
        this.red = red;
        this.blue=blue;
        this.yellow=yellow;
    }
    public int red;
    public int blue;
    public int yellow;

    public void remove(Ammo ammo){
        if(!(red >= ammo.red && yellow >= ammo.yellow && blue >= ammo.blue )) throw new NotEnoughAmmoException();
        red -= ammo.red;
        yellow -= ammo.yellow;
        blue -= ammo.blue;
    }
}
