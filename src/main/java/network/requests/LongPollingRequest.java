package network.requests;

import network.Request;
import network.RequestHandlerInterface;
import network.Response;

public class LongPollingRequest implements Request {
    @Override
    public Response handle(RequestHandlerInterface handler) {
        return handler.handle(this);
    }
}
