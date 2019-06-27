package network.responses;

import models.player.Player;
import network.Response;
import network.ResponseHandlerInterface;

public class RegisterPlayerResponse implements Response {

    private String me;

    public RegisterPlayerResponse(String name){
        me = name;
    }

    public String getMe() {
        return me;
    }

    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}