package network;

import errors.InvalidConnectionTypeException;
import network.responses.RegisterPlayerResponse;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RMIConnection implements Connection {

    private RemoteMethodsInterface remoteMethods;
    private Registry registry;
    private BlockingQueue<Update> queue;
    private String token;

    @Override
    public void init() {
        try {
            registry = LocateRegistry.getRegistry();
            remoteMethods = (RemoteMethodsInterface) registry.lookup("RemoteMethods");
            queue = new LinkedBlockingQueue<>(100);
            LongPollingTask longPollingTask = new LongPollingTask(remoteMethods, queue);
            Timer timer = new Timer();
            timer.schedule(longPollingTask, 0, 2);
            PollingQueueListener pollingQueueListener = new PollingQueueListener();
            (new Thread(pollingQueueListener)).start();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }


    @Override
    public String getToken() {
        return token;
    }

    @Override
    public void sendRequest(Request request) {

    }

    @Override
    public void registerPlayer(String username) {
        try {
            RegisterPlayerResponse response = remoteMethods.registerPlayer(username);
            response.handle(new ResponseHandler());

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveResponse(Response response) {

    }

    public RemoteMethodsInterface getRemoteMethods() {
        return remoteMethods;
    }
}
