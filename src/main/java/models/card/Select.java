package models.card;

import java.io.Serializable;

public class Select implements Serializable {
    public TargetType type;
    public String id;
    public Integer max;
    public Radix[] radix;
    public Rules rules;
}
