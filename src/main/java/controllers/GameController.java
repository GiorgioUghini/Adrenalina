package controllers;

import network.Client;

public class GameController {

    public void getValidActions() {
        Client.getInstance().getConnection().validActions();
    }

    public void drawPowerUp() {
        Client.getInstance().getConnection().drawPowerUpCard();
    }

}
