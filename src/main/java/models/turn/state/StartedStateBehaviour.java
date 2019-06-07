package models.turn.state;

import models.turn.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StartedStateBehaviour implements TurnStateBehaviour {
    protected TurnType turnType;
    protected ActionGroup actionGroup;

    public StartedStateBehaviour(TurnType turnType, ActionGroup actionGroup) {
        this.turnType = turnType;
        this.actionGroup = actionGroup;
    }

    @Override
    public TurnStateBehaviour transition(TurnEvent event) {
        TurnStateBehaviour nextBehaviour = null;
        if (turnType == TurnType.IN_GAME) {
            if (event == TurnEvent.RUN)
                nextBehaviour = new FirstRunStateBehaviour(turnType, actionGroup);
            else if (event == TurnEvent.GRAB)
                nextBehaviour = new GrabbedStateBehaviour(turnType, actionGroup);
            else if (event == TurnEvent.SHOOT)
                nextBehaviour = new ShotStateBehaviour(turnType, actionGroup);
            else if (event == TurnEvent.END)
                nextBehaviour = new EndedStateBehaviour(turnType, actionGroup);
            else if ((actionGroup == ActionGroup.FRENZY_TYPE_1 || actionGroup == ActionGroup.FRENZY_TYPE_2) && event == TurnEvent.RELOAD)
                nextBehaviour = new EndedStateBehaviour(turnType, actionGroup);
        } else if (turnType == TurnType.START_GAME) {
            if (event == TurnEvent.DRAW)
                nextBehaviour = new FirstDrawnStateBehaviour(turnType, actionGroup);
        } else if (turnType == TurnType.RESPAWN) {
            if (event == TurnEvent.DRAW)
                nextBehaviour = new FirstDrawnStateBehaviour(turnType, actionGroup);
        }
        return nextBehaviour;
    }

    @Override
    public Set<TurnEvent> getValidEvents() {
        if (turnType == TurnType.IN_GAME) {
            if (actionGroup == ActionGroup.FRENZY_TYPE_1 || actionGroup == ActionGroup.FRENZY_TYPE_2) {
                return new HashSet<>(Arrays.asList(TurnEvent.RUN, TurnEvent.GRAB, TurnEvent.RELOAD, TurnEvent.SHOOT, TurnEvent.END));
            } else {
                return new HashSet<>(Arrays.asList(TurnEvent.RUN, TurnEvent.GRAB, TurnEvent.SHOOT, TurnEvent.END));
            }
        } else {
            return new HashSet<>(Arrays.asList(TurnEvent.DRAW));
        }
    }

    @Override
    public TurnState getState() {
        return TurnState.STARTED;
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
