package models.card;

import java.io.Serializable;

public class Select implements Serializable {
    public TargetType type;
    public String id;
    public boolean optional;
    public Radix[] radix;
    public Rules rules;
    public boolean auto;

    public Select(){
        auto = false;
        optional = false;
    }
}
