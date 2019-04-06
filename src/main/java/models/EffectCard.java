package models;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.util.EnumSet;
import java.util.List;

public class WeaponCard extends EffectCard {
    public String name;
    public Boolean exclusive;
    public List<Effect> effects;
}

class Effect {
    public String name;
    public List<Action> actions;
}

class Action {
    public ActionType type;
    public String id;
    public Select select;
    public Damage damage;
    public Mark mark;
    public Move move;
}

class Select{
    public TargetType type;
    public String id;
    public Integer max;
    public Radix[] radix;
    public Rules rules;
}

class Radix{
    public String ref;
    public String area;
    public Integer min;
    public Integer max;
}

class Rules{
    public String[] includes;
    public String[] excludes;
}

class Damage{
    public TargetType type;
    public String target;
    public Integer value;
}

class Mark{
    public TargetType type;
    public String target;
    public Integer value;
}

class Move{
    public String target;
    public String dest;
}

enum TargetType{
    @SerializedName("player")
    PLAYER,

    @SerializedName("room")
    ROOM,

    @SerializedName("square")
    SQUARE
}

enum ActionType{
    @SerializedName("select")
    SELECT,

    @SerializedName("damage")
    DAMAGE,

    @SerializedName("mark")
    MARK,

    @SerializedName("move")
    MOVE
}

enum AreaType{
    @SerializedName("visible")
    VISIBLE,

    @SerializedName("cardinal")
    CARDINAL,

    @SerializedName("room")
    ROOM,

    @SerializedName("other_rooms")
    OTHER_ROOMS
}

class AreaTypeDeserializer implements JsonDeserializer<EnumSet>
{
    @Override
    public EnumSet<AreaType> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        EnumSet<AreaType> areas = EnumSet.noneOf(AreaType.class);
        String[] strings = json.getAsString().split("\\|");
        for (String str : strings)
            for (AreaType a : AreaType.values())
                if(a.name().equalsIgnoreCase(str))
                    areas.add(a);
        return areas;
    }
}