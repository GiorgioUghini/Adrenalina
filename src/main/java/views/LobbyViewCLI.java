package views;

import controllers.LobbyController;
import network.ConnectionType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LobbyViewCLI implements LobbyView {
    private LobbyController lobbyController;
    private Scanner scanner = new Scanner(System.in);
    private List<String> players = new ArrayList<>();
    private String username = "";

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
        while((username = scanner.nextLine()).equals("")){
            println("Username cannot be null");
            print("Insert username: ");
        }
        println("Welcome, " + username);
        try {
            lobbyController.registerPlayer(username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registrationCompleted(ArrayList<String> players){
        println("You joined a lobby!\nOther players in this lobby: ");
        for(String name : players){
            if(!name.equals(username)) println(name);
            players.add(name);
        }
        printGameSituation();
    }

    @Override
    public void onNewPlayer(String playerName){
        println(playerName + " joined the Lobby");
        players.add(playerName);
        printGameSituation();
    }

    @Override
    public void onPlayerDisconnected(String name){
        println(name + "left the room");
        players.remove(name);
        printGameSituation();
    }

    @Override
    public void startGame() {
        println("The game is starting");
    }

    @Override
    public void printError(String error) {
        println(error);
    }

    private void printGameSituation(){
        if(players.size()==5){
            println("There are 5 players in this lobby, the match is starting now");
        }else if(players.size() >= 3){
            println("The match will start in 5 seconds...");
        }else{
            println("The match will start when " + (3-players.size()) + " more player(s) will connect");
        }
    }

    private void print(String message){
        System.out.print(message);
    }
    private void println(String message){
        System.out.println(message);
    }
}
