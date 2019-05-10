import network.Client;
import utils.Constants;
import views.ViewType;

import java.io.IOException;
import java.rmi.NotBoundException;

public class StartClient {
    public static void main(String[] args) throws IOException, NotBoundException, InterruptedException {
        ViewType viewType = ViewType.CLI;
        for (String s: args) {
            s = s.replace(Constants.ARG_PREFIX, "").toLowerCase();
            switch (s){
                case "gui":
                    viewType = ViewType.GUI;
                    break;
                case "cli":
                    viewType = ViewType.CLI;
            }
        }
        Client client = Client.createInstance(viewType);
        client.start();
    }
}
