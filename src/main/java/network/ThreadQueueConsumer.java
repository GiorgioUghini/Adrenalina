package network;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class ThreadQueueConsumer implements Runnable {

    private BlockingQueue<Thread> queue;
    private boolean stop;

    public ThreadQueueConsumer(BlockingQueue<Thread> queue) {
        this.queue = queue;
        this.stop = false;
    }

    @Override
    public void run() {
        Thread t = null;
        while (!stop) {
            try {
                t = queue.take();
                t.start();
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
