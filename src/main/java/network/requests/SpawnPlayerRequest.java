package network.requests;

import models.map.RoomColor;
import network.Client;
import network.Request;
import network.RequestHandlerInterface;
import network.Response;

import java.rmi.RemoteException;

public class SpawnPlayerRequest implements Request {

    public String token;
    public RoomColor color;

    public SpawnPlayerRequest(RoomColor color){
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