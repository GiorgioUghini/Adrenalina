package models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.EnumSet;
import java.util.List;

public class Deck {
    public String getJsonDescriptor() throws IOException {
        ClassLoader classLoader = new Deck().getClass().getClassLoader();
        File file = new File(classLoader.getResource("cards.json").getFile());
        String content = new String(Files.readAllBytes(file.toPath()));
        return content;
    }

    public List<CardDescriptor> getDescriptor() throws IOException {
        String json = getJsonDescriptor();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(EnumSet.class, new AreaTypeDeserializer());
        Gson gson = gsonBuilder.create();
        Type type = new TypeToken<List<CardDescriptor>>(){}.getType();
        List<CardDescriptor> inpList = gson.fromJson(json, type);
        return inpList;
    }
}