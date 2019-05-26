package network.updates;

import network.Response;
import network.ResponseHandlerInterface;

public class PlayerDisconnectUpdate implements Response {

    private String name;

    public String getName() {
        return name;
    }

    public PlayerDisconnectUpdate(String name) {
        this.name = name;
    }

    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}
