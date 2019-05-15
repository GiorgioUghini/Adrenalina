import network.Client;
import utils.Constants;
import views.ViewType;

public class StartClient {
    public static void main(String[] args) throws InterruptedException {
        ViewType viewType = ViewType.CLI;
        args = new String[] {"gui"};    //I'M STARTING GUI!!!
        for (String s: args) {
            s = s.replace(Constants.ARG_PREFIX, "").toLowerCase();
            if (s.equals("gui")) {
                viewType = ViewType.GUI;
            }
        }
        Client client = Client.getInstance();
        client.start(viewType);
    }
}
