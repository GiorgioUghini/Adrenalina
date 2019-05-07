package network.responses;

import network.Response;
import network.ResponseHandlerInterface;

public class RegisterPlayerResponse implements Response {

    public String token;

    public RegisterPlayerResponse(String token){
        this.token = token;
    }

    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}