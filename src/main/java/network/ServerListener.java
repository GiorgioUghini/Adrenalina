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
        Response response = null;
        while(!stop){
            try {
                response = (Response) in.readObject();
            } catch (Exception ex) {
                Logger logger = Logger.getAnonymousLogger();
                logger.log(Level.SEVERE, "an exception was thrown while in.readObject(). The connection has been killed", ex);
                stop();
                ScreenController.getInstance().activate("WaitingRoom.fxml");
            }
            try{
                if(response != null)
                    Client.getInstance().getConnection().receiveResponse(response);
            }
            catch (Exception ex){
                Logger logger = Logger.getAnonymousLogger();
                logger.log(Level.SEVERE, "an exception was thrown while receiveResponse(response). The connection has not been killed", ex);
            }
        }
    }

    public void stop(){
        stop = true;
    }
}
