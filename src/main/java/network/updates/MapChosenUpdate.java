package network.updates;

import network.Response;
import network.ResponseHandlerInterface;

public class MapChosenUpdate implements Response {
    int map;

    public MapChosenUpdate(int map) {
        this.map = map;
    }

    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }

    public int getMap() {
        return map;
    }
}
