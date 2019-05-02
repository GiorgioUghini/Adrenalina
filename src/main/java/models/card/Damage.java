package models.card;

import java.io.Serializable;

public class Damage implements Serializable {
    public TargetType type;
    public String target;
    public Integer value;
}
