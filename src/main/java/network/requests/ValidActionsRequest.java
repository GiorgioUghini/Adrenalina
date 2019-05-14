package network.requests;

import network.Client;
import network.Request;
import network.RequestHandlerInterface;
import network.Response;

import java.rmi.RemoteException;

public class ValidActionsRequest implements Request {

    private String token;

    public ValidActionsRequest(){
        this.token = Client.getInstance().getConnection().getToken();
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public Response handle(RequestHandlerInterface handler) throws RemoteException {
        return handler.handle(this);
    }
}
