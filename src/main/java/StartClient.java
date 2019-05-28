import controllers.ResourceController;
import network.Client;
import utils.Constants;
import views.ViewType;

import java.io.File;

public class StartClient {
    public static void main(String[] args) throws InterruptedException {
        File lolo = ResourceController.getResource("img/map-1.png");
        ViewType viewType = ViewType.CLI;
        for (String s: args) {
            s = s.replace(Constants.ARG_PREFIX, "").toLowerCase();
            if (s.equals("gui")) {
                viewType = ViewType.GUI;
            }
        }
        Client client = Client.getInstance();
        client.start(viewType);
        Thread.currentThread().join();
    }
}
