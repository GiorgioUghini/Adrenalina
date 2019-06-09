package models.card;

import models.map.RoomColor;

import java.util.List;

public class PowerUpCard extends EffectCard {
    public RoomColor color;
    public boolean hasPrice = false;
    public String when;
    public List<Action> effect;
}