package models.card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Damage extends Mark implements Serializable {
    //exclude some player from the damage
    public List<String> except;

    public Damage(){
        super();
        except = new ArrayList<>();
    }
}
