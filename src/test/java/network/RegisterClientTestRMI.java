package network;

import models.Lobby;
import org.junit.Test;

import java.io.IOException;
import java.rmi.NotBoundException;

import static org.junit.Assert.*;

public class RegisterClientTestRMI {

    @Test
    public void registerClientRMI() throws IOException, InterruptedException, NotBoundException {
        /*
        final Server server = new Server(3000);
        new Thread(() -> {
            try {
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        final Client client = new Client("localhost", 3000);
        new Thread(() -> {
            try {
                client.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        String token = client.getRemoteMethods().registerPlayer("Giorgio");
        Lobby mainLobby = Lobby.getInstance();
        assertEquals("Giorgio", mainLobby.getPlayerWaiting().get(0).getName());
        assertEquals(1, mainLobby.getPlayerWaiting().size());
        assertEquals(64, token.length());
*/
    }
}
