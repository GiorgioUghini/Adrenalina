package models.card;

import java.io.Serializable;

public class Action implements Serializable {
    public ActionType type;
    public String id;
    public Select select;
    public Damage damage;
    public Mark mark;
    public Move move;
}
