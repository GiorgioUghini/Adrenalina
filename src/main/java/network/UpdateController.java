package network;

import models.player.Player;

public class UpdateController implements UpdateHandler {
    @Override
    public void handle(PlayerDisconnectUpdate update) {
        Player player = update.player;
        System.out.println("Addio " +  player.getName());
    }
}
