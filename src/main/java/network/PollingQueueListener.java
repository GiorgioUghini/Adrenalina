package network;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PollingQueueListener implements Runnable {

    private BlockingQueue<Update> queue;

    public PollingQueueListener() {
        this.queue = new LinkedBlockingQueue<>(100);
    }

    @Override
    public void run() {
        UpdateHandler updateHandler = new UpdateHandler();
        Update update = null;
        while (true) {
            try {
                update = queue.take();
                update.handle(updateHandler);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
