package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Client {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Registry registry;
    private RemoteMethodsInterface remoteMethods;
    private BlockingQueue<Update> queue;
    private UpdateHandler updateHandler;

    public Client(String host, int port) throws IOException, NotBoundException {
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

        queue = new LinkedBlockingQueue<>(100);
        registry = LocateRegistry.getRegistry();
        remoteMethods = (RemoteMethodsInterface) registry.lookup("RemoteMethods");
        updateHandler = new UpdateController();
    }

    public void start() throws IOException, InterruptedException {
        ServerListener serverListener = new ServerListener(in);
        (new Thread(serverListener)).start();

        LongPollingTask longPollingTask = new LongPollingTask(remoteMethods, queue);
        Timer timer = new Timer();
        timer.schedule(longPollingTask, 0, 2);

        Update update = null;
        while(true){
            update = queue.take();
            update.handle(updateHandler);
        }
    }

    public ObjectOutputStream getOutputStream(){
        return out;
    }

    public ObjectInputStream getInputStream(){
        return in;
    }

    public Socket getSocket(){
        return socket;
    }

    public RemoteMethodsInterface getRemoteMethods(){
        return remoteMethods;
    }
}
