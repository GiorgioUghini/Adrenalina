package network.responses;

import models.map.GameMap;
import models.player.Player;
import network.Response;
import network.ResponseHandlerInterface;

import java.util.List;

public class ReconnectPlayerResponse implements Response {

    public Player player;
    public GameMap map;
    public List<Player> players;
    public int mapIndex;

    public ReconnectPlayerResponse(Player player, GameMap map, List<Player> players, int mapIndex){
        this.player = player;
        this.map = map;
        this.players = players;
        this.mapIndex = mapIndex;
    }

    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}