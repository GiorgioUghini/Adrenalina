package models.map;

import errors.NotWallException;

public class SquareLink {
    private Square square;
    private LinkType linkType;

    public SquareLink(Square nextSquare, LinkType linkType){
        square = nextSquare;
        this.linkType = linkType;
    }

    public Square getNextSquare(){
        return square;
    }
    public LinkType getLinkType(){
        return linkType;
    }
    public boolean isDoor(){
        return linkType.equals(LinkType.DOOR);
    }
    public boolean isWall(){
        return linkType.equals(LinkType.WALL);
    }
    public boolean isVisible(){
        return !isWall();
    }
    public boolean isSameRoom(){
        return (!isWall() && !isDoor());
    }
    public void upgradeToDoor() throws NotWallException {
        if(!isWall()) throw new NotWallException();
        this.linkType = LinkType.DOOR;
    }
}
