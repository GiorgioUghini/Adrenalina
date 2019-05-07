package views;

import controllers.LobbyController;
import network.ConnectionType;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Scanner;

public class LobbyViewCLI implements LobbyView {
    private LobbyController lobbyController;
    Scanner scanner = new Scanner(System.in);

    public LobbyViewCLI(){
        this.lobbyController = new LobbyController();
    }

    @Override
    public void createConnection() {
        print("Choose connection type:\n1: Socket\n2: rmi\nyour choice: ");
        int connType = scanner.nextInt() - 1;
        scanner.nextLine();
        ConnectionType type = ConnectionType.values()[connType];
        try{
            lobbyController.createConnection(type);
        }catch (Exception e) {
            e.printStackTrace();
        }
        registerPlayer();
    }

    @Override
    public void registerPlayer(){
        print("Insert username: ");
        String username = scanner.nextLine();
        try {
            lobbyController.registerPlayer(username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startGame() {

    }

    @Override
    public void printError() {

    }

    private void print(String message){
        System.out.print(message);
    }
    private void println(String message){
        System.out.println(message);
    }
}
