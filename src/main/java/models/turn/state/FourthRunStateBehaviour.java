package models.turn.state;

import models.turn.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FourthRunStateBehaviour implements TurnStateBehaviour {
    protected TurnType turnType;
    protected ActionGroup actionGroup;

    public FourthRunStateBehaviour(TurnType turnType, ActionGroup actionGroup) {
        this.turnType = turnType;
        this.actionGroup = actionGroup;
    }

    @Override
    public TurnStateBehaviour transition(TurnEvent event) {
        TurnStateBehaviour nextBehaviour = null;
        if (event == TurnEvent.END)
            nextBehaviour = new EndedStateBehaviour(turnType, actionGroup);
        return nextBehaviour;
    }

    @Override
    public Set<TurnEvent> getValidEvents() {
        return new HashSet<>(Arrays.asList(TurnEvent.END));
    }

    @Override
    public TurnState getState() {
        return TurnState.FOURTH_RUN;
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