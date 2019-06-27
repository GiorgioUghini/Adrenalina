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

    public void getValidActions() {
        Client.getInstance().getConnection().validActions();
    }

    public void reconnect(){
        Client.getInstance().getConnection().reconnect();
    }

    public void drawPowerUp() {
        Client.getInstance().getConnection().drawPowerUpCard();
    }

    public void run(TurnEvent runType, Square square) {
        Client.getInstance().getConnection().run(runType, square);
    }

    public void spawn(PowerUpCard powerUpCard) {
        Client.getInstance().getConnection().spawnPlayer(powerUpCard);
    }

    public void grab(WeaponCard drawn, WeaponCard toRelease, PowerUpCard powerUpCard) {
        Client.getInstance().getConnection().grab(drawn, toRelease, powerUpCard);
    }

    public void getEffects(WeaponCard weaponCard) {
        Client.getInstance().getConnection().cardEffects(weaponCard.name);
    }

    public void getEffects(PowerUpCard powerUpCard, PowerUpCard payWithThis) { Client.getInstance().getConnection().playPowerUp(powerUpCard.name, payWithThis == null ? Client.getInstance().getPlayer().getAmmo() : null, payWithThis); }

    public void playEffect(Effect effect, PowerUpCard powerUpCard) {
        Client.getInstance().getConnection().playEffect(effect, Client.getInstance().getPlayer().getAmmo(), powerUpCard);
    }

    public void finishCard() {
        Client.getInstance().getConnection().finishCard();
    }

    public void endTurn() {
        Client.getInstance().getConnection().endTurn();
    }

    public void tagElement(Taggable selected, boolean shooting) {
        if (shooting)
            Client.getInstance().getConnection().tagElement(selected);
        else
            Client.getInstance().getConnection().powerUpTagElement(selected);

    }
}
