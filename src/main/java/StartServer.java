import network.Server;
import utils.Constants;

import java.io.IOException;

public class StartServer {
    public static void main(String[] args) throws IOException {
        Server server = Server.getInstance();
        int registryPort = Constants.REGISTRY_PORT;
        int socketPort = Constants.PORT;
        String hostname = Constants.HOSTNAME;
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
            else if(s.startsWith("hostname")){
                hostname = s.replace("hostname=", "");
            }
            else if(s.startsWith("socketport")){
                socketPort = Integer.parseInt(s.replace("socketport=", ""));
            }
            else if(s.startsWith("registryport")){
                registryPort = Integer.parseInt(s.replace("registryport=", ""));
            }
        }
        server.setMaxClients(maxClients);
        server.start(hostname, socketPort, registryPort);
    }
}