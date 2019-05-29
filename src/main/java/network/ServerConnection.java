package network;

import network.updates.NewPlayerUpdate;
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
    private BiMap<String, ConnectionWrapper> connectionWrapperMap;
    private boolean close;

    public ServerConnection() throws IOException {
        close = false;
        pool = Executors.newCachedThreadPool();
        serverSocket = new ServerSocket(Constants.PORT);
        connectionWrapperMap = new BiMap<>();

        registry = LocateRegistry.createRegistry(Constants.REGISTRY_PORT);
        registry.rebind(Constants.REGISTRY_NAME, new RemoteMethods());
    }

    public void init() throws IOException {
        while (!close) {
            Socket clientSocket = serverSocket.accept();
            SocketWrapper socketWrapper = new SocketWrapper(clientSocket);
            UpdatePusher updatePusher = new UpdatePusher(socketWrapper);
            socketWrapper.setUpdatePusher(updatePusher);
            (new Thread(updatePusher)).start();
            pool.submit(new ClientListener(socketWrapper));
        }
    }

    public void close() throws IOException {
        close = true;
        serverSocket.close();
        pool.shutdown();
        UnicastRemoteObject.unexportObject(registry, true);
    }

    public void addConnectionWrapper(String token, ConnectionWrapper connectionWrapper){
        connectionWrapperMap.add(token, connectionWrapper);
    }

    public void removeConnection(String token){
        connectionWrapperMap.removeByKey(token);
    }

    public void removeConnection(ConnectionWrapper connectionWrapper){
        connectionWrapperMap.removeByValue(connectionWrapper);
    }

    public ConnectionWrapper getConnectionWrapper(String token){
        ConnectionWrapper connectionWrapper = connectionWrapperMap.getSingleValue(token);
        return connectionWrapper;
    }

    public SocketWrapper getSocketWrapper(String token){
        SocketWrapper socketWrapper = (SocketWrapper) connectionWrapperMap.getSingleValue(token);
        return socketWrapper;
    }

    public RMIWrapper getRMIWrapper(String token){
        RMIWrapper rmiWrapper = (RMIWrapper) connectionWrapperMap.getSingleValue(token);
        return rmiWrapper;
    }

    public Registry getRegistry(){
        return registry;
    }

    public String getToken(SocketWrapper socketWrapper){
        String token = connectionWrapperMap.getSingleKey(socketWrapper);
        return token;
    }

    public String getToken(RMIWrapper rmiWrapper){
        String token = connectionWrapperMap.getSingleKey(rmiWrapper);
        return token;
    }
}
