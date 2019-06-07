package models.turn.state;

import models.turn.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SecondRunStateBehaviour implements TurnStateBehaviour {
    protected TurnType turnType;
    protected ActionGroup actionGroup;

    public SecondRunStateBehaviour(TurnType turnType, ActionGroup actionGroup) {
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
                nextBehaviour = new ThirdRunStateBehaviour(turnType, actionGroup);
            else if ((actionGroup == ActionGroup.LOW_LIFE || actionGroup == ActionGroup.VERY_LOW_LIFE || actionGroup == ActionGroup.FRENZY_TYPE_1) && event == TurnEvent.GRAB)
                nextBehaviour = new GrabbedStateBehaviour(turnType, actionGroup);
            else if (actionGroup == ActionGroup.FRENZY_TYPE_2 && event == TurnEvent.RELOAD)
                nextBehaviour = new ReloadedStateBehaviour(turnType, actionGroup);
            else if ((actionGroup == ActionGroup.FRENZY_TYPE_1 || actionGroup == ActionGroup.FRENZY_TYPE_2) && event == TurnEvent.SHOOT)
                nextBehaviour = new ShotStateBehaviour(turnType, actionGroup);
        }
        return nextBehaviour;
    }

    @Override
    public Set<TurnEvent> getValidEvents() {
        if (actionGroup == ActionGroup.LOW_LIFE || actionGroup == ActionGroup.VERY_LOW_LIFE || actionGroup == ActionGroup.FRENZY_TYPE_1) {
            return new HashSet<>(Arrays.asList(TurnEvent.RUN, TurnEvent.GRAB, TurnEvent.END));
        } else if (actionGroup == ActionGroup.FRENZY_TYPE_2) {
            return new HashSet<>(Arrays.asList(TurnEvent.RUN, TurnEvent.GRAB, TurnEvent.RELOAD, TurnEvent.SHOOT, TurnEvent.END));
        } else {
            return new HashSet<>(Arrays.asList(TurnEvent.RUN, TurnEvent.END));
        }
    }

    @Override
    public TurnState getState() {
        return TurnState.SECOND_RUN;
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
