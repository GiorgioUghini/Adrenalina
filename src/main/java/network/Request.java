package network;

import java.io.Serializable;
import java.rmi.RemoteException;

public interface Request extends Serializable {
    String getToken();
    void setToken(String token);
    Response handle(RequestHandlerInterface handler) throws RemoteException;
}