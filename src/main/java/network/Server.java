package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private ServerSocket serverSocket;
    private ExecutorService pool;
    private Registry registry;
    private boolean stop;

    public Server(int port) throws IOException {
        stop = false;
        pool = Executors.newCachedThreadPool();
        serverSocket = new ServerSocket(port);

        registry = LocateRegistry.createRegistry(1099);
        registry.rebind("RemoteMethods", new RemoteMethods());
    }

    public void start() throws IOException {
        while (!stop) {
            Socket clientSocket = serverSocket.accept();
            pool.submit(new ClientListener(clientSocket));
        }
    }

    public void stop() throws IOException {
        stop = true;
        serverSocket.close();
        pool.shutdown();
        UnicastRemoteObject.unexportObject(registry, true);
    }
}
