package network;

import utils.BiMap;
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
    private BiMap<String, SocketWrapper> socketMap;
    private BiMap<String, RMIWrapper> rmiMap;
    private boolean close;

    public ServerConnection() throws IOException {
        close = false;
        pool = Executors.newCachedThreadPool();
        serverSocket = new ServerSocket(Constants.PORT);
        socketMap = new BiMap<>();
        rmiMap = new BiMap<>();

        registry = LocateRegistry.createRegistry(Constants.REGISTRY_PORT);
        registry.rebind(Constants.REGISTRY_NAME, new RemoteMethods());
    }

    public void init() throws IOException {
        while (!close) {
            Socket clientSocket = serverSocket.accept();
            SocketWrapper socketWrapper = new SocketWrapper(clientSocket);
            pool.submit(new ClientListener(socketWrapper));
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

    public void addRMI(String token,RMIWrapper rmiWrapper){
        rmiMap.add(token, rmiWrapper);
    }

    public void removeSocket(String token){
        socketMap.removeByKey(token);
    }

    public void removeRMI(String token){
        rmiMap.removeByKey(token);
    }

    public SocketWrapper getSocketWrapper(String token){
        SocketWrapper socketWrapper = socketMap.getSingleValue(token);
        return socketWrapper;
    }

    public RMIWrapper getRMIWrapper(String token){
        RMIWrapper rmiWrapper = rmiMap.getSingleValue(token);
        return rmiWrapper;
    }

    public boolean isSocket(String token){
        SocketWrapper socketWrapper = socketMap.getSingleValue(token);
        boolean isSocket = socketWrapper != null;
        return isSocket;
    }

    public boolean isRMI(String token){
        RMIWrapper rmiWrapper = rmiMap.getSingleValue(token);
        boolean isRMI = rmiWrapper != null;
        return isRMI;
    }

    public Registry getRegistry(){
        return registry;
    }

    public String getToken(SocketWrapper socketWrapper){
        String token = socketMap.getSingleKey(socketWrapper);
        return token;
    }
}
