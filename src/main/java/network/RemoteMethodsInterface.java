package network;

import network.responses.RegisterPlayerResponse;
import network.responses.ValidActionsResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteMethodsInterface extends Remote {
    List<Update> longPolling(String token) throws RemoteException;
    RegisterPlayerResponse registerPlayer(String username) throws RemoteException;
    ValidActionsResponse validActions(String token) throws RemoteException;
}