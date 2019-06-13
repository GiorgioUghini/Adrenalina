package network.updates;

import models.map.GameMap;
import network.Response;
import network.ResponseHandlerInterface;

public class MapUpdate implements Response {
    public GameMap map;

    public MapUpdate(GameMap map) {
        this.map = map;
    }

    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}
