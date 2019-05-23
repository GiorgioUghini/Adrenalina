package network;

import javafx.application.Platform;
import models.player.Player;

import java.util.TimerTask;

public class RMIStatusTask extends TimerTask {

    private boolean ping;
    private Player player;

    RMIStatusTask(Player player){
        this.ping = true;
        this.player = player;
    }

    @Override
    public void run() {
        if(!ping){
            player.disconnect();
        }
        ping = false;
    }

    public void ping(){
        ping = true;
    }
}
