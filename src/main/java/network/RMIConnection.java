package network;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RMIConnection implements Connection {

    private RemoteMethodsInterface remoteMethods;
    private ResponseHandler responseHandler;
    private Registry registry;
    private BlockingQueue<Response> queue;
    private String token;

    @Override
    public void registerPlayer(String username, String password) {
        try {
            Response response = remoteMethods.registerPlayer(username, password, token);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getWaitingPlayer() {
        try {
            Response response = remoteMethods.waitingPlayer();
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void validActions() {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.validActions(token);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void chooseMap(int map) {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.chooseMap(token, map);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cardEffects(String cardName) {
        try {
            String token = Client.getInstance().getConnection().getToken();
            Response response = remoteMethods.cardEffects(token, cardName);
            Client.getInstance().getConnection().receiveResponse(response);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveResponse(Response response) {
        response.handle(responseHandler);
    }

    @Override
    public void init() {
        try {
            registry = LocateRegistry.getRegistry();
            remoteMethods = (RemoteMethodsInterface) registry.lookup("RemoteMethods");
            queue = new LinkedBlockingQueue<>(100);
            responseHandler = new ResponseHandler();
            token = remoteMethods.handshake();
            LongPollingTask longPollingTask = new LongPollingTask(remoteMethods, queue);
            Timer timer = new Timer();
            timer.schedule(longPollingTask, 0, 100);
            PollingQueueListener pollingQueueListener = new PollingQueueListener(queue);
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

    public RemoteMethodsInterface getRemoteMethods() {
        return remoteMethods;
    }
}
