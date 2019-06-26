package models.turn;

import java.util.*;

public class TurnEngine {

    public Map<ActionType,  List<TurnEvent>> getValidActions(TurnType turnType, ActionGroup actionGroup){
        Map<ActionType,  List<TurnEvent>> map = new HashMap<>();
        switch (turnType){
            case START_GAME:
                map.put(ActionType.START, new LinkedList<>(Arrays.asList(TurnEvent.DRAW, TurnEvent.DRAW, TurnEvent.SPAWN)));
                break;
            case RESPAWN:
                map.put(ActionType.START, new LinkedList<>(Arrays.asList(TurnEvent.DRAW, TurnEvent.SPAWN)));
                break;
            case IN_GAME:
                switch (actionGroup){
                    case NORMAL:
                        map.put(ActionType.RUN_NORMAL, new LinkedList<>(Collections.singletonList(TurnEvent.RUN_3)));
                        map.put(ActionType.GRAB_NORMAL, new LinkedList<>(Arrays.asList(TurnEvent.RUN_1, TurnEvent.GRAB)));
                        map.put(ActionType.SHOOT_NORMAL, new LinkedList<>(Collections.singletonList(TurnEvent.SHOOT)));
                        break;
                    case LOW_LIFE:
                        map.put(ActionType.RUN_NORMAL, new LinkedList<>(Collections.singletonList(TurnEvent.RUN_3)));
                        map.put(ActionType.GRAB_LOW_LIFE, new LinkedList<>(Arrays.asList(TurnEvent.RUN_2, TurnEvent.GRAB)));
                        map.put(ActionType.SHOOT_NORMAL, new LinkedList<>(Collections.singletonList(TurnEvent.SHOOT)));
                        break;
                    case VERY_LOW_LIFE:
                        map.put(ActionType.RUN_NORMAL, new LinkedList<>(Collections.singletonList(TurnEvent.RUN_3)));
                        map.put(ActionType.GRAB_LOW_LIFE, new LinkedList<>(Arrays.asList(TurnEvent.RUN_2, TurnEvent.GRAB)));
                        map.put(ActionType.SHOOT_VERY_LOW_LIFE, new LinkedList<>(Arrays.asList(TurnEvent.RUN_1, TurnEvent.SHOOT)));
                        break;
                    case FRENZY_TYPE_1:
                        map.put(ActionType.SHOOT_FRENZY_1, new LinkedList<>(Arrays.asList(TurnEvent.RUN_1, TurnEvent.RELOAD, TurnEvent.SHOOT)));
                        map.put(ActionType.GRAB_FRENZY_1, new LinkedList<>(Arrays.asList(TurnEvent.RUN_2, TurnEvent.GRAB)));
                        map.put(ActionType.RUN_FRENZY_1, new LinkedList<>(Collections.singletonList(TurnEvent.RUN_4)));
                        break;
                    case FRENZY_TYPE_2:
                        map.put(ActionType.SHOOT_FRENZY_2, new LinkedList<>(Arrays.asList(TurnEvent.RUN_2, TurnEvent.RELOAD, TurnEvent.SHOOT)));
                        map.put(ActionType.GRAB_FRENZY_2, new LinkedList<>(Arrays.asList(TurnEvent.RUN_3, TurnEvent.GRAB)));
                        break;
                        default:
                }
                break;
        }
        return  map;
    }
}
