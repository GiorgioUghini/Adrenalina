package models.turn.state;

import models.turn.ActionGroup;
import models.turn.TurnEvent;
import models.turn.TurnState;
import models.turn.TurnType;

import java.util.Set;

public interface TurnStateBehaviour {
    TurnStateBehaviour transition(TurnEvent event);

    TurnState getState();

    TurnType getTurnType();

    ActionGroup getActionGroup();

    Set<TurnEvent> getValidEvents();
}
