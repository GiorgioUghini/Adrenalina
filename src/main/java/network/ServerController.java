package network;

import java.rmi.RemoteException;

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
            e.printStackTrace();
        }
        RegisterPlayerResponse response = new RegisterPlayerResponse(token);
        return response;
    }
}
