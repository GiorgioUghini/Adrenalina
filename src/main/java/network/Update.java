package network;

import java.io.Serializable;

public interface Update extends Serializable {
    void handle(UpdateHandler handler);
}