package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import models.card.Card;
import models.card.PowerUpDeck;
import models.card.WeaponCard;
import models.card.WeaponDeck;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.List;

public class CardController {
    private WeaponDeck weaponDeck;
    private PowerUpDeck powerUpDeck;

    private List<Card> getDeserializedCards(String filename) throws IOException {
        String json = getJsonCardDescriptor(filename);
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Type type = new TypeToken<List<WeaponCard>>(){}.getType();
        return gson.fromJson(json, type);
    }

    private void loadWeaponCards() throws IOException{
        List<Card> cards = getDeserializedCards("weapon_cards.json");
        weaponDeck = new WeaponDeck(cards);
    }

    private void loadPowerUpCards() throws IOException{
        List<Card> cards = getDeserializedCards("powerup_cards.json");
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