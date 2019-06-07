package network.responses;

import network.Response;
import network.ResponseHandlerInterface;

public class EndTurnResponse implements Response {
    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}
