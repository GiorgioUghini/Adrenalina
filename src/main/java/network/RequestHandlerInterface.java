package network;

import network.requests.*;

import java.rmi.RemoteException;

public interface RequestHandlerInterface {
    Response handle(ValidActionsRequest request) throws RemoteException;
    Response handle(RegisterPlayerRequest request) throws RemoteException;
    Response handle(WaitingPlayerRequest request) throws RemoteException;
    Response handle(ChooseMapRequest request) throws RemoteException;
    Response handle(CardEffectsRequest request) throws RemoteException;
    Response handle(DrawPowerUpRequest request) throws RemoteException;
    Response handle(SpawnPlayerRequest request) throws RemoteException;
}
