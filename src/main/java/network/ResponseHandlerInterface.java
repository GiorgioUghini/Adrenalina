package network;

import network.responses.ErrorResponse;
import network.responses.RegisterPlayerResponse;
import network.responses.ValidActionsResponse;
import network.responses.WaitingPlayerResponse;
import network.updates.NewPlayerUpdate;
import network.updates.PlayerDisconnectUpdate;
import network.updates.StartGameUpdate;

public interface ResponseHandlerInterface {
    void handle(RegisterPlayerResponse response);

    void handle(ValidActionsResponse response);

    void handle(NewPlayerUpdate response);

    void handle(StartGameUpdate response);

    void handle(PlayerDisconnectUpdate response);

    void handle(WaitingPlayerResponse response);

    void handle(ErrorResponse response);
}
