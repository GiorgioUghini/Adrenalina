import network.Client;
import utils.Constants;
import views.ViewType;


public class StartClient {
    public static void main(String[] args) throws InterruptedException {
        ViewType viewType = ViewType.CLI;
        String hostname = Constants.HOSTNAME;
        int socketPort = Constants.PORT;
        int registryPort = Constants.REGISTRY_PORT;
        boolean debug = false;
        for (String s : args) {
            s = s.replace(Constants.ARG_PREFIX, "").toLowerCase();
            if (s.equals("gui")) {
                viewType = ViewType.GUI;
            } else if (s.equals("debug")) {
                debug = true;
            } else if (s.startsWith("hostname")) {
                hostname = s.replace("hostname=", "");
            } else if (s.startsWith("socketport")) {
                socketPort = Integer.parseInt(s.replace("socketport=", ""));
            } else if (s.startsWith("registryport")) {
                registryPort = Integer.parseInt(s.replace("registryport=", ""));
            }
        }
        Client client = Client.getInstance();
        client.setDebug(debug);
        client.start(viewType, hostname, socketPort, registryPort);
        Thread.currentThread().join();
    }
}
