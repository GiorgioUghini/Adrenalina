package network.requests;

import models.card.PowerUpCard;
import models.player.Ammo;
import network.Request;
import network.RequestHandlerInterface;
import network.Response;

import java.rmi.RemoteException;

public class PlayPowerUpRequest implements Request {

    private String token;
    public Ammo ammo;
    public PowerUpCard powerUpAmmo;
    public String powerUpName;

    public PlayPowerUpRequest(String powerUpName, Ammo ammo, PowerUpCard powerUpAmmo){
        this.powerUpName = powerUpName;
        this.ammo = ammo;
        this.powerUpAmmo = powerUpAmmo;
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
