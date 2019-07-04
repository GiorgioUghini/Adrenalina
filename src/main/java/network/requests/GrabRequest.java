package network.requests;

import models.card.PowerUpCard;
import models.card.WeaponCard;
import network.Request;
import network.RequestHandlerInterface;
import network.Response;

import java.rmi.RemoteException;

public class GrabRequest implements Request {

    private String token;
    public WeaponCard drawn;
    public WeaponCard toRelease;
    public PowerUpCard powerUpCard;

    public GrabRequest(WeaponCard drawn, WeaponCard toRelease, PowerUpCard powerUpCard) {
        this.drawn = drawn;
        this.toRelease = toRelease;
        this.powerUpCard = powerUpCard;
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
