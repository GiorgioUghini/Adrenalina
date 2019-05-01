package network;

import org.junit.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class RegisterClientTest {

    @Test
    public void registerClient() throws IOException, InterruptedException, ClassNotFoundException {
        final Server server = new Server(3000);
        new Thread(() -> {
            try {
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        Client client = new Client("localhost", 3000);
        client.start();
        ObjectOutputStream out = client.getOutputStream();
        RegisterPlayerRequest request = new RegisterPlayerRequest("Furlan");
        out.writeObject(request);
        Thread.sleep(2000);
    }
}
