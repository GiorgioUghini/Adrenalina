package models.turn;

import org.omg.PortableInterceptor.ACTIVE;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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

    TurnState getState() {
        return stateBehaviour.getState();
    }

    Set<TurnEvent> getValidEvents() {
        return stateBehaviour.getValidEvents();
    }

    void run() {
        transition(TurnEvent.RUN);
    }

    void draw() {
        transition(TurnEvent.DRAW);
    }

    void grab() {
        transition(TurnEvent.GRAB);
    }

    void shoot() {
        transition(TurnEvent.SHOOT);
    }

    void spawn() {
        transition(TurnEvent.SPAWN);
    }

    void reload() {
        transition(TurnEvent.RELOAD);
    }

    void end() {
        transition(TurnEvent.END);
    }

    void transition(TurnEvent event) {
        TurnStateBehaviour nextBehaviour = stateBehaviour.transition(event);
        if (nextBehaviour == null)
            throw new RuntimeException("This move is not valid");
        else
            stateBehaviour = nextBehaviour;
    }
}

interface TurnStateBehaviour {
    public TurnStateBehaviour transition(TurnEvent event);

    public TurnState getState();

    public TurnType getTurnType();

    public ActionGroup getActionGroup();

    public Set<TurnEvent> getValidEvents();
}

class StartedStateBehaviour implements TurnStateBehaviour {

    private TurnType turnType;
    private ActionGroup actionGroup;

    StartedStateBehaviour(TurnType turnType, ActionGroup actionGroup) {
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

class FirstDrawnStateBehaviour implements TurnStateBehaviour {

    private TurnType turnType;
    private ActionGroup actionGroup;

    FirstDrawnStateBehaviour(TurnType turnType, ActionGroup actionGroup) {
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

class SecondDrawnStateBehaviour implements TurnStateBehaviour {

    private TurnType turnType;
    private ActionGroup actionGroup;

    SecondDrawnStateBehaviour(TurnType turnType, ActionGroup actionGroup) {
        this.turnType = turnType;
        this.actionGroup = actionGroup;
    }

    @Override
    public TurnStateBehaviour transition(TurnEvent event) {
        TurnStateBehaviour nextBehaviour = null;
        if (event == TurnEvent.SPAWN)
            nextBehaviour = new SpawnedStateBehaviour(turnType, actionGroup);
        return nextBehaviour;
    }

    @Override
    public Set<TurnEvent> getValidEvents() {
        return new HashSet<>(Arrays.asList(TurnEvent.SPAWN));
    }

    @Override
    public TurnState getState() {
        return TurnState.SECOND_DRAWN;
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

class SpawnedStateBehaviour implements TurnStateBehaviour {

    private TurnType turnType;
    private ActionGroup actionGroup;

    SpawnedStateBehaviour(TurnType turnType, ActionGroup actionGroup) {
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
        return TurnState.SPAWNED;
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

class FirstRunStateBehaviour implements TurnStateBehaviour {

    private TurnType turnType;
    private ActionGroup actionGroup;

    FirstRunStateBehaviour(TurnType turnType, ActionGroup actionGroup) {
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

class SecondRunStateBehaviour implements TurnStateBehaviour {

    private TurnType turnType;
    private ActionGroup actionGroup;

    SecondRunStateBehaviour(TurnType turnType, ActionGroup actionGroup) {
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

class ThirdRunStateBehaviour implements TurnStateBehaviour {

    private TurnType turnType;
    private ActionGroup actionGroup;

    ThirdRunStateBehaviour(TurnType turnType, ActionGroup actionGroup) {
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

class FourthRunStateBehaviour implements TurnStateBehaviour {

    private TurnType turnType;
    private ActionGroup actionGroup;

    FourthRunStateBehaviour(TurnType turnType, ActionGroup actionGroup) {
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

class GrabbedStateBehaviour implements TurnStateBehaviour {

    private TurnType turnType;
    private ActionGroup actionGroup;

    GrabbedStateBehaviour(TurnType turnType, ActionGroup actionGroup) {
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
        return TurnState.GRABBED;
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

class ShotStateBehaviour implements TurnStateBehaviour {

    private TurnType turnType;
    private ActionGroup actionGroup;

    ShotStateBehaviour(TurnType turnType, ActionGroup actionGroup) {
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
        return TurnState.SHOT;
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

class ReloadedStateBehaviour implements TurnStateBehaviour {

    private TurnType turnType;
    private ActionGroup actionGroup;

    ReloadedStateBehaviour(TurnType turnType, ActionGroup actionGroup) {
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

class EndedStateBehaviour implements TurnStateBehaviour {

    private TurnType turnType;
    private ActionGroup actionGroup;

    EndedStateBehaviour(TurnType turnType, ActionGroup actionGroup) {
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

enum TurnEvent {
    DRAW,
    SPAWN,
    RUN,
    GRAB,
    SHOOT,
    RELOAD,
    END
}

enum TurnState {
    STARTED,
    FIRST_DRAWN,
    SECOND_DRAWN,
    SPAWNED,
    FIRST_RUN,
    SECOND_RUN,
    THIRD_RUN,
    FOURTH_RUN,
    GRABBED,
    SHOT,
    RELOADED,
    ENDED
}

enum TurnType {
    START_GAME,
    RESPAWN,
    IN_GAME
}