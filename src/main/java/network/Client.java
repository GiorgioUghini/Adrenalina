package network;

import errors.InvalidViewTypeException;
import errors.NotImplementedException;
import errors.SingletionViolationException;
import views.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Client {

    private static Client instance = null;

    private Connection connection;
    private ViewType viewType;
    private LobbyView lobbyView;
    private GameView gameView;
    private View currentView;

    public static Client createInstance(ViewType viewType) {
        if (instance == null){
            instance = new Client(viewType);
            return instance;
        }
        throw new SingletionViolationException();
    }

    public static Client getInstance() {
        return instance;
    }

    private Client(ViewType viewType) {
        this.viewType = viewType;
    }

    public void start() throws InterruptedException {
        if(viewType == ViewType.CLI){
            lobbyView = new LobbyViewCLI();
            //gameView = new GameViewCLI(); Later

        }
        else if(viewType == ViewType.GUI){
            //TODO implementation GUI
            throw new NotImplementedException();
        }
        else{
            throw new InvalidViewTypeException();
        }
        currentView = lobbyView;
        lobbyView.createConnection();
        Thread.currentThread().join();
    }

    public void setConnection(Connection connection){
        this.connection = connection;
    }

    public Connection getConnection(){
        return connection;
    }

    public View getCurrentView(){
        return currentView;
    }
}
