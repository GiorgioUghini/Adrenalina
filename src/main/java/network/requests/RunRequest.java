package network.requests;

import models.map.Square;
import models.turn.TurnEvent;
import network.Request;
import network.RequestHandlerInterface;
import network.Response;

import java.rmi.RemoteException;

public class RunRequest implements Request {

    public TurnEvent turnEvent;
    public Square square;
    private String token;

    public RunRequest(TurnEvent turnEvent, Square square) {
        this.square = square;
        this.turnEvent = turnEvent;
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
