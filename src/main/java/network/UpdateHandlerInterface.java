package network;

import network.updates.PlayerDisconnectUpdate;
import network.updates.StartGameUpdate;

public interface UpdateHandlerInterface {
    void handle(PlayerDisconnectUpdate update);
    void handle(StartGameUpdate update);
}
