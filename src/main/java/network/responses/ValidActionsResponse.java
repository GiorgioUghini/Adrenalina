package network.responses;

import network.Response;
import network.ResponseHandlerInterface;

import java.util.Set;

public class ValidActionsResponse implements Response {

    public Set actions;

    public ValidActionsResponse(Set actions){
        this.actions = actions;
    }

    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}