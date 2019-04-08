package models.map;

import errors.NotWallException;

import java.util.EnumMap;
import java.util.Map;
import java.util.Iterator;

public abstract class Square{
    private RoomColor color;
    private boolean isSpawnPoint;
    private Map<CardinalDirection, SquareLink> links;
    private int id;

    public Square(RoomColor color, boolean isSpawnPoint, int id){
        this.color = color;
        this.isSpawnPoint = isSpawnPoint;
        links = new EnumMap<>(CardinalDirection.class);
        this.id = id;
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
        SquareLink link = links.get(side);
        links.get(side).upgradeToDoor();
    }

    public boolean isSpawnPoint(){
        return isSpawnPoint;
    }
    public RoomColor getColor(){
        return this.color;
    }
    public Square getNextSquare(CardinalDirection direction){
        if(hasNext(direction)){
            return getLink(direction).getNextSquare();
        }
        return null;

    }
    public boolean hasNext(CardinalDirection direction){
        return (this.links.get(direction) != null);
    }
    public boolean hasNext(CardinalDirection direction, boolean inSameRoom){
        if(!inSameRoom) return hasNext(direction);
        if(!hasNext(direction)) return false;
        return this.links.get(direction).isSameRoom();
    }
    public boolean hasNextWalkable(CardinalDirection direction){
        return hasNext(direction) && !getLink(direction).isWall();
    }
    public SquareLink getLink(CardinalDirection direction){
        return this.links.get(direction);
    }
    public int getId(){return id;}
    public boolean hasDoors(){
        Iterator iterator = links.values().iterator();
        while (iterator.hasNext()){
            SquareLink link = (SquareLink) iterator.next();
            if(link.isDoor())return true;
        }
        return false;
    }
    @Override
    public boolean equals(Object object){
        if(object==null) return false;
        if(object==this) return true;
        try{
            Square s = (Square)object;
            return s.getId() == getId();
        }catch(Exception e){
            return false;
        }
    }
}
