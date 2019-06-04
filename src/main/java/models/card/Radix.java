package models.card;

import java.io.Serializable;

public class Radix implements Serializable {
    public String ref;
    public String area;
    public Integer min;
    public Integer max;
    /** Parto da "ref" e andando dritto verso "straight" mi fermo solo se trovo un muro
     *  (throughWalls=false) o vado avanti finché non finisce la mappa (throughWalls=true) */
    public String straight;
    /** da usare in combinazione con "straight" e "area":"cardinal" */
    public boolean throughWalls;

    public Radix(){
        this.min = 0;
        this.max = -1;
        this.straight = null;
        this.throughWalls = false;
    }
}
