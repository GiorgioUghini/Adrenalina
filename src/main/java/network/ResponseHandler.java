package network;

import network.responses.LongPollingResponse;
import network.responses.RegisterPlayerResponse;

public class ResponseHandler implements ResponseHandlerInterface {
    @Override
    public void handle(RegisterPlayerResponse response) {
        Connection.getInstance().setToken(response.token);
    }

    @Override
    public void handle(LongPollingResponse response) {

    }
}
