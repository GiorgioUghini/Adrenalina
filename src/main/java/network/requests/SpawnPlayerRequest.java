package network.requests;

import models.card.PowerUpCard;
import network.Request;
import network.RequestHandlerInterface;
import network.Response;

import java.rmi.RemoteException;

public class SpawnPlayerRequest implements Request {

    public String token;
    public PowerUpCard powerUpCard;

    public SpawnPlayerRequest(PowerUpCard powerUpCard) {
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
