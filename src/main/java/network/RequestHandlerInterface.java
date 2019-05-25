package network;

import network.requests.RegisterPlayerRequest;
import network.requests.ValidActionsRequest;
import network.requests.WaitingPlayerRequest;

import java.rmi.RemoteException;

public interface RequestHandlerInterface {
    Response handle(ValidActionsRequest request) throws RemoteException;
    Response handle(RegisterPlayerRequest request) throws RemoteException;
    Response handle(WaitingPlayerRequest request) throws RemoteException;
}
