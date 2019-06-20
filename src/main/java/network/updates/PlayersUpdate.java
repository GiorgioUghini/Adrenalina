package network.updates;

import models.player.Player;
import network.Response;
import network.ResponseHandlerInterface;

import java.util.List;

public class PlayersUpdate implements Response {
    public List<Player> players;

    public PlayersUpdate(List<Player> players) {
        this.players = players;
    }

    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}
