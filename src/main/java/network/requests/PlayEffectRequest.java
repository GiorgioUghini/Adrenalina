package network.requests;

import models.card.Effect;
import network.Request;
import network.RequestHandlerInterface;
import network.Response;

import java.rmi.RemoteException;

public class PlayEffectRequest implements Request {

    private String token;
    public Effect effect;

    public PlayEffectRequest(Effect effect){
        this.effect = effect;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public Response handle(RequestHandlerInterface handler) throws RemoteException {
        return handler.handle(this);
    }
}
