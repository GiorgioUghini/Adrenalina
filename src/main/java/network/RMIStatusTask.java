package network;

import models.Match;
import models.card.PowerUpCard;
import models.map.GameMap;
import models.map.SpawnPoint;
import models.player.Player;
import network.updates.MapUpdate;
import network.updates.PlayerDisconnectUpdate;

import java.util.TimerTask;

public class RMIStatusTask extends TimerTask {

    private boolean ping;
    private RMIWrapper rmiWrapper;

    RMIStatusTask(RMIWrapper rmiWrapper){
        this.ping = true;
        this.rmiWrapper = rmiWrapper;
    }

    @Override
    public synchronized void run() {
        if(!ping){
            String token = Server.getInstance().getConnection().getToken(rmiWrapper);
            Player player = Server.getInstance().getLobby().getPlayer(token);
            if(player != null){
                if(player.isWaiting()){
                    Server.getInstance().getLobby().disconnectPlayer(player);
                    Server.getInstance().getLobby().addUpdateWaitingPlayers(new PlayerDisconnectUpdate(player.getName()));
                    Server.getInstance().getConnection().addUpdateUnregisteredPlayers(new PlayerDisconnectUpdate(player.getName()));
                }
                else{
                    player.disconnect();
                    Match match = Server.getInstance().getLobby().getMatch(player);
                    if(match.getCurrentPlayer().getName().equals(player.getName())){
                        if(player.hasJustStarted()){
                            while (player.getPowerUpList().size() < 2){
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
                rmiWrapper.stop();
            }
            Server.getInstance().getConnection().removeConnection(token);
        }
        ping = false;
    }

    public synchronized void ping(){
        ping = true;
    }
}
