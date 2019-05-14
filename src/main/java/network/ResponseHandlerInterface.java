package network;

import network.responses.ErrorResponse;
import network.responses.RegisterPlayerResponse;
import network.responses.ValidActionsResponse;

public interface ResponseHandlerInterface {
    void handle(RegisterPlayerResponse response);

    void handle(ValidActionsResponse response);

    void handle(ErrorResponse response);
}
