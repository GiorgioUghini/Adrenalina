package network;

import errors.InvalidConnectionTypeException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Connection {

    private static Connection instance = null;

    private ConnectionType type;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private UpdateHandlerInterface updateHandler;

    private RemoteMethodsInterface remoteMethods;
    private Registry registry;
    private BlockingQueue<Update> queue;

    private String token;

    private Connection() {
    }

    void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

    public static Connection getInstance() {
        if (instance == null)
            instance = new Connection();
        return instance;
    }

    public void initSocket(String host, int port) throws IOException {
        type = ConnectionType.Socket;
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        ServerListener serverListener = new ServerListener();
        (new Thread(serverListener)).start();
    }

    public void initRMI() throws RemoteException, NotBoundException, InterruptedException {
        type = ConnectionType.RMI;
        registry = LocateRegistry.getRegistry();
        remoteMethods = (RemoteMethodsInterface) registry.lookup("RemoteMethods");
        queue = new LinkedBlockingQueue<>(100);
        LongPollingTask longPollingTask = new LongPollingTask(remoteMethods, queue);
        Timer timer = new Timer();
        timer.schedule(longPollingTask, 0, 2);
        PollingQueueListener pollingQueueListener = new PollingQueueListener();
        (new Thread(pollingQueueListener)).start();
    }

    public ObjectOutputStream getOutputStream() {
        if (type == ConnectionType.Socket)
            return out;
        else
            throw new InvalidConnectionTypeException();
    }


    public ObjectInputStream getInputStream() {
        if (type == ConnectionType.Socket)
            return in;
        else
            throw new InvalidConnectionTypeException();
    }

    public Socket getSocket() {
        if (type == ConnectionType.Socket)
            return socket;
        else
            throw new InvalidConnectionTypeException();
    }

    public RemoteMethodsInterface getRemoteMethods() {
        if (type == ConnectionType.RMI)
            return remoteMethods;
        else
            throw new InvalidConnectionTypeException();
    }

    public ConnectionType getType(){
        return type;
    }
}