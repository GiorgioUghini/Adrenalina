package network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controllers.ResourceController;
import models.Config;
import models.Lobby;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static Server instance = null;
    private ServerConnection connection;
    private Config config;
    private Lobby lobby;
    private int registryPort;
    private int socketPort;
    private String hostname;
    private int maxClients;
    private boolean debug;
    private boolean autoReload;

    private Server() {
        maxClients = 5;
        debug = false;
        autoReload = false;
    }

    /**
     * Method that creates or directly return the singleton instance
     *
     * @return the instance of the singleton class "Server"
     */
    public static Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    public void start(String hostname, int socketPort, int registryPort) throws IOException {
        this.registryPort = registryPort;
        this.socketPort = socketPort;
        this.hostname = hostname;
        InputStream file = ResourceController.getResource("config.json");
        String jsonConfig = new String(file.readAllBytes());
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        config = gson.fromJson(jsonConfig, Config.class);
        lobby = new Lobby(config);
        connection = new ServerConnection();
        connection.init();
    }

    public void fatalError(Exception e) {
        Logger.getAnonymousLogger().log(Level.SEVERE, e.getMessage());
        System.exit(1);
    }

    public ServerConnection getConnection() {
        return connection;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public int getMaxClients() {
        return maxClients;
    }

    public void setMaxClients(int maxClients) {
        this.maxClients = maxClients;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean isDebug) {
        this.debug = isDebug;
    }

    public boolean isAutoReload() {
        return autoReload;
    }

    public void setAutoReload(boolean autoReload) {
        this.autoReload = autoReload;
    }

    public int getRegistryPort() {
        return registryPort;
    }

    public int getSocketPort() {
        return socketPort;
    }

    public String getHostname() {
        return hostname;
    }
}
