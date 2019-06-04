package models.card;

import java.io.Serializable;

public class Select implements Serializable {
    public TargetType type;
    public String id;
    public Integer max;
    public Integer min;
    public Radix[] radix;
    public Rules rules;
    public boolean auto;

    public Select(){
        auto = false;
        max = 1;
        min = 1;
    }
}
