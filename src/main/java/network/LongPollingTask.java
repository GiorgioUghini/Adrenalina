package network;

import java.awt.*;
import java.io.ObjectInputStream;
import java.rmi.RemoteException;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;

public class LongPollingTask extends TimerTask {

    private RemoteMethodsInterface remoteMethods;
    private BlockingQueue<Update> queue;

    public LongPollingTask(RemoteMethodsInterface remoteMethods, BlockingQueue<Update> queue){
        this.remoteMethods = remoteMethods;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            List<Update> updates = remoteMethods.longPolling();
            if(updates != null)
                queue.addAll(updates);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
