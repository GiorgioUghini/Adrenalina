package models.GameMap;

public class SpawnPoint extends SquareDecorator {

    public SpawnPoint(AbstractSquare abstractSquare){
        this.abstractSquare = abstractSquare;
    }
}
