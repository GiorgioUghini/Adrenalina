package network;

import java.io.Serializable;

public interface Response extends Serializable {
    boolean isError = false;
    void handle(ResponseHandlerInterface handler);
}