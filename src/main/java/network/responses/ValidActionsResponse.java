package network.responses;

import models.turn.ActionType;
import models.turn.TurnEvent;
import network.Response;
import network.ResponseHandlerInterface;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ValidActionsResponse implements Response {

    public Map<ActionType, List<TurnEvent>> actions;

    public ValidActionsResponse(Map<ActionType, List<TurnEvent>> actions){
        this.actions = actions;
    }

    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}