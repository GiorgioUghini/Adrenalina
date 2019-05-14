package network.requests;

import network.Client;
import network.Request;
import network.RequestHandlerInterface;
import network.Response;

import java.rmi.RemoteException;

public class RegisterPlayerRequest implements Request {

    private String token;
    public String username;

    @Override
    public String getToken() {
        return token;
    }

    public RegisterPlayerRequest(String username){
        this.token = Client.getInstance().getConnection().getToken();
        this.username = username;
    }

    @Override
    public Response handle(RequestHandlerInterface handler) throws RemoteException {
        return handler.handle(this);
    }
}
