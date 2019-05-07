package network;

import network.responses.LongPollingResponse;
import network.responses.RegisterPlayerResponse;

public interface ResponseHandlerInterface {
    void handle(RegisterPlayerResponse response);

    void handle(LongPollingResponse response);
}
