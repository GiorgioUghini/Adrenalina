package network;

import java.rmi.RemoteException;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            String token = Client.getInstance().getConnection().getToken();
            List<Update> updates = remoteMethods.longPolling(token);
            if(updates != null)
                queue.addAll(updates);
        } catch (RemoteException e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.SEVERE, "an exception was thrown", e);
        }
    }
}
