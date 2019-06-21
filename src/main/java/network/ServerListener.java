package network;

import controllers.ScreenController;

import java.io.ObjectInputStream;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerListener implements Runnable{

    private ObjectInputStream in;
    private boolean stop;

    ServerListener() {
        this.in = ((SocketConnection) Client.getInstance().getConnection()).getInputStream();
        this.stop = false;
    }

    @Override
    public void run() {
        Response response = null;
        while(!stop){
            try {
                response = (Response) in.readObject();
                Client.getInstance().getConnection().receiveResponse(response);
            } catch (SocketException socketEx) {
                Logger logger = Logger.getAnonymousLogger();
                logger.log(Level.SEVERE, "a socket exception was thrown", socketEx);
                stop();
                ScreenController.getInstance().activate("WaitingRoom.fxml");
            } catch (Exception ex) {
                Logger logger = Logger.getAnonymousLogger();
                logger.log(Level.SEVERE, "an unknown exception was thrown", ex);
            }
        }
    }

    public void stop(){
        stop = true;
    }
}
