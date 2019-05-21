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
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static Server instance = null;
    private ServerConnection connection;
    private Lobby lobby;

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

    public Lobby getLobby(){
        return lobby;
    }

    public void start() throws IOException {
        lobby = new Lobby();
        connection = new ServerConnection();
        connection.init();
    }

    public void fatalError(Exception e){
        Logger.getAnonymousLogger().log(Level.SEVERE, e.getMessage());
        System.exit(1);
    }
}
