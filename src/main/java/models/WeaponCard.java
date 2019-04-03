package models;

public class WeaponCard extends Card {
    public enum state {
        LOADED,
        PARTIALLY_LOADED,
        UNLOADED
    }
    private state actualState;

    public void setState(state stateToSet) {
        actualState = stateToSet;
    }

    public state getState() {
        return actualState;
    }

    WeaponCard() {
        setState(state.PARTIALLY_LOADED);
    }
}