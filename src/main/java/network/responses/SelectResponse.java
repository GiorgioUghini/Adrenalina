package network.responses;

import models.card.Selectable;
import network.Response;
import network.ResponseHandlerInterface;

public class SelectResponse implements Response {

    public Selectable selectable;

    public SelectResponse(Selectable selectable){
        this.selectable = selectable;
    }

    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}
