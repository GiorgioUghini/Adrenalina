package models.card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Damage implements Serializable {
    public TargetType type;
    public String target;
    public Integer value;
    //exclude some player from the damage
    public List<String> except;

    public Damage(){
        except = new ArrayList<>();
    }
}
