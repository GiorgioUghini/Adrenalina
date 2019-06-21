package controllers;

import models.card.Effect;
import models.card.PowerUpCard;
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

    public void playEffect(Effect effect, PowerUpCard powerUpCard) {
        Client.getInstance().getConnection().playEffect(effect, Client.getInstance().getPlayer().getAmmo(), powerUpCard);
    }

    public void finishCard() {
        Client.getInstance().getConnection().finishCard();
    }

    public void endTurn() {
        Client.getInstance().getConnection().endTurn();
    }
}
