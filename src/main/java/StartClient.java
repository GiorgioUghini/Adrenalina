import models.turn.Turn;
import network.Client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StartClient {
    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", 3000);
        client.start();
    }
}
