package network.responses;

import models.card.LegitEffects;
import network.Response;
import network.ResponseHandlerInterface;

public class CardEffectsResponse implements Response {

    public LegitEffects legitEffects;

    public CardEffectsResponse(LegitEffects legitEffects){
        this.legitEffects = legitEffects;
    }

    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}
