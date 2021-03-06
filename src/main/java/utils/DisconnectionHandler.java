package utils;

import models.Match;
import models.card.PowerUpCard;
import models.map.GameMap;
import models.map.SpawnPoint;
import models.player.Player;
import network.Server;
import network.updates.EndMatchUpdate;
import network.updates.MapUpdate;
import network.updates.PlayerDisconnectUpdate;

public class DisconnectionHandler {
    /**
     * handles the disconnection of the given player. If less than 3 players remain the game ends
     * @param player the player disconnected
     */
    public static void handle(Player player) {
        player.disconnect();
        Match match = Server.getInstance().getLobby().getMatch(player);

        if (match.getPlayersOnlineNumber() < 3) {
            match.addUpdate(new EndMatchUpdate(match.getTotalPoints()));
            return;
        }

        if (match.getCurrentPlayer().getName().equals(player.getName())) {
            if (player.hasJustStarted()) {
                while (player.getPowerUpList().size() < 2) {
                    player.drawPowerUp();
                }
                PowerUpCard powerUpCard = player.getPowerUpList().get(0);
                player.throwPowerUp(powerUpCard);
                GameMap map = match.getMap();
                SpawnPoint spawnPoint = map.getSpawnPoints().stream().filter(p -> p.getColor() == powerUpCard.color).findFirst().orElse(null);
                map.spawnPlayer(player, spawnPoint);
                match.addUpdate(new MapUpdate(match.getMap()));
            }
            match.nextTurn();
        }
        match.addUpdate(new PlayerDisconnectUpdate(player.getName()));
    }
}
