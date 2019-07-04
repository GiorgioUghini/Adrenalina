package models.card;

import java.io.Serializable;
import java.util.Set;

public class Selectable implements Serializable {
    private Set<Taggable> taggables;
    private boolean optional;
    private TargetType type;

    public Selectable(boolean optional) {
        this.optional = optional;
    }

    /**
     * Add a set of taggable elements, all of the type defined by targetType
     *
     * @param taggables
     * @param type      square, player or RoomColor
     */
    public void add(Set<Taggable> taggables, TargetType type) {
        this.taggables = taggables;
        this.type = type;
    }

    /**
     * @return the set of taggables
     */
    public Set<Taggable> get() {
        return taggables;
    }

    /**
     * @return true if the select is optional
     */
    public boolean isOptional() {
        return this.optional;
    }

    /**
     * @return the type of the taggables in the set (Square, Player, RoomColor)
     */
    public TargetType getType() {
        return type;
    }
}
