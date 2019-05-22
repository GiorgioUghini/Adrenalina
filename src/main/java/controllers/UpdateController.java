package controllers;

import models.player.Player;
import network.Response;
import network.Server;
import network.SocketWrapper;
import network.Update;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class UpdateController {
    public static void sendUpdate(Player player, Response update){
        try {
            SocketWrapper socketWrapper = Server.getInstance().getConnection().getSocket(player.getToken());
            ObjectOutputStream out = socketWrapper.getOutputStream();
            out.writeObject(update);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
