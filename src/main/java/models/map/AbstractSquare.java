package models.map;

public abstract class AbstractSquare {
    private Coordinate coordinates;
    private Room room;

    public AbstractSquare(){
        this.coordinates = new Coordinate(0,0);
    }
    public AbstractSquare(Room room, Coordinate coordinates){
        this.coordinates = coordinates;
        this.room = room;
    }
    public Coordinate getCoordinates(){
        return this.coordinates;
    }
    public int getX(){
        return this.coordinates.getX();
    }
    public int getY(){
        return this.coordinates.getY();
    }
    public Room getRoom(){
        return room;
    }
    public boolean equals(AbstractSquare other){
        if(this==other) return true;
        if(this.getCoordinates().equals(other.getCoordinates()) && this.getRoom().equals(other.getRoom())) return true;
        return false;
    }
}
