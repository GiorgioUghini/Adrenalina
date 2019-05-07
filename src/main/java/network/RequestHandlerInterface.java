package network;

import network.requests.LongPollingRequest;
import network.requests.RegisterPlayerRequest;

import java.rmi.RemoteException;

public interface RequestHandlerInterface {
    Response handle(LongPollingRequest request);

    Response handle(RegisterPlayerRequest request) throws RemoteException;
}
