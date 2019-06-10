package controllers;

import models.card.PowerUpCard;
import models.map.Square;
import models.player.Player;
import models.turn.TurnEvent;
import models.turn.TurnType;
import network.Client;

public class GameController {

    public void getValidActions() {
        Client.getInstance().getConnection().validActions();
    }

    public void drawPowerUp() {
        Client.getInstance().getConnection().drawPowerUpCard();
    }

    public void run(TurnEvent runType, Square square) {
        Client.getInstance().getConnection().run(runType, square);
    }

    public void spawn(PowerUpCard powerUpCard) {
        Client.getInstance().getConnection().spawnPlayer(powerUpCard.color);
    }

    public void endTurn() {
        Client.getInstance().getConnection().endTurn();
    }
}
