package network.requests;

import models.turn.ActionType;
import network.Request;
import network.RequestHandlerInterface;
import network.Response;

import java.rmi.RemoteException;

public class TurnActionRequest implements Request {

    public ActionType actionType;
    private String token;

    public TurnActionRequest(ActionType actionType) {
        this.actionType = actionType;
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
