package network.responses;

import network.Response;
import network.ResponseHandlerInterface;

import java.util.Map;

public class CardEffectsResponse implements Response {

    public Map<String, Boolean> effects;

    public CardEffectsResponse(Map<String, Boolean> effects){
        this.effects = effects;
    }

    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}
