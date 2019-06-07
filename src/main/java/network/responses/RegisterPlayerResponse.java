package network.responses;

import models.player.Player;
import network.Response;
import network.ResponseHandlerInterface;

public class RegisterPlayerResponse implements Response {

    private Player me;

    public RegisterPlayerResponse(Player p){
        me = p;
    }

    public Player getMe() {
        return me;
    }

    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}