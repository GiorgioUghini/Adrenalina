package models.turn;

import models.turn.state.StartedStateBehaviour;
import models.turn.state.TurnStateBehaviour;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TurnEngine {

    private TurnType turnType;
    private ActionGroup actionGroup;
    private TurnStateBehaviour stateBehaviour;

    public TurnEngine(TurnType turnType, ActionGroup actionGroup) {
        this.turnType = turnType;
        this.actionGroup = actionGroup;
        this.stateBehaviour = new StartedStateBehaviour(turnType, actionGroup);
    }

    public TurnEngine(TurnStateBehaviour state) {
        this.turnType = state.getTurnType();
        this.actionGroup = state.getActionGroup();
        this.stateBehaviour = state;

    }

    public TurnState getState() {
        return stateBehaviour.getState();
    }

    public Set<TurnEvent> getValidEvents() {
        return stateBehaviour.getValidEvents();
    }

    public void run() {
        transition(TurnEvent.RUN);
    }

    public void draw() {
        transition(TurnEvent.DRAW);
    }

    public void grab() {
        transition(TurnEvent.GRAB);
    }

    public void shoot() {
        transition(TurnEvent.SHOOT);
    }

    public void spawn() {
        transition(TurnEvent.SPAWN);
    }

    public void reload() {
        transition(TurnEvent.RELOAD);
    }

    public void end() {
        transition(TurnEvent.END);
    }

    public void transition(TurnEvent event) {
        TurnStateBehaviour nextBehaviour = stateBehaviour.transition(event);
        if (nextBehaviour == null)
            throw new RuntimeException("This move is not valid");
        else
            stateBehaviour = nextBehaviour;
    }
}