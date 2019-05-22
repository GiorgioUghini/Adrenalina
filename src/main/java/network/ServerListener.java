package network;

import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerListener implements Runnable{

    private ObjectInputStream in;

    ServerListener() {
        this.in = ((SocketConnection) Client.getInstance().getConnection()).getInputStream();
    }

    @Override
    public void run() {
        try {
            Response response = null;
            while(true){
                response = (Response) in.readObject();
                Client.getInstance().getConnection().receiveResponse(response);
            }
        } catch (Exception e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.SEVERE, "an exception was thrown", e);
        }
    }
}
