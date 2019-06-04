package network.responses;

import network.Response;
import network.ResponseHandlerInterface;

public class SpawnPlayerResponse implements Response {
    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}
