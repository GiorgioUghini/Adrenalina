package network;

import java.io.Serializable;
import java.rmi.RemoteException;

public interface Request extends Serializable {
    void setToken(String token);
    String getToken();
    Response handle(RequestHandlerInterface handler) throws RemoteException;
}