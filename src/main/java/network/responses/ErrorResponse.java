package network.responses;

import network.Response;
import network.ResponseHandlerInterface;

public class ErrorResponse implements Response {

    public Exception exception;

    public ErrorResponse(Exception exception) {
        this.exception = exception;
    }

    @Override
    public void handle(ResponseHandlerInterface handler) {
        handler.handle(this);
    }
}