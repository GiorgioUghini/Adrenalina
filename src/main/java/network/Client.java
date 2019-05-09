package network;

import errors.InvalidViewTypeException;
import errors.NotImplementedException;
import views.GameView;
import views.LobbyView;
import views.LobbyViewCLI;
import views.ViewType;

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

    ViewType viewType;
    LobbyView lobbyView;
    GameView gameView;

    public Client(ViewType viewType) throws IOException, NotBoundException {
        this.viewType = viewType;
    }

    public void start() throws IOException, InterruptedException {
        if(viewType == ViewType.CLI){
            lobbyView = new LobbyViewCLI();
            //gameView = new GameViewCLI(); Later
            lobbyView.createConnection();
            Thread.currentThread().join(); // ??
        }
        else if(viewType == ViewType.GUI){
            //TODO implementation GUI
            throw new NotImplementedException();
        }
        else{
            throw new InvalidViewTypeException();
        }
    }
}
