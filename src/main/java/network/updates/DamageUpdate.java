package network.updates;

import models.player.Player;
import network.Response;
import network.ResponseHandlerInterface;

public class DamageUpdate implements Response {
    public Player player;

    public DamageUpdate(Player player) {
        this.player = player;
    }

    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}
