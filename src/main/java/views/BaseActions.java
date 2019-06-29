package views;

public enum BaseActions {
    END_TURN("End turn"),
    DRAW("Draw"),
    SPAWN("Spawn"),
    RUN("Run"),
    GRAB("Grab"),
    SHOOT("Shoot"),
    RELOAD("Reload"),
    USE_POWERUP("Use Powerup"),
    END_SELECT_TAG("End Select Tag");

    private String friendlyName;

    BaseActions(String friendlyName){
        this.friendlyName = friendlyName;
    }

    public String toString(){
        return friendlyName;
    }

    public BaseActions fromString(String str){
        for(BaseActions baseActions : BaseActions.values()){
            if(str.equals(baseActions.toString())) return baseActions;
        }
        return null;
    }
}
