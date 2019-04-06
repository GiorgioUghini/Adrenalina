package models.card;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.EnumSet;

public class AreaTypeDeserializer implements JsonDeserializer<EnumSet>
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