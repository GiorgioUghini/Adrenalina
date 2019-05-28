package network;

import network.responses.*;
import network.updates.*;

public interface ResponseHandlerInterface {
    void handle(RegisterPlayerResponse response);

    void handle(ValidActionsResponse response);

    void handle(NewPlayerUpdate response);

    void handle(StartGameUpdate response);

    void handle(PlayerDisconnectUpdate response);

    void handle(WaitingPlayerResponse response);

    void handle(CardEffectsResponse response);

    void handle(ErrorResponse response);

    void handle(ChooseMapUpdate response);

    void handle(MapChosenUpdate response);

    void handle(ChooseMapResponse response);
}
