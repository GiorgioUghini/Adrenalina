package network.requests;

import network.Request;
import network.RequestHandlerInterface;
import network.Response;

import java.rmi.RemoteException;

public class PlayPowerUpRequest implements Request {

    private String token;
    public String color;
    public String powerUpName;

    public PlayPowerUpRequest(String powerUpName, String color){
        this.powerUpName = powerUpName;
        this.color = color;
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
