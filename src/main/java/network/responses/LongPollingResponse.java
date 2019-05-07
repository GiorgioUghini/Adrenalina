package network.responses;

import network.Response;
import network.ResponseHandlerInterface;

public class LongPollingResponse implements Response {

    public LongPollingResponse(){

    }

    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}