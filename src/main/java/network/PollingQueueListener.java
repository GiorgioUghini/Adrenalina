package network;

import java.util.concurrent.BlockingQueue;

public class PollingQueueListener implements Runnable {

    private BlockingQueue<Response> queue;
    private boolean stop;

    public PollingQueueListener(BlockingQueue queue) {
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
                e.printStackTrace();
            }
        }
    }

    public void stop(){
        stop = true;
    }
}
