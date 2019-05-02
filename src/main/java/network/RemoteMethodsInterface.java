package network;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteMethodsInterface extends Remote {
    public List<Update> longPolling() throws RemoteException;

    public String registerPlayer(String username) throws RemoteException;
}