package network.updates;

import models.player.Player;
import network.Update;
import network.UpdateHandlerInterface;

public class PlayerDisconnectUpdate implements Update {

    public Player player;

    public PlayerDisconnectUpdate(Player player){
        this.player = player;
    }

    @Override
    public void handle(UpdateHandlerInterface handler) {
        handler.handle(this);
    }
}
