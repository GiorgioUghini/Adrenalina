package models.GameMap;

import java.util.Set;

public class Map {
    Set<Room> rooms;

    public Map(Set<Room> rooms){
        this.rooms = rooms;
    }
}
