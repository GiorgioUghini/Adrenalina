package network.responses;

import models.card.PowerUpCard;
import models.card.WeaponCard;
import network.Response;
import network.ResponseHandlerInterface;

public class DrawPowerUpResponse implements Response {

    private PowerUpCard card;

    public DrawPowerUpResponse(PowerUpCard card){
        this.card = card;
    }

    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}
