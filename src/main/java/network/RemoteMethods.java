package network;

import models.player.Player;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;

public class RemoteMethods extends UnicastRemoteObject implements RemoteMethodsInterface {
    RemoteMethods() throws RemoteException { }

    @Override
    public List<Update> longPolling() throws RemoteException {
        /*
        List<Update> updates = new LinkedList();
        Player player = new Player(true, "Furlan");
        PlayerDisconnectUpdate update = new PlayerDisconnectUpdate(player);
        updates.add(update);
        return updates;
        */
        return new LinkedList<Update>(); //Placeholder
    }

    @Override
    public String registerPlayer(String username) throws RemoteException  {
        // TODO add token login
        return "abc123";
    }
}