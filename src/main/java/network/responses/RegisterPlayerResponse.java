package network.responses;

import network.Response;
import network.ResponseHandlerInterface;

public class RegisterPlayerResponse implements Response {

    public RegisterPlayerResponse(){

    }

    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}