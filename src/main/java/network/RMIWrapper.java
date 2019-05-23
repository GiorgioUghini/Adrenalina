package network;

public class RMIWrapper {

    private RMIStatusTask rmiStatusTask;

    public void setRMIStatusTask(RMIStatusTask rmiStatusTask){
        this.rmiStatusTask = rmiStatusTask;
    }

    public void stop() {
        rmiStatusTask.cancel();
    }

    public void ping(){
        rmiStatusTask.ping();
    }
}
