package models.turn.state;

import models.turn.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ThirdRunStateBehaviour implements TurnStateBehaviour {
    protected TurnType turnType;
    protected ActionGroup actionGroup;

    public ThirdRunStateBehaviour(TurnType turnType, ActionGroup actionGroup) {
        this.turnType = turnType;
        this.actionGroup = actionGroup;
    }

    @Override
    public TurnStateBehaviour transition(TurnEvent event) {
        TurnStateBehaviour nextBehaviour = null;
        if (turnType == TurnType.IN_GAME) {
            if (event == TurnEvent.END)
                nextBehaviour = new EndedStateBehaviour(turnType, actionGroup);
            if (actionGroup == ActionGroup.FRENZY_TYPE_1) {
                if (event == TurnEvent.RUN)
                    nextBehaviour = new FourthRunStateBehaviour(turnType, actionGroup);
            } else if (actionGroup == ActionGroup.FRENZY_TYPE_2) {
                if (event == TurnEvent.GRAB)
                    nextBehaviour = new GrabbedStateBehaviour(turnType, actionGroup);
            }
        }
        return nextBehaviour;
    }

    @Override
    public Set<TurnEvent> getValidEvents() {
        if (actionGroup == ActionGroup.FRENZY_TYPE_1) {
            return new HashSet<>(Arrays.asList(TurnEvent.RUN, TurnEvent.END));
        } else if (actionGroup == ActionGroup.FRENZY_TYPE_2) {
            return new HashSet<>(Arrays.asList(TurnEvent.GRAB, TurnEvent.END));
        } else {
            return new HashSet<>(Arrays.asList(TurnEvent.END, TurnEvent.END));
        }
    }

    @Override
    public TurnState getState() {
        return TurnState.THIRD_RUN;
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
