package network.updates;

import network.*;

public class NewPlayerUpdate implements Response {
    private String name;

    public String getName() {
        return name;
    }

    public NewPlayerUpdate(String name) {
        this.name = name;
    }

    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}
