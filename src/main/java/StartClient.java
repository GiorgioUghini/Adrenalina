import errors.NotImplementedException;
import network.Client;
import utils.Constants;
import views.LobbyView;
import views.LobbyViewCLI;
import views.ViewType;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Scanner;

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
        Client client = new Client(viewType);
        client.start();
    }
}
