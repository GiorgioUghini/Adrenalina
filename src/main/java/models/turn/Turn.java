package models.turn;

import java.util.*;

public class Turn {
    HashMap<ActionGroup, HashSet<LinkedList<ActionGroup>>> compositions = new HashMap<>();

    public Turn() {
        LinkedList actionGroupRunNormal = new LinkedList<>(Arrays.asList(ActionElement.RUN, ActionElement.RUN));
        LinkedList actionGroupGrabNormal = new LinkedList<>(Arrays.asList(ActionElement.RUN, ActionElement.GRAB));
        LinkedList actionGroupShootNormal = new LinkedList<>(Arrays.asList(ActionElement.SHOOT));

        LinkedList actionGroupGrabLowLife = new LinkedList<>(Arrays.asList(ActionElement.RUN, ActionElement.RUN, ActionElement.GRAB));

        LinkedList actionGroupShootVeryLowLife = new LinkedList<>(Arrays.asList(ActionElement.RUN, ActionElement.SHOOT));

        LinkedList actionGroupRunFrenzyType1 = new LinkedList<>(Arrays.asList(ActionElement.RUN, ActionElement.RUN, ActionElement.RUN, ActionElement.RUN));
        LinkedList actionGroupShootFrenzyType1 = new LinkedList<>(Arrays.asList(ActionElement.RUN, ActionElement.RELOAD, ActionElement.SHOOT));
        LinkedList actionGroupGrabFrenzyType1 = new LinkedList<>(Arrays.asList(ActionElement.RUN, ActionElement.RUN, ActionElement.GRAB));

        LinkedList actionGroupShootFrenzyType2 = new LinkedList<>(Arrays.asList(ActionElement.RUN, ActionElement.RUN, ActionElement.RELOAD, ActionElement.SHOOT));
        LinkedList actionGroupGrabFrenzyType2 = new LinkedList<>(Arrays.asList(ActionElement.RUN, ActionElement.RUN, ActionElement.RUN, ActionElement.GRAB));

        compositions.put(ActionGroup.NORMAL, new HashSet(Arrays.asList(actionGroupRunNormal, actionGroupGrabNormal, actionGroupShootNormal)));
        compositions.put(ActionGroup.LOW_LIFE, new HashSet(Arrays.asList(actionGroupGrabLowLife, actionGroupGrabNormal, actionGroupShootNormal)));
        compositions.put(ActionGroup.VERY_LOW_LIFE, new HashSet(Arrays.asList(actionGroupGrabLowLife, actionGroupGrabNormal, actionGroupShootVeryLowLife)));
        compositions.put(ActionGroup.FRENZY_TYPE_1, new HashSet(Arrays.asList(actionGroupRunFrenzyType1, actionGroupGrabFrenzyType1, actionGroupShootFrenzyType1)));
        compositions.put(ActionGroup.FRENZY_TYPE_2, new HashSet(Arrays.asList(actionGroupGrabFrenzyType2, actionGroupShootFrenzyType2)));
    }

    public Map getCompositions()
    {
        return compositions;
    }
}
