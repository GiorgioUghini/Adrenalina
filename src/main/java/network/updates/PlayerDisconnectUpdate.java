package network.updates;

import models.player.Player;
import network.Response;
import network.ResponseHandlerInterface;

public class PlayerDisconnectUpdate implements Response {

    public Player player;

    public PlayerDisconnectUpdate(Player player){
        this.player = player;
    }

    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}
