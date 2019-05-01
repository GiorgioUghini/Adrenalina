package network;

public class LongPollingRequest implements Request {
    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
