package network;

import utils.BiMap;
import utils.Constants;
import utils.TokenGenerator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerConnection {
    private ServerSocket serverSocket;
    private ExecutorService pool;
    private Registry registry;
    private BiMap<String, SocketWrapper> socketMap;
    private boolean close;

    public ServerConnection() throws IOException {
        close = false;
        pool = Executors.newCachedThreadPool();
        serverSocket = new ServerSocket(Constants.PORT);
        socketMap = new BiMap<>();

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

    public void addSocket(String token, SocketWrapper socketWrapper){
        socketMap.add(token, socketWrapper);
    }

    public SocketWrapper getSocketWrapper(String token){
        SocketWrapper socketWrapper = socketMap.getSingleValue(token);
        return socketWrapper;
    }

    public boolean isSocket(String token){
        SocketWrapper socketWrapper = socketMap.getSingleValue(token);
        boolean isSocket = socketWrapper != null;
        return isSocket;
    }

    public Registry getRegistry(){
        return registry;
    }

    public String getToken(SocketWrapper socketWrapper){
        String token = socketMap.getSingleKey(socketWrapper);
        return token;
    }
}
