package network;

import utils.Console;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class PollingQueueListener implements Runnable {

    private BlockingQueue<Response> queue;
    private boolean stop;

    public PollingQueueListener(BlockingQueue<Response> queue) {
        this.queue = queue;
        this.stop = false;
    }

    @Override
    public void run() {
        ResponseHandler updateHandler = new ResponseHandler();
        Response update = null;
        while (!stop) {
            try {
                update = queue.take();
                update.handle(updateHandler);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Logger.getAnonymousLogger().info(e.toString());
            }
        }
    }

    public void stop(){
        stop = true;
    }
}
