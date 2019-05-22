package controllers;

import models.player.Player;
import network.Server;
import network.SocketWrapper;

import java.net.Socket;
import java.util.List;

public class LobbyController {

    public void startGame(List<Player> players){
        for(Player p : players){
            SocketWrapper socketWrapper = Server.getInstance().getConnection().getSocket(p.getToken());

        }
    }

}
