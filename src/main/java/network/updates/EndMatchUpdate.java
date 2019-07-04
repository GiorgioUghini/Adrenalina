package network.updates;

import models.player.Player;
import network.Response;
import network.ResponseHandlerInterface;

import java.util.Map;

public class EndMatchUpdate implements Response {
    public Map<Player, Integer> points;

    public EndMatchUpdate(Map<Player, Integer> points) {
        this.points = points;
    }

    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}
