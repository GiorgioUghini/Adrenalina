package network.responses;

import models.turn.ActionType;
import models.turn.TurnEvent;
import network.Response;
import network.ResponseHandlerInterface;

import java.util.List;
import java.util.Map;

public class ValidActionsResponse implements Response {

    public Map<ActionType, List<TurnEvent>> actions;
    public boolean newActions;

    public ValidActionsResponse(Map<ActionType, List<TurnEvent>> actions, boolean newActions){
        this.actions = actions;
        this.newActions = newActions;
    }

    public ValidActionsResponse(Map<ActionType, List<TurnEvent>> actions){
        this.actions = actions;
        this.newActions = false;
    }

    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}