package models.map;

import errors.NotWallException;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public abstract class Square{
    private RoomColor color;
    private boolean isSpawnPoint;
    private Map<CardinalDirection, SquareLink> links;
    private int id;

    public Square(RoomColor color){
        this.color = color;
        this.isSpawnPoint = false;
        links = new HashMap<>();
    }
    public Square(RoomColor color, boolean isSpawnPoint){
        this.color = color;
        this.isSpawnPoint = isSpawnPoint;
        links = new HashMap<>();
    }

    public void connectToSquare(Square square, CardinalDirection side){
        LinkType linkType;
        if(getColor().equals(square.getColor())){
            linkType = LinkType.NOTHING;
        }else {
            linkType = LinkType.WALL;
        }
        SquareLink newConnection = new SquareLink(square, linkType);
        links.put(side, newConnection);
    }
    public void addDoor(CardinalDirection side) throws NotWallException{
        links.get(side).upgradeToDoor();
    }

    public void setSpawnPoint(boolean isSpawnPoint){
        this.isSpawnPoint = isSpawnPoint;
    }
    public boolean isSpawnPoint(){
        return isSpawnPoint;
    }
    public RoomColor getColor(){
        return this.color;
    }
    public Square getNextSquare(CardinalDirection direction){
        return getLink(direction).getNextSquare();
    }
    public boolean hasNext(CardinalDirection direction){
        return (this.links.get(direction) != null);
    }
    private SquareLink getLink(CardinalDirection direction){
        return this.links.get(direction);
    }
    public boolean hasDoors(){
        Iterator iterator = links.values().iterator();
        while (iterator.hasNext()){
            SquareLink link = (SquareLink) iterator.next();
            if(link.isDoor())return true;
        }
        return false;
    }
}
