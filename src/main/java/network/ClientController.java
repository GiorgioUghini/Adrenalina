package network;

public class ClientController implements ResponseHandler {
    @Override
    public void handle(RegisterPlayerResponse response) {
        // TODO store auth token
        System.out.println(response.token); // Placeholder
    }
}
