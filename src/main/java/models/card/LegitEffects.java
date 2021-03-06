package models.card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LegitEffects implements Serializable {
    private Map<Effect, Boolean> effects;

    public LegitEffects() {
        this.effects = new HashMap<>();
    }

    public LegitEffects(Map<Effect, Boolean> effects) {
        this.effects = effects;
    }

    /**
     * Returns a list of the effects that the user can actually activate
     */
    public List<Effect> getLegitEffects() {
        List<Effect> out = new ArrayList<>();
        for (Map.Entry<Effect, Boolean> entry : effects.entrySet()) {
            if (entry.getValue()) out.add(entry.getKey());
        }
        return out;
    }

    /**
     * Returns only the effect the user cannot activate
     */
    public List<Effect> getIllegalEffects() {
        List<Effect> out = new ArrayList<>();
        for (Map.Entry<Effect, Boolean> entry : effects.entrySet()) {
            if (!entry.getValue()) out.add(entry.getKey());
        }
        return out;
    }

    /**
     * @param effect the effect to add to the list
     * @param legit  a boolean that indicates whether the effect can be activated or not
     */
    void addEffect(Effect effect, Boolean legit) {
        effects.put(effect, legit);
    }

    /**
     * @return all the effects of the card
     */
    public List<Effect> getAllEffects() {
        List<Effect> out = new ArrayList<>();
        for (Map.Entry<Effect, Boolean> entry : effects.entrySet()) {
            out.add(entry.getKey());
        }
        return out;
    }

    /**
     * confront these effects with those of the other list and returns a list in which the legit effects are only
     * those valid in both lists.
     *
     * @param other the other list
     */
    LegitEffects logicalAnd(LegitEffects other) {
        Map<Effect, Boolean> map = new HashMap<>();
        map.putAll(effects);
        for (Effect e : other.getIllegalEffects()) {
            map.put(e, false);
        }
        return new LegitEffects(map);
    }
}
