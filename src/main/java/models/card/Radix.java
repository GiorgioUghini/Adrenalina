package models.card;

import java.io.Serializable;

public class Radix implements Serializable {
    public String ref;
    public String area;
    public Integer min;
    public Integer max;
    /**
     * Starting from 'ref' and going straight towards "straight". Stop at walls if throughWalls=false,
     * otherwise go straight till a wall is found
     */
    public String straight;
    /**
     * to be used with "straight" and "cardinal"
     */
    public boolean throughWalls;

    public Radix() {
        this.min = 0;
        this.max = -1;
        this.straight = null;
        this.throughWalls = false;
    }
}
