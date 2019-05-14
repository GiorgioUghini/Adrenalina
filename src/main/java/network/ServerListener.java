package network;

import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerListener implements Runnable{

    private ResponseHandlerInterface responseHandler;
    private ObjectInputStream in;

    public ServerListener() {
        this.responseHandler = new ResponseHandler();
        this.in = ((SocketConnection) Client.getInstance().getConnection()).getInputStream();
    }

    @Override
    public void run() {
        try {
            Response response = (Response) in.readObject();
            response.handle(responseHandler);
        } catch (Exception e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.SEVERE, "an exception was thrown", e);
        }
    }
}
