package models.map;

import java.util.UUID;

public class SpawnPoint extends Square {
    //TODO add array of cards

    public SpawnPoint(RoomColor color, int id, UUID mapId){
        super(color, true, id, mapId);
    }
}
