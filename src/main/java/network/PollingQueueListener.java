package network;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PollingQueueListener implements Runnable {

    private BlockingQueue<Response> queue;

    public PollingQueueListener(BlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        ResponseHandler updateHandler = new ResponseHandler();
        Response update = null;
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
