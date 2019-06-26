package network.updates;

import models.player.Player;
import network.Response;
import network.ResponseHandlerInterface;

import java.util.List;
import java.util.Map;

public class PointsUpdate implements Response {
    public Map<Player, Integer> points;

    public PointsUpdate(Map<Player, Integer> points) {
        this.points = points;
    }

    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}
