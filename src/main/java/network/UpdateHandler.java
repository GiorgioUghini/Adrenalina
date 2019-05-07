package network;

import models.player.Player;

public class UpdateHandler implements UpdateHandlerInterface {
    @Override
    public void handle(PlayerDisconnectUpdate update) {
        Player player = update.player;
        System.out.println("Addio " +  player.getName());
    }
}
