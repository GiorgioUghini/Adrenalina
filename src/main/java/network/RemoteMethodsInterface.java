package network;

import network.responses.RegisterPlayerResponse;
import network.responses.ValidActionsResponse;
import network.responses.WaitingPlayerResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteMethodsInterface extends Remote {
    List<Response> longPolling(String token) throws RemoteException;
    RegisterPlayerResponse registerPlayer(String username, String token) throws RemoteException;
    ValidActionsResponse validActions(String token) throws RemoteException;
    WaitingPlayerResponse waitingPlayer() throws RemoteException;
}