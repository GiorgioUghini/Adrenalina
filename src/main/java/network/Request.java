package network;

import java.io.Serializable;
import java.rmi.RemoteException;

public interface Request extends Serializable {
    String getToken();
    Response handle(RequestHandlerInterface handler) throws RemoteException;
}