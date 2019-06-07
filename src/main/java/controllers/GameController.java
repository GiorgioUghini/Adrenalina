package controllers;

import models.card.PowerUpCard;
import models.player.Player;
import models.turn.TurnEngine;
import models.turn.TurnType;
import network.Client;

public class GameController {

    public void getValidActions() {
        Client.getInstance().getConnection().validActions();
    }

    public void drawPowerUp() {
        Client.getInstance().getConnection().drawPowerUpCard();
    }

    public int howMuchRun() {
        Player me = Client.getInstance().getPlayer();
        TurnEngine te;
        if (me.hasJustStarted())
            te = new TurnEngine(me.isDead() ? TurnType.RESPAWN : TurnType.IN_GAME, me.getLifeState());
        else te = new TurnEngine(TurnType.START_GAME, me.getLifeState());
        int runs = 0;
        boolean go = true;
        while (go) {
            try {
                te.run();
                runs++;
            } catch (Exception e) {
                go = false;
            }
        }
        return runs;
    }

    public void spawn(PowerUpCard powerUpCard) {
        Client.getInstance().getConnection().spawnPlayer(powerUpCard.color);

    }
}
