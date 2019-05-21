package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import models.card.*;
import network.Server;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.List;

public class CardController {
    private WeaponDeck weaponDeck;
    private PowerUpDeck powerUpDeck;
    private AmmoDeck ammoDeck;

    private List<Card> getDeserializedCards(String filename, CardType cardType) {
        try{
            String json = getJsonCardDescriptor(filename);
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            Type type = getType(cardType);
            return gson.fromJson(json, type);
        }catch(IOException e){
            //fatal error, close the server
            Server.getInstance().fatalError(e);
        }
        return null;
    }

    private Type getType(CardType cardType){
        if(cardType.equals(CardType.AMMO))
            return new TypeToken<List<AmmoCard>>(){}.getType();
        else
            return new TypeToken<List<WeaponCard>>(){}.getType();
    }

    private void loadWeaponCards(){
        List<Card> cards = getDeserializedCards("weapon_cards.json", CardType.WEAPON);
        weaponDeck = new WeaponDeck(cards);
    }

    private void loadPowerUpCards(){
        List<Card> cards = getDeserializedCards("powerup_cards.json", CardType.POWERUP);
        powerUpDeck = new PowerUpDeck(cards);
    }

    private void loadAmmoCards(){
        List<Card> cards = getDeserializedCards("ammo_cards.json", CardType.AMMO);
        ammoDeck = new AmmoDeck(cards);
    }

    private String getJsonCardDescriptor(String filename) throws IOException {
        File file = ResourceController.getResource(filename);
        return new String(Files.readAllBytes(file.toPath()));
    }

    public CardController(){
        loadWeaponCards();
        loadPowerUpCards();
        loadAmmoCards();
    }

    public WeaponDeck getWeaponDeck(){
        return weaponDeck;
    }

    public PowerUpDeck getPowerUpDeck(){
        return powerUpDeck;
    }

    public AmmoDeck getAmmoDeck(){
        return ammoDeck;
    }
}