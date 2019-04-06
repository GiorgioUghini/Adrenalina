package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import models.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.List;

public class CardController {
    private WeaponDeck weaponDeck;
    private PowerUpDeck powerUpDeck;

    private void loadWeaponCards() throws IOException{
        String json = getJsonCardDescriptor("weapon_cards.json");
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Type type = new TypeToken<List<WeaponCard>>(){}.getType();
        List<WeaponCard> cards = gson.fromJson(json, type);
        weaponDeck = new WeaponDeck(cards);
    }

    private void loadPowerUpCards() throws IOException{
        String json = getJsonCardDescriptor("powerup_cards.json");
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Type type = new TypeToken<List<PowerUpCard>>(){}.getType();
        List<PowerUpCard> cards = gson.fromJson(json, type);
        powerUpDeck = new PowerUpDeck(cards);
    }

    private String getJsonCardDescriptor(String filename) throws IOException {
        File file = ResourceController.getResource(filename);
        return new String(Files.readAllBytes(file.toPath()));
    }

    public CardController() throws IOException{
        loadWeaponCards();
        loadPowerUpCards();
    }

    public WeaponDeck getWeaponDeck(){
        return weaponDeck;
    }

    public PowerUpDeck getPowerUpDeck(){
        return powerUpDeck;
    }
}