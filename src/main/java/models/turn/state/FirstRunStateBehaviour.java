package models.turn.state;

import models.turn.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FirstRunStateBehaviour implements TurnStateBehaviour {
    protected TurnType turnType;
    protected ActionGroup actionGroup;

    public FirstRunStateBehaviour(TurnType turnType, ActionGroup actionGroup) {
        this.turnType = turnType;
        this.actionGroup = actionGroup;
    }

    @Override
    public TurnStateBehaviour transition(TurnEvent event) {
        TurnStateBehaviour nextBehaviour = null;
        if (turnType == TurnType.IN_GAME) {
            if (event == TurnEvent.END)
                nextBehaviour = new EndedStateBehaviour(turnType, actionGroup);
            else if (event == TurnEvent.RUN)
                nextBehaviour = new SecondRunStateBehaviour(turnType, actionGroup);
            else if (event == TurnEvent.GRAB)
                nextBehaviour = new GrabbedStateBehaviour(turnType, actionGroup);
            else if (actionGroup == ActionGroup.VERY_LOW_LIFE && event == TurnEvent.SHOOT)
                nextBehaviour = new ShotStateBehaviour(turnType, actionGroup);
            else if ((actionGroup == ActionGroup.FRENZY_TYPE_1 || actionGroup == ActionGroup.FRENZY_TYPE_2) && event == TurnEvent.RELOAD)
                nextBehaviour = new ReloadedStateBehaviour(turnType, actionGroup);
        }
        return nextBehaviour;
    }

    @Override
    public Set<TurnEvent> getValidEvents() {
        if (actionGroup == ActionGroup.VERY_LOW_LIFE) {
            return new HashSet<>(Arrays.asList(TurnEvent.RUN, TurnEvent.GRAB, TurnEvent.SHOOT, TurnEvent.END));
        } else if (actionGroup == ActionGroup.FRENZY_TYPE_1 || actionGroup == ActionGroup.FRENZY_TYPE_2) {
            return new HashSet<>(Arrays.asList(TurnEvent.RUN, TurnEvent.GRAB, TurnEvent.RELOAD, TurnEvent.SHOOT, TurnEvent.END));
        } else {
            return new HashSet<>(Arrays.asList(TurnEvent.RUN, TurnEvent.GRAB, TurnEvent.END));
        }
    }

    @Override
    public TurnState getState() {
        return TurnState.FIRST_RUN;
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
