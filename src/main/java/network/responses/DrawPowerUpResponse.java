package network.responses;

import models.card.PowerUpCard;
import network.Response;
import network.ResponseHandlerInterface;

public class DrawPowerUpResponse implements Response {

    private PowerUpCard card;

    public DrawPowerUpResponse(PowerUpCard card){
        this.card = card;
    }

    public PowerUpCard getCard() {
        return card;
    }

    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}
