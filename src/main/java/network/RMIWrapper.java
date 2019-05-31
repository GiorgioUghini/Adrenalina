package network;

import utils.TokenGenerator;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RMIWrapper implements ConnectionWrapper {

    private RMIStatusTask rmiStatusTask;
    private String token;
    private BlockingQueue<Response> updates;

    public RMIWrapper(){
        this.token = TokenGenerator.nextToken();
        updates = new LinkedBlockingQueue<>();
        Server.getInstance().getConnection().addConnectionWrapper(token, this);
    }

    public void setRMIStatusTask(RMIStatusTask rmiStatusTask){
        this.rmiStatusTask = rmiStatusTask;
    }

    public void stop() {
        rmiStatusTask.cancel();
    }

    public void ping(){
        if(rmiStatusTask != null) {
            rmiStatusTask.ping();
        }
    }

    @Override
    public void addUpdate(Response update) {
        updates.add(update);
    }

    @Override
    public BlockingQueue<Response> getUpdates() {
        return updates;
    }

    @Override
    public void clearUpdates() {
        updates.clear();
    }
}
