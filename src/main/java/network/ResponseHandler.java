package network;

import network.responses.LongPollingResponse;
import network.responses.RegisterPlayerResponse;

public class ResponseHandler implements ResponseHandlerInterface {
    @Override
    public void handle(RegisterPlayerResponse response) {
        // TODO store auth token
        //System.out.println(response.token); // Placeholder
    }

    @Override
    public void handle(LongPollingResponse response) {

    }
}
