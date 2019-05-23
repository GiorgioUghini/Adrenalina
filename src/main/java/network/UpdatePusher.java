package network;

import models.player.Player;

import java.io.ObjectOutputStream;
import java.util.concurrent.BlockingQueue;

public class UpdatePusher implements Runnable {

    private BlockingQueue<Response> queue;
    private SocketWrapper socketWrapper;

    UpdatePusher(Player player) {
        this.queue = player.getUpdates();
        this.socketWrapper = Server.getInstance().getConnection().getSocketWrapper(player.getToken());
    }

    @Override
    public void run() {
        Response update = null;
        while (true) {
            try {
                update = queue.take();
                socketWrapper.write(update);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
