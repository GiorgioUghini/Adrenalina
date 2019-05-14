package network;

import models.Lobby;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static Server instance = null;
    private ServerConnection connection;

    private Server() {}

    /**
     * Method that creates or directly return the singleton instance
     * @return the instance of the singleton class "Server"
     */
    public static Server getInstance()
    {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    public void start() throws IOException {
        connection = new ServerConnection();
        connection.init();
    }

    public ServerConnection getConnection(){
        return connection;
    }
}
