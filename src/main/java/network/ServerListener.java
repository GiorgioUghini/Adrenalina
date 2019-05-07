package network;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerListener implements Runnable{

    private ResponseHandlerInterface responseHandler;

    public ServerListener() {
        this.responseHandler = new ResponseHandler();
    }

    @Override
    public void run() {
        try {
            Response response = (Response) Connection.getInstance().getInputStream().readObject();
            response.handle(responseHandler);
        } catch (Exception e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.SEVERE, "an exception was thrown", e);
        }
    }
}
