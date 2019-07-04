package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import models.card.*;
import network.Server;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

public class CardController {
    private WeaponDeck weaponDeck;
    private PowerUpDeck powerUpDeck;
    private AmmoDeck ammoDeck;

    public CardController() {
        loadWeaponCards();
        loadPowerUpCards();
        loadAmmoCards();
    }

    /**
     * Reads the cards from the json file, workd with all deck
     *
     * @param filename the name of the json file containing the cards
     * @param cardType the card type
     * @return a list containing the read cards
     */
    private List<Card> getDeserializedCards(String filename, CardType cardType) {
        try {
            String json = getJsonCardDescriptor(filename);
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            Type type = getType(cardType);
            return gson.fromJson(json, type);
        } catch (IOException e) {
            //fatal error, close the server
            Server.getInstance().fatalError(e);
        }
        return null;
    }

    /**
     * creates the right typeToken for the given card type
     *
     * @param cardType the type of the card
     * @return the correct typeToken
     */
    private Type getType(CardType cardType) {
        switch (cardType) {
            case AMMO:
                return new TypeToken<List<AmmoCard>>() {
                }.getType();
            case WEAPON:
                return new TypeToken<List<WeaponCard>>() {
                }.getType();
            case POWERUP:
                return new TypeToken<List<PowerUpCard>>() {
                }.getType();
            default:
                return null;
        }
    }

    /**
     * Reads the weapons cards and sets the deck
     */
    private void loadWeaponCards() {
        List<Card> cards = getDeserializedCards("weapon_cards.json", CardType.WEAPON);
        weaponDeck = new WeaponDeck(cards);
    }

    /**
     * Reads the powerUp cards and sets the deck
     */
    private void loadPowerUpCards() {
        List<Card> cards = getDeserializedCards("powerup_cards.json", CardType.POWERUP);
        powerUpDeck = new PowerUpDeck(cards);
    }

    /**
     * Reads the ammo cards and sets the deck
     */
    private void loadAmmoCards() {
        List<Card> cards = getDeserializedCards("ammo_cards.json", CardType.AMMO);
        ammoDeck = new AmmoDeck(cards);
    }

    /**
     * reads the file given as param
     *
     * @param filename the name of the file to read
     * @return
     * @throws IOException if the file does not exist
     */
    private String getJsonCardDescriptor(String filename) throws IOException {
        InputStream file = ResourceController.getResource(filename);
        return new String(file.readAllBytes());
    }

    /**
     * @return the weapon deck
     */
    public WeaponDeck getWeaponDeck() {
        return weaponDeck;
    }

    /**
     * @return the powerUp deck
     */
    public PowerUpDeck getPowerUpDeck() {
        return powerUpDeck;
    }

    /**
     * @return the ammo deck
     */
    public AmmoDeck getAmmoDeck() {
        return ammoDeck;
    }
}