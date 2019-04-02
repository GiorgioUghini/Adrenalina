package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import models.Card;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.List;

public class CardController {
    private List<Card> weaponDeck;
    private List<Card> powerUpDeck;

    private void loadWeaponCards() throws IOException{
        String jsonContent = getJsonCardDescriptor("weapon_cards.json");
        weaponDeck = getDescriptor(jsonContent);
    }

    private void loadPowerUpCards() throws IOException{
        String jsonContent = getJsonCardDescriptor("powerup_cards.json");
        powerUpDeck = getDescriptor(jsonContent);
    }

    private String getJsonCardDescriptor(String filename) throws IOException {
        File file = new ResourceController().getResource(filename);
        String content = new String(Files.readAllBytes(file.toPath()));
        return content;
    }

    private List<Card> getDescriptor(String json) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        //gsonBuilder.registerTypeAdapter(EnumSet.class, new AreaTypeDeserializer());
        Gson gson = gsonBuilder.create();
        Type type = new TypeToken<List<Card>>(){}.getType();
        List<Card> inpList = gson.fromJson(json, type);
        return inpList;
    }

    public CardController() throws IOException{
        loadWeaponCards();
        loadPowerUpCards();
    }

    public List<Card> getWeaponDeck(){
        return weaponDeck;
    }

    public List<Card> getPowerUpDeck(){
        return powerUpDeck;
    }
}