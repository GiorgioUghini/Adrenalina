package network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controllers.ResourceController;
import models.Config;
import models.Lobby;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static Server instance = null;
    private ServerConnection connection;
    private Config config;
    private Lobby lobby;
    private int maxClients;
    private boolean debug;

    private Server() {
        maxClients = 5;
        debug = false;
    }

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
        File file = ResourceController.getResource("config.json");
        String jsonConfig = new String(Files.readAllBytes(file.toPath()));
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        config = gson.fromJson(jsonConfig, Config.class);
        lobby = new Lobby(config);
        connection = new ServerConnection();
        connection.init();
    }

    public void fatalError(Exception e){
        Logger.getAnonymousLogger().log(Level.SEVERE, e.getMessage());
        System.exit(1);
    }
  
    public ServerConnection getConnection(){
        return connection;
    }

    public Lobby getLobby(){
        return lobby;
    }

    public void setMaxClients(int maxClients){
        this.maxClients = maxClients;
    }

    public int getMaxClients(){
        return maxClients;
    }

    public void setDebug(boolean isDebug){
        this.debug = isDebug;
    }

    public boolean isDebug(){
        return debug;
    }
}
