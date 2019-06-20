package network;

import controllers.ScreenController;

import java.io.ObjectInputStream;
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
        try {
            Response response = null;
            while(!stop){
                response = (Response) in.readObject();
                Client.getInstance().getConnection().receiveResponse(response);
            }
        } catch (Exception e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.SEVERE, "an exception was thrown", e);
            ScreenController.getInstance().activate("WaitingRoom.fxml");
        }
    }

    public void stop(){
        stop = true;
    }
}
