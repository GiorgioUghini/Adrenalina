package models.card;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LegitEffects {
    private Map<Effect, Boolean> effects;

    public LegitEffects(Map<Effect, Boolean> effects){
        this.effects = effects;
    }

    public List<Effect> getLegitEffects(){
        List<Effect> out = new ArrayList<>();
        for(Map.Entry<Effect, Boolean> entry : effects.entrySet()){
            if(entry.getValue()) out.add(entry.getKey());
        }
        return out;
    }

    public List<Effect> getAllEffects(){
        List<Effect> out = new ArrayList<>();
        for(Map.Entry<Effect, Boolean> entry : effects.entrySet()){
            out.add(entry.getKey());
        }
        return out;
    }
}
