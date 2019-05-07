package network;

import config.Constants;
import models.Lobby;
import network.responses.RegisterPlayerResponse;
import org.junit.Test;

import java.io.IOException;
import java.rmi.NotBoundException;

import static org.junit.Assert.*;

public class RegisterClientTestRMI {

    @Test
    public void registerClientRMI() throws IOException, NotBoundException, InterruptedException {
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

        new Thread(() -> {
            try {
                Connection.getInstance().initRMI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(200);
        RegisterPlayerResponse res = Connection.getInstance().getRemoteMethods().registerPlayer("Furlan");
        res.handle(new ResponseHandler());
        Lobby mainLobby = Lobby.getInstance();
        assertEquals("Furlan", mainLobby.getPlayerWaiting().get(0).getName());
        assertEquals(1, mainLobby.getPlayerWaiting().size());
        assertEquals(64, mainLobby.getPlayerWaiting().get(0).getToken().length());
    }
}
