package network.requests;

import network.Request;
import network.RequestHandlerInterface;
import network.Response;

import java.rmi.RemoteException;

public class RegisterPlayerRequest implements Request {

    public String username;

    public RegisterPlayerRequest(String username){
        this.username = username;
    }

    @Override
    public Response handle(RequestHandlerInterface handler) throws RemoteException {
        return handler.handle(this);
    }
}
