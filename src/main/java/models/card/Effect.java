package models.card;

import models.player.Ammo;

import java.io.Serializable;
import java.util.List;

public class Effect implements Serializable {
    public String name;
    public List<Action> actions;
    public Ammo price;
    public int orderId;
}