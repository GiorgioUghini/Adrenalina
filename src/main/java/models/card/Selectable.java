package models.card;

import models.map.RoomColor;
import models.map.Square;
import models.player.Player;

import java.util.Set;

public class Selectable {
    private Set<Square> squares;
    private Set<Player> players;
    private Set<RoomColor> rooms;
    private boolean optional;
    private TargetType type;

    public Selectable(boolean optional){
        this.optional = optional;
    }

    public void addSquares(Set<Square> squares){
        this.squares = squares;
        this.type = TargetType.SQUARE;
    }

    public void addPlayers(Set<Player> players){
        this.players = players;
        this.type = TargetType.PLAYER;
    }

    public void addRooms(Set<RoomColor> rooms){
        this.rooms = rooms;
        this.type = TargetType.ROOM;
    }

    public TargetType getType(){
        return type;
    }

    public Set<Square> getSquares() {
        return squares;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public Set<RoomColor> getRooms() {
        return rooms;
    }
}
