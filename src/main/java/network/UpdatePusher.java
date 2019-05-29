package network;

import models.player.Player;
import java.util.concurrent.BlockingQueue;

public class UpdatePusher implements Runnable {

    private BlockingQueue<Response> queue;
    private SocketWrapper socketWrapper;
    private boolean stop;

    UpdatePusher(SocketWrapper socketWrapper) {
        this.stop = false;
        this.queue = socketWrapper.getUpdates();
        this.socketWrapper = socketWrapper;
    }

    @Override
    public void run() {
        Response update = null;
        while (!stop) {
            try {
                update = queue.take();
                if(stop)
                    break;
                socketWrapper.write(update);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stop(){
        stop = true;
    }
}
