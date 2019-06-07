package controllers;

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
        TurnEngine te = new TurnEngine(me.hasJustStarted() ? (me.isDead() ? TurnType.RESPAWN : TurnType.IN_GAME) : TurnType.START_GAME, me.getLifeState());
        int runs = 0;
        
    }
}
