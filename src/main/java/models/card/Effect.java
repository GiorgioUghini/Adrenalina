package models.card;

import java.io.Serializable;
import java.util.List;

public class Effect implements Serializable {
    public String name;
    public List<Action> actions;
}