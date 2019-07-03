package network;

import models.Match;
import models.card.PowerUpCard;
import models.map.GameMap;
import models.map.SpawnPoint;
import models.player.Player;
import network.updates.MapUpdate;
import network.updates.PlayerDisconnectUpdate;
import utils.Console;
import utils.DisconnectionHandler;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ClientListener implements Runnable{

    private ObjectInputStream in;
    private RequestHandlerInterface requestHandler;
    private SocketWrapper socketWrapper;
    private boolean stop;

    ClientListener(SocketWrapper socketWrapper) throws IOException {
        this.socketWrapper = socketWrapper;
        this.in = socketWrapper.getInputStream();
        this.requestHandler = new RequestHandler();
        this.stop = false;
    }

    @Override
    public void run() {
        String token = Server.getInstance().getConnection().getToken(socketWrapper);
        try {
            while (!stop){
                Request request = (Request) in.readObject();
                request.setToken(token);
                Response response = request.handle(requestHandler);
                socketWrapper.write(response);
            }
        } catch (Exception socketEx) {
            Console.println("Socket " + token + " disconnected!");
            Player player = Server.getInstance().getLobby().getPlayer(token);
            if(player != null){
                socketWrapper.stop();
                Server.getInstance().getConnection().addUpdateUnregisteredPlayers(new PlayerDisconnectUpdate(player.getName()));
                if(player.isWaiting()){
                    Server.getInstance().getLobby().addUpdateWaitingPlayers(new PlayerDisconnectUpdate(player.getName()));
                    Server.getInstance().getLobby().disconnectPlayer(player);
                }
                else{
                    DisconnectionHandler.handle(player);
                }
            }
            Server.getInstance().getConnection().removeConnection(token);
        }
    }

    public void stop(){
        stop = true;
    }
}
