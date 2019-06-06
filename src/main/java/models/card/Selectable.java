package models.card;

import java.util.Set;

public class Selectable {
    private Set<Taggable> taggables;
    private boolean optional;
    private TargetType type;

    public Selectable(boolean optional){
        this.optional = optional;
    }

    public void add(Set<Taggable> taggables, TargetType type){
        this.taggables = taggables;
        this.type = type;
    }

    public Set<Taggable> get(){
        return taggables;
    }

    public boolean isOptional(){
        return this.optional;
    }

    public TargetType getType(){
        return type;
    }
}
