package network.requests;

import models.card.Taggable;
import network.Request;
import network.RequestHandlerInterface;
import network.Response;

import java.rmi.RemoteException;

public class PowerUpTagElementRequest implements Request {

    private String token;
    public Taggable taggable;

    public PowerUpTagElementRequest(Taggable taggable) {
        this.taggable = taggable;
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
