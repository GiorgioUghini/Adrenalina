package network;

public interface RequestHandler {
    Response handle(LongPollingRequest request);

    Response handle(RegisterPlayerRequest request);
}
