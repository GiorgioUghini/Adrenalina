package models;

import models.card.Effect;
import java.util.List;

public class EffectCard extends Card  {
    public String name;
    public Boolean exclusive;
    public List<Effect> effects;
}