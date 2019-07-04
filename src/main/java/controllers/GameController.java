package controllers;

import models.card.Effect;
import models.card.PowerUpCard;
import models.card.Taggable;
import models.card.WeaponCard;
import models.map.Square;
import models.player.Ammo;
import models.turn.TurnEvent;
import network.Client;

public class GameController {

    /**
     * asks the server for the valid actions
     */
    public void getValidActions() {
        Client.getInstance().getConnection().validActions();
    }

    /**
     * asks the server for a reconnection
     */
    public void reconnect() {
        Client.getInstance().getConnection().reconnect();
    }

    /**
     * asks the server to draw a powerup
     */
    public void drawPowerUp() {
        Client.getInstance().getConnection().drawPowerUpCard();
    }

    /**
     * asks the server to run in another square
     * @param runType the run types decides in which squares you can run
     * @param square the destination square
     */
    public void run(TurnEvent runType, Square square) {
        Client.getInstance().getConnection().run(runType, square);
    }

    /**
     * asks the server to spawn a player
     * @param powerUpCard the powerup to discard when spawning
     */
    public void spawn(PowerUpCard powerUpCard) {
        Client.getInstance().getConnection().spawnPlayer(powerUpCard);
    }

    /**
     * asks the server to grab either an ammo or a weapon
     * @param drawn the weapon to draw, null for ammos
     * @param toRelease the weapons to release, null for ammos or if you have less than 3 weapons in your hand
     * @param powerUpCard the powerup card to pay with, can always be null
     */
    public void grab(WeaponCard drawn, WeaponCard toRelease, PowerUpCard powerUpCard) {
        Client.getInstance().getConnection().grab(drawn, toRelease, powerUpCard);
    }

    /**
     * asks the server for the possible effects of a weapon card
     * @param weaponCard the card you are playing
     */
    public void getEffects(WeaponCard weaponCard) {
        Client.getInstance().getConnection().cardEffects(weaponCard.name);
    }

    /**
     * asks the server to play a powerup card
     * @param powerUpCard the powerup card to play
     * @param whichAmmo the ammo to pay, if the powerup has a price. it must be exactly 1 cube of any color or null
     * @param payWithThis the powerup used to pay instead of the ammo
     */
    public void playPowerUp(PowerUpCard powerUpCard, Ammo whichAmmo, PowerUpCard payWithThis) {
        Client.getInstance().getConnection().playPowerUp(powerUpCard.name, whichAmmo, payWithThis);
    }

    /**
     * plays the chosen effect of a weapon card
     * @param effect the effect to play
     * @param powerUpCard the powerup card to pay for the effect
     */
    public void playEffect(Effect effect, PowerUpCard powerUpCard) {
        Client.getInstance().getConnection().playEffect(effect, Client.getInstance().getPlayer().getAmmo(), powerUpCard);
    }

    /**
     * informs the server that the user does not want to do anything else with the card he was playing
     */
    public void finishCard() {
        Client.getInstance().getConnection().finishCard();
    }

    /**
     * asks the server to end the turn
     */
    public void endTurn() {
        Client.getInstance().getConnection().endTurn();
    }

    /**
     * asks the server to tag an element, either with a weapon or with a powerup
     * @param selected the element to tag
     * @param shooting if true you are using a weapon, if false a powerup
     */
    public void tagElement(Taggable selected, boolean shooting) {
        if (shooting)
            Client.getInstance().getConnection().tagElement(selected);
        else
            Client.getInstance().getConnection().powerUpTagElement(selected);

    }
}
