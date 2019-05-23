package network.updates;

import network.*;

public class NewPlayerUpdate implements Response {
    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}
