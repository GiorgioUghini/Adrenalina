package models.turn.state;

import models.turn.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FirstDrawnStateBehaviour implements TurnStateBehaviour {
    protected TurnType turnType;
    protected ActionGroup actionGroup;

    public FirstDrawnStateBehaviour(TurnType turnType, ActionGroup actionGroup) {
        this.turnType = turnType;
        this.actionGroup = actionGroup;
    }

    @Override
    public TurnStateBehaviour transition(TurnEvent event) {
        TurnStateBehaviour nextBehaviour = null;
        if (turnType == TurnType.START_GAME) {
            if (event == TurnEvent.DRAW)
                nextBehaviour = new SecondDrawnStateBehaviour(turnType, actionGroup);
        } else if (turnType == TurnType.RESPAWN)
            if (event == TurnEvent.SPAWN)
                nextBehaviour = new SpawnedStateBehaviour(turnType, actionGroup);
        return nextBehaviour;
    }

    @Override
    public Set<TurnEvent> getValidEvents() {
        if (turnType == TurnType.START_GAME) {
            return new HashSet<>(Arrays.asList(TurnEvent.DRAW));
        } else if (turnType == TurnType.RESPAWN) {
            return new HashSet<>(Arrays.asList(TurnEvent.SPAWN));
        } else {
            return new HashSet<>();
        }
    }

    @Override
    public TurnState getState() {
        return TurnState.FIRST_DRAWN;
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
