package models.card;

import models.player.Ammo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Effect implements Serializable {
    public String name;
    public List<Action> actions;
    public Ammo price;
    public int orderId;

    public Effect(){
        price = new Ammo();
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
        Effect e = (Effect) o;
        // field comparison
        return name.equals(e.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}