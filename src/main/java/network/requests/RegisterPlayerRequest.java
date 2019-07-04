package network.requests;

import network.Client;
import network.Request;
import network.RequestHandlerInterface;
import network.Response;

import java.rmi.RemoteException;

public class RegisterPlayerRequest implements Request {

    public String username;
    public String password;
    private String token;

    public RegisterPlayerRequest(String username, String password) {
        this.token = Client.getInstance().getConnection().getToken();
        this.username = username;
        this.password = password;
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
