package network.responses;

import network.Response;
import network.ResponseHandlerInterface;

public class ReconnectPlayerResponse implements Response {
    public ReconnectPlayerResponse(){

    }

    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}