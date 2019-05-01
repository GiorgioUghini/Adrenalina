package network;

import java.rmi.Remote;

public interface RemoteMethodsInterface extends Remote {
    public Object longPoll();

    public String registerPlayer(String username);
}