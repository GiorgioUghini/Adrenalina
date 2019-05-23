package network.responses;

import models.turn.ActionElement;
import models.turn.ActionGroup;
import network.Response;
import network.ResponseHandlerInterface;

import java.util.List;
import java.util.Set;

public class ValidActionsResponse implements Response {

    public Set<List<ActionElement>> actions;

    public ValidActionsResponse(Set actions){
        this.actions = actions;
    }

    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}