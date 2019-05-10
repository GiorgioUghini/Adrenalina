package network;

import network.requests.LongPollingRequest;
import network.requests.RegisterPlayerRequest;
import network.responses.ErrorResponse;
import network.responses.RegisterPlayerResponse;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestHandler implements RequestHandlerInterface {

    private RemoteMethods remoteMethods;

    public RequestHandler() throws RemoteException {
        remoteMethods = new RemoteMethods();
    }

    @Override
    public Response handle(LongPollingRequest request) {
        return null;
    }

    @Override
    public Response handle(RegisterPlayerRequest request) throws RemoteException {
        return remoteMethods.registerPlayer(request.username);
    }
}
