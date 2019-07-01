package network.requests;

import models.card.PowerUpCard;
import models.card.WeaponCard;
import network.Request;
import network.RequestHandlerInterface;
import network.Response;

import java.rmi.RemoteException;
import java.util.Map;

public class ReloadRequest implements Request {

    private String token;
    public Map<WeaponCard, PowerUpCard> weaponsMap;

    public ReloadRequest(Map<WeaponCard, PowerUpCard> weaponsMap){
        this.weaponsMap = weaponsMap;
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
