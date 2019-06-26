import network.Server;
import utils.Constants;

import java.io.IOException;

public class StartServer {
    public static void main(String[] args) throws IOException {
        Server server = Server.getInstance();

        int maxClients = 5;
        for (String s: args) {
            s = s.replace(Constants.ARG_PREFIX, "").toLowerCase();
            if(s.startsWith("max-clients")){
                maxClients = Integer.parseInt(s.replace("max-clients=", ""));
            }else if(s.equals("debug")){
                server.setDebug(true);
            }else if(s.equals("autoreload")){
                server.setAutoReload(true);
            }
        }
        server.setMaxClients(maxClients);
        server.start();
    }
}