import network.Client;
import utils.Constants;
import views.ViewType;


public class StartClient {
    public static void main(String[] args) throws InterruptedException {
        ViewType viewType = ViewType.CLI;
        boolean debug = false;
        for (String s: args) {
            s = s.replace(Constants.ARG_PREFIX, "").toLowerCase();
            if (s.equals("gui")) {
                viewType = ViewType.GUI;
            }
            else if(s.equals("debug")){
                debug = true;
            }
        }
        Client client = Client.getInstance();
        client.setDebug(debug);
        client.start(viewType);
        Thread.currentThread().join();
    }
}
