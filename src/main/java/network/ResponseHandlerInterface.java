package network;

import network.responses.ErrorResponse;
import network.responses.LongPollingResponse;
import network.responses.RegisterPlayerResponse;

public interface ResponseHandlerInterface {
    void handle(RegisterPlayerResponse response);

    void handle(LongPollingResponse response);

    void handle(ErrorResponse response);
}
