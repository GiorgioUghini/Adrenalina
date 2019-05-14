package models.map;

import errors.NotWallException;
import models.card.Card;

import java.util.*;

public abstract class Square{
    private RoomColor color;
    private boolean isSpawnPoint;
    private Map<CardinalDirection, SquareLink> links;
    private int id;
    private UUID mapId;

    public Square(RoomColor color, boolean isSpawnPoint, int id, UUID mapId){
        this.color = color;
        this.isSpawnPoint = isSpawnPoint;
        links = new EnumMap<>(CardinalDirection.class);
        this.id = id;
        this.mapId = mapId;
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
    public UUID getMapId(){return mapId;}
    public boolean hasDoors(){
        Iterator iterator = links.values().iterator();
        while (iterator.hasNext()){
            SquareLink link = (SquareLink) iterator.next();
            if(link.isDoor())return true;
        }
        return false;
    }

    public abstract void addCard(Card card);

    @Override
    public boolean equals(Object o) {
        // self check
        if (this == o)
            return true;
        // null check
        if (o == null)
            return false;
        // type check and cast
        if (getClass() != o.getClass())
            return false;
        Square sq = (Square) o;
        // field comparison
        return (getId() == (sq.getId()) && (getMapId().equals(sq.getMapId())) );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId()) * getMapId().hashCode();
    }
}
