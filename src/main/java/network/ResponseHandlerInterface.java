package network;

import network.responses.*;
import network.updates.*;

public interface ResponseHandlerInterface {
    void handle(RegisterPlayerResponse response);

    void handle(ReconnectPlayerResponse response);

    void handle(ValidActionsResponse response);

    void handle(NewPlayerUpdate response);

    void handle(StartGameUpdate response);

    void handle(PlayerDisconnectUpdate response);

    void handle(WaitingPlayerResponse response);

    void handle(CardEffectsResponse response);

    void handle(ErrorResponse response);

    void handle(ChooseMapUpdate response);

    void handle(MapChosenUpdate response);

    void handle(NextTurnUpdate response);

    void handle(ChooseMapResponse response);

    void handle(DrawPowerUpResponse response);

    void handle(SpawnPlayerResponse response);

    void handle(SelectResponse response);

    void handle(PowerUpSelectResponse response);

    void handle(FinishCardResponse response);

    void handle(FinishEffectResponse response);

    void handle(FinishPowerUpResponse response);

    void handle(EndTurnResponse response);

    void handle(GrabResponse response);

    void handle(RunResponse response);

    void handle(ReloadResponse response);

    void handle(TurnActionResponse response);

    void handle(MapUpdate response);

    void handle(PlayerUpdate response);

    void handle(ReconnectResponse response);

    void handle(PlayersUpdate response);

    void handle(DamageUpdate response);

    void handle(MarkUpdate response);
}
