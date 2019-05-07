package models.map;

import java.util.UUID;

public class AmmoPoint extends Square {
    public AmmoPoint(RoomColor color, int id, UUID mapId){
        super(color, false, id, mapId);
    }
}
