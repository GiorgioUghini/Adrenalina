package network.updates;

import models.player.Player;
import network.Update;
import network.UpdateHandlerInterface;

public class StartGameUpdate implements Update {

    public Player player;

    public StartGameUpdate(Player player){
        this.player = player;
    }

    @Override
    public void handle(UpdateHandlerInterface handler) {
        handler.handle(this);
    }
}
