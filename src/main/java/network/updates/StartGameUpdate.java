package network.updates;

import models.player.Player;
import network.Response;
import network.ResponseHandlerInterface;

import java.util.List;

public class StartGameUpdate implements Response {

    public List<Player> players;

    public StartGameUpdate(List<Player> players){
        this.players = players;
    }

    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}
