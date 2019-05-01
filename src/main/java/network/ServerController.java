package network;

public class ServerController implements RequestHandler {

    private RemoteMethods remoteMethods;

    public ServerController(){
        remoteMethods = new RemoteMethods();
    }

    @Override
    public Response handle(LongPollingRequest request) {
        return null;
    }

    @Override
    public Response handle(RegisterPlayerRequest request) {
        String token = remoteMethods.registerPlayer(request.username);
        RegisterPlayerResponse response = new RegisterPlayerResponse(token);
        return response;
    }
}
