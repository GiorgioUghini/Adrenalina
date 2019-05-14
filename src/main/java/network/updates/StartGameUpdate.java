package network.updates;

import network.Update;
import network.UpdateHandlerInterface;

public class StartGameUpdate implements Update {

    @Override
    public void handle(UpdateHandlerInterface handler) {
        handler.handle(this);
    }
}
