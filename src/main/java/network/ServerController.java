package network;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerController implements RequestHandler {

    private RemoteMethods remoteMethods;

    public ServerController() throws RemoteException {
        remoteMethods = new RemoteMethods();
    }

    @Override
    public Response handle(LongPollingRequest request) {
        return null;
    }

    @Override
    public Response handle(RegisterPlayerRequest request) {
        String token = null;
        try {
            token = remoteMethods.registerPlayer(request.username);
        } catch (RemoteException e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.SEVERE, "an exception was thrown", e);
        }
        return new RegisterPlayerResponse(token);
    }
}
