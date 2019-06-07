package models.turn.state;

import models.turn.*;

import java.util.HashSet;
import java.util.Set;

public class EndedStateBehaviour implements TurnStateBehaviour {
    protected TurnType turnType;
    protected ActionGroup actionGroup;

    public EndedStateBehaviour(TurnType turnType, ActionGroup actionGroup) {
        this.turnType = turnType;
        this.actionGroup = actionGroup;
    }

    @Override
    public TurnStateBehaviour transition(TurnEvent event) {
        return null;
    }

    @Override
    public Set<TurnEvent> getValidEvents() {
        return new HashSet<>();
    }

    @Override
    public TurnState getState() {
        return TurnState.ENDED;
    }

    @Override
    public TurnType getTurnType() {
        return turnType;
    }

    @Override
    public ActionGroup getActionGroup() {
        return actionGroup;
    }
}
