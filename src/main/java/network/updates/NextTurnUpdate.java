package network.updates;

import network.Response;
import network.ResponseHandlerInterface;

public class NextTurnUpdate implements Response {
    public String name;

    public NextTurnUpdate(String name) {
        this.name = name;
    }

    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}
