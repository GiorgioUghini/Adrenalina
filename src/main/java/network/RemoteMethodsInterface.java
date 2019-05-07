package network;

import network.responses.RegisterPlayerResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteMethodsInterface extends Remote {
    public List<Update> longPolling() throws RemoteException;

    public RegisterPlayerResponse registerPlayer(String username) throws RemoteException;
}