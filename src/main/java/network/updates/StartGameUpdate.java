package network.updates;

import network.*;

public class StartGameUpdate implements Response {

    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}
