import network.Server;

import java.io.IOException;

public class StartServer {
    public static void main(String[] args) throws IOException {
        Server server = Server.getInstance();
        server.start();
    }
}