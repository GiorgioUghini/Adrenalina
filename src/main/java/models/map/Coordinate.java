package models.map;

public class Coordinate {
    private int x;
    private int y;
    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }
    public int[] getCoordinates(){
        int[] out = {x, y};
        return out;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public boolean equals(Coordinate other){
        if(this==other) return true;
        if(this.getX()==other.getX() && this.getY()==other.getY())return true;
        return false;
    }
}
