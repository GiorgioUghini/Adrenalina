package network;

import config.Constants;
import models.Lobby;
import network.requests.RegisterPlayerRequest;
import org.junit.Test;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.NotBoundException;

import static org.junit.Assert.*;

public class RegisterClientTestSocket {

    @Test
    public void registerClientSocket() throws IOException, InterruptedException, NotBoundException {
        final Server server = new Server(Constants.PORT);
        new Thread(() -> {
            try {
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        final Client client = new Client(Constants.HOSTNAME, Constants.PORT);
        new Thread(() -> {
            try {
                client.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        Connection.getIstance().initSocket(Constants.HOSTNAME, Constants.PORT);
        ObjectOutputStream out = Connection.getIstance().getOutputStream();
        RegisterPlayerRequest request = new RegisterPlayerRequest("Furlan");
        out.writeObject(request);
        Thread.sleep(250); //Do not remove

        Lobby mainLobby = Lobby.getInstance();
        assertEquals("Furlan", mainLobby.getPlayerWaiting().get(0).getName());
        assertEquals(1, mainLobby.getPlayerWaiting().size());
        assertEquals(64, mainLobby.getPlayerWaiting().get(0).getToken().length());
    }

}
