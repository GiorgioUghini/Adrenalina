package network;

import models.player.Player;
import network.updates.PlayerDisconnectUpdate;
import network.updates.StartGameUpdate;

public class UpdateHandler implements UpdateHandlerInterface {
    @Override
    public void handle(PlayerDisconnectUpdate update) {
        Player player = update.player;
        System.out.println("Addio " +  player.getName());
    }

    @Override
    public void handle(StartGameUpdate update) {

    }
}
