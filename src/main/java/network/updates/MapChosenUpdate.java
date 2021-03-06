package network.updates;

import models.map.GameMap;
import network.Response;
import network.ResponseHandlerInterface;

public class MapChosenUpdate implements Response {
    public int mapIndex;
    public GameMap map;

    public MapChosenUpdate(GameMap map, int mapIndex) {
        this.map = map;
        this.mapIndex = mapIndex;
    }

    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}
