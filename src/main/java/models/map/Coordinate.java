package models.map;

import java.io.Serializable;
import java.util.Objects;

public class Coordinate implements Serializable {
    private int x;
    private int y;
    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }
    public int getX(){return x;}
    public int getY(){return y;}

    @Override
    public boolean equals(Object object){
        if(this==object)return true;
        if (!(object instanceof Coordinate)) return false;
        Coordinate other = (Coordinate) object;
        return (other.getX() == getX() && other.getY() == getY());
    }


    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }
}
