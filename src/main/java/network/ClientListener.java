package network;

import models.Match;
import models.player.Player;
import network.updates.PlayerDisconnectUpdate;
import utils.Console;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
                    player.disconnect();
                    Match match = Server.getInstance().getLobby().getMatch(player);
                    if(match.getCurrentPlayer().getName().equals(player.getName())){
                        match.nextTurn();
                    }
                    match.addUpdate(new PlayerDisconnectUpdate(player.getName()));
                }
            }
            Server.getInstance().getConnection().removeConnection(token);
        }
    }

    public void stop(){
        stop = true;
    }
}
