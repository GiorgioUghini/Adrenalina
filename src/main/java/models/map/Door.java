package models.map;

public class Door extends SquareDecorator {
    private Door nextDoor;

    public Door (AbstractSquare abstractSquare){
        this.abstractSquare = abstractSquare;
    }
    public void setNextDoor(Door door){
        nextDoor = door;
    }
    public Door getNextDoor(){
        return nextDoor;
    }
}
