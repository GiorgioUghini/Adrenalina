package network.requests;

import network.Request;
import network.RequestHandlerInterface;
import network.Response;

import java.rmi.RemoteException;

public class ChooseMapRequest implements Request {

    private String token;
    public int map;

    public ChooseMapRequest(int map) {
        this.map = map;
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
