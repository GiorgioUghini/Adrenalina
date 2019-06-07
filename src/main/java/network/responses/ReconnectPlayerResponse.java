package network.responses;

import models.player.Player;
import network.Response;
import network.ResponseHandlerInterface;

public class ReconnectPlayerResponse implements Response {

    private Player me;

    public ReconnectPlayerResponse(Player p){
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