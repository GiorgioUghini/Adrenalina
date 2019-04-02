package models.GameMap;

import java.util.Set;

public class Room {
    private AbstractSquare[][] squares;
    private int width;
    private int height;
    private String color;

    public Room(int width, int height, String color, Set<AbstractSquare> doors, Set<AbstractSquare> spawnPoints){
        this.color = color;
        this.width = width;
        this.height = height;
        squares = new AbstractSquare[width][height];
        for(int x=0; x<width;x++){
            for (int y=0; y<height; y++){
                Coordinate c = new Coordinate(x,y);
                AbstractSquare square = new Square(this, c);
                if(doors.contains(square)){
                    square = new Door(square);
                }
                if(spawnPoints.contains(square)){
                    square = new SpawnPoint(square);
                }
                squares[x][y] = square;
            }
        }
    }
    public String getColor(){
        return this.color;
    }
    public boolean equals(Room other){
        if(this==other)return true;
        if(this.getColor().equals(other.getColor()))return true;
        return false;
    }
}
