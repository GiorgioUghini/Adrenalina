package models.player;

import errors.InvalidAmmoException;
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

    public void add(Ammo ammo){
        if(ammo.red<0 || ammo.yellow<0 || ammo.blue<0) throw new InvalidAmmoException();
        red += ammo.red;
        yellow += ammo.yellow;
        blue += ammo.blue;
        if(red>3) red=3;
        if(yellow>3) yellow = 3;
        if(blue>3) blue = 3;
    }

    @Override
    public boolean equals(Object o) {
        // self check
        if (this == o)
            return true;
        // null check
        if (o == null)
            return false;
        // type check and cast
        if (getClass() != o.getClass())
            return false;
        Ammo ammo = (Ammo) o;
        // field comparison
        return (
            blue == ammo.blue &&
            yellow == ammo.yellow &&
            red == ammo.red
        );
    }
}
