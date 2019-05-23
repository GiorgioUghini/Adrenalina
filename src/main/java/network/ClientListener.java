package network;

import models.Match;
import models.player.Player;
import network.updates.PlayerDisconnectUpdate;
import utils.Console;
import utils.TokenGenerator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientListener implements Runnable{

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private RequestHandlerInterface requestHandler;
    private String token;
    private SocketWrapper socketWrapper;
    private boolean stop;

    public ClientListener(SocketWrapper socketWrapper) throws IOException {
        this.socketWrapper = socketWrapper;
        this.token = TokenGenerator.nextToken();
        Server.getInstance().getConnection().addSocket(token, socketWrapper);
        this.in = socketWrapper.getInputStream();
        this.requestHandler = new RequestHandler();
        this.stop = false;
    }

    @Override
    public void run() {
        try {
            while (!stop){
                Request request = (Request) in.readObject();
                request.setToken(token);
                Response response = request.handle(requestHandler);
                socketWrapper.write(response);
            }
        } catch (SocketException socketEx) {
            Console.println("Socket " + token + " disconnected!");
            socketWrapper.stopUpdatePusher();
            Player player = Server.getInstance().getLobby().getPlayer(token);
            Match match = Server.getInstance().getLobby().getMatch(player);
            Server.getInstance().getConnection().removeSocket(token);
            match.addUpdate(new PlayerDisconnectUpdate(player));
        }
        catch (Exception ex){
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.SEVERE, "an exception was thrown", ex);
        }
    }

    public void stop(){
        stop = true;
    }
}
