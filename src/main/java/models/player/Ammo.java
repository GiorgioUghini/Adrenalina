package models.player;

import errors.InvalidAmmoException;
import models.card.PowerUpCard;

import java.io.Serializable;
import java.util.Objects;

public class Ammo implements Serializable {
    public Ammo() {
        red = 0;
        blue = 0;
        yellow = 0;
    }

    public Ammo(int red, int blue, int yellow) {
        this.red = red;
        this.blue = blue;
        this.yellow = yellow;
    }

    public Ammo(PowerUpCard powerUpCard) {
        switch (powerUpCard.color) {
            case RED:
                this.red = 1;
                break;
            case BLUE:
                this.blue = 1;
                break;
            case YELLOW:
                this.yellow = 1;
                break;
            default:
                throw new InvalidAmmoException();
        }
    }

    public int red;
    public int blue;
    public int yellow;

    public void remove(Ammo ammo) {
        red -= ammo.red;
        yellow -= ammo.yellow;
        blue -= ammo.blue;
        if (red < 0) red = 0;
        if (yellow < 0) yellow = 0;
        if (blue < 0) blue = 0;
    }

    /**
     * add the ammos given as param to this
     */
    public void add(Ammo ammo) {
        if (ammo.red < 0 || ammo.yellow < 0 || ammo.blue < 0) throw new InvalidAmmoException();
        red += ammo.red;
        yellow += ammo.yellow;
        blue += ammo.blue;
        if (red > 3) red = 3;
        if (yellow > 3) yellow = 3;
        if (blue > 3) blue = 3;
    }

    public Ammo getCopy() {
        Ammo out = new Ammo();
        out.add(this);
        return out;
    }

    /**
     * Creates a new Ammo with the sum of this ammo and the ammo given as param
     */
    public Ammo getSum(Ammo ammo) {
        Ammo out = getCopy();
        out.add(ammo);
        return out;
    }

    public boolean isEmpty() {
        return red + blue + yellow == 0;
    }

    /**
     * @return true iff each ammo color of this is greater than or equal than the ammo given as param
     */
    public boolean isGreaterThanOrEqual(Ammo ammo) {
        return (
                red >= ammo.red &&
                        yellow >= ammo.yellow &&
                        blue >= ammo.blue
        );
    }

    @Override
    public String toString() {
        return String.format("Red: %d; Blue: %d; Yellow: %d", red, blue, yellow);
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

    @Override
    public int hashCode() {
        return Objects.hash(blue, yellow, red);
    }
}
