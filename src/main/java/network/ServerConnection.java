package network;

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
    private Map<String, SocketWrapper> tokenSocketMap;
    private Map<SocketWrapper, String> socketTokenMap;
    private boolean close;

    public ServerConnection() throws IOException {
        close = false;
        pool = Executors.newCachedThreadPool();
        serverSocket = new ServerSocket(Constants.PORT);
        tokenSocketMap = new HashMap<>();
        socketTokenMap = new HashMap<>();

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

    public void addSocket(String token, SocketWrapper socket){
        tokenSocketMap.put(token, socket);
        socketTokenMap.put(socket, token);
    }

    public SocketWrapper getSocket(String token){
        SocketWrapper socket = tokenSocketMap.get(token);
        return socket;
    }

    public String getToken(Socket socket){
        String token = socketTokenMap.get(socket);
        return token;
    }
}
