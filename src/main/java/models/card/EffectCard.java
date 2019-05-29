package models.card;

import models.player.Ammo;

import java.util.List;

public class EffectCard extends Card  {
    public String name;
    public Boolean exclusive;
    public Ammo price;
    public List<Effect> effects;
}