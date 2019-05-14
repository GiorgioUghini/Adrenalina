package network;

import network.requests.RegisterPlayerRequest;
import network.requests.ValidActionsRequest;

import java.rmi.RemoteException;

public interface RequestHandlerInterface {
    Response handle(ValidActionsRequest request) throws RemoteException;
    Response handle(RegisterPlayerRequest request) throws RemoteException;
}
