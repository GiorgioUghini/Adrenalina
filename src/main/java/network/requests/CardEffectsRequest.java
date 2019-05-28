package network.requests;

import network.Request;
import network.RequestHandlerInterface;
import network.Response;

import java.rmi.RemoteException;

public class CardEffectsRequest implements Request {

    private String token;
    public String cardName;

    public CardEffectsRequest(String cardName){
        this.cardName = cardName;
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
        return null;
    }
}
