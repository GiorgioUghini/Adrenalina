package network.updates;

import network.Response;
import network.ResponseHandlerInterface;

public class ChooseMapUpdate implements Response {
    String username;

    public ChooseMapUpdate(String username) {
        this.username = username;
    }

    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }

    public String getUsername() {
        return username;
    }
}
