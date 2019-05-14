package network;

import utils.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerConnection {
    private ServerSocket serverSocket;
    private ExecutorService pool;
    private Registry registry;
    private boolean close;

    public ServerConnection() throws IOException {
        close = false;
        pool = Executors.newCachedThreadPool();
        serverSocket = new ServerSocket(Constants.PORT);

        registry = LocateRegistry.createRegistry(Constants.REGISTRY_PORT);
        registry.rebind(Constants.REGISTRY_NAME, new RemoteMethods());
    }

    public void init() throws IOException {
        while (!close) {
            Socket clientSocket = serverSocket.accept();
            pool.submit(new ClientListener(clientSocket));
        }
    }

    public void close() throws IOException {
        close = true;
        serverSocket.close();
        pool.shutdown();
        UnicastRemoteObject.unexportObject(registry, true);
    }
}
