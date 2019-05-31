package network;

import models.Match;
import models.player.Player;
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
