package network;

import java.util.concurrent.BlockingQueue;

public interface ConnectionWrapper {
    void stop();
    void addUpdate(Response update);
    BlockingQueue<Response> getUpdates();
    void clearUpdates();
}
