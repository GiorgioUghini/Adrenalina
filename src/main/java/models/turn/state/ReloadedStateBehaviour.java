package models.turn.state;

import models.turn.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ReloadedStateBehaviour implements TurnStateBehaviour {
    protected TurnType turnType;
    protected ActionGroup actionGroup;

    public ReloadedStateBehaviour(TurnType turnType, ActionGroup actionGroup) {
        this.turnType = turnType;
        this.actionGroup = actionGroup;
    }

    @Override
    public TurnStateBehaviour transition(TurnEvent event) {
        TurnStateBehaviour nextBehaviour = null;
        if (event == TurnEvent.END)
            nextBehaviour = new EndedStateBehaviour(turnType, actionGroup);
        if ((actionGroup == ActionGroup.FRENZY_TYPE_1 || actionGroup == ActionGroup.FRENZY_TYPE_2) && event == TurnEvent.SHOOT)
            nextBehaviour = new ShotStateBehaviour(turnType, actionGroup);
        return nextBehaviour;
    }

    @Override
    public Set<TurnEvent> getValidEvents() {
        return new HashSet<>(Arrays.asList(TurnEvent.SHOOT, TurnEvent.END));
    }

    @Override
    public TurnState getState() {
        return TurnState.RELOADED;
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
