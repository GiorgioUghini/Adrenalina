package network.updates;

import network.Response;
import network.ResponseHandlerInterface;

public class NewPlayerUpdate implements Response {
    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}
