package network.requests;

import models.card.Effect;
import models.card.PowerUpCard;
import models.player.Ammo;
import network.Request;
import network.RequestHandlerInterface;
import network.Response;

import java.rmi.RemoteException;

public class PlayEffectRequest implements Request {

    private String token;
    public Effect effect;
    public Ammo ammo;
    public PowerUpCard powerUpCard;

    public PlayEffectRequest(Effect effect, Ammo ammo, PowerUpCard powerUpCard){
        this.effect = effect;
        this.ammo = ammo;
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
