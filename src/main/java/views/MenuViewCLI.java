package views;

import controllers.MenuController;
import network.ConnectionType;
import utils.Console;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuViewCLI implements MenuView {
    private MenuController menuController;
    private List<String> players = new ArrayList<>();
    private String username = "";

    public MenuViewCLI(){
        this.menuController = new MenuController();
    }

    /**{@inheritDoc}*/
    @Override
    public void createConnection() {
        Console.println("Choose connection type:");
        Console.println("1) Socket");
        Console.println("2) RMI");
        Console.print("Choice: ");
        int connType = Console.nextInt() - 1;
        ConnectionType type = ConnectionType.values()[connType];
        try{
            menuController.createConnection(type);
        }catch (Exception e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.SEVERE, "an exception was thrown", e);
        }
        registerPlayer();
    }

    /**{@inheritDoc}*/
    @Override
    public void registerPlayer(){
        Console.print("Insert username: ");
        while((username = Console.nextLine()).equals("")){
            Console.println("Username cannot be null");
            Console.print("Insert username: ");
        }
        Console.println("Welcome, " + username);
        menuController.registerPlayer(username);
    }

    /**{@inheritDoc}*/
    @Override
    public void registrationCompleted(List<String> players){
        Console.println("You joined a lobby!");
        Console.println("Other players in this lobby:");
        for(String name : players){
            if(!name.equals(username)) Console.println(name);
            players.add(name);
        }
        printGameSituation();
    }

    /**{@inheritDoc}*/
    @Override
    public void onNewPlayer(String playerName){
        Console.println(playerName + " joined the Lobby");
        players.add(playerName);
        printGameSituation();
    }

    /**{@inheritDoc}*/
    @Override
    public void onPlayerDisconnected(String name){
        Console.println(name + "left the room");
        players.remove(name);
        printGameSituation();
    }

    /**{@inheritDoc}*/
    @Override
    public void startGame() {
        Console.println("The game is starting");
    }

    /**{@inheritDoc}*/
    @Override
    public void printError(String error) {
        Console.println(error);
    }

    @Override
    public void showMessage(String message) {
        Console.println(message);
    }

    private void printGameSituation(){
        if(players.size()==5){
            Console.println("There are 5 players in this lobby, the match is starting now");
        }else if(players.size() >= 3){
            Console.println("The match will start in 5 seconds...");
        }else{
            Console.println("The match will start when " + (3-players.size()) + " more player(s) will connect");
        }
    }
}