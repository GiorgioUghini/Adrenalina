package network.requests;

import models.card.WeaponCard;
import network.Request;
import network.RequestHandlerInterface;
import network.Response;

import java.rmi.RemoteException;
import java.util.List;

public class ReloadRequest implements Request {

    private String token;
    public List<WeaponCard> weapons;

    public ReloadRequest(List<WeaponCard> weapons){
        this.weapons = weapons;
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
