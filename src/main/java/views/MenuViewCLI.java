package views;

import controllers.MenuController;
import network.ConnectionType;
import utils.Console;

import java.util.ArrayList;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;

import static utils.Console.*;

public class MenuViewCLI implements MenuView {
    private MenuController menuController;
    private List<String> players = new ArrayList<>();
    private String username = "";
    private String password = "";

    public MenuViewCLI(){
        this.menuController = new MenuController();
    }

    @Override
    public void startView() {
        this.createConnection();
    }

    /**{@inheritDoc}*/
    @Override
    public void createConnection() {
        Console.println("Choose connection type:");
        Console.println("1) Socket");
        Console.println("2) RMI");
        print("Choice: ");
        int connType = Console.nextInt() - 1;
        ConnectionType type = ConnectionType.values()[connType];
        try{
            menuController.createConnection(type);
        }catch (Exception e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.SEVERE, "an exception was thrown", e);
        }
    }

    @Override
    public void connectionCreated() {
        Console.println("Connection created.");
        menuController.getWaitingPlayer();
    }

    /**{@inheritDoc}*/
    @Override
    public void registerPlayer(){
        print("Insert username: ");
        while((username = Console.nextLine()).equals("")){
            Console.println("Username cannot be null");
            print("Insert username: ");
        }
        print("Insert password: ");
        while((password = Console.nextLine()).equals("")){
            Console.println("Password cannot be null");
            print("Insert password: ");
        }
        Console.println("Welcome, " + username);
        menuController.registerPlayer(username, password);
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

    @Override
    public void showWaitingPlayerList(List<String> waitingPlayerUsername) {
        for (String name : waitingPlayerUsername) {
            Console.println(name + " joined the Lobby");
            players.add(name);
        }
        printGameSituation();
        registerPlayer();
    }

    @Override
    public void mapChosen(int map) {
        printMapNum(map);
    }

    @Override
    public void chooseMap(String username) {
        if (username.equals(this.username)) {
            int mapNum;
            print("Insert the map number you've chosen (1-4): ");
            mapNum = Console.nextInt();
            while((mapNum > 4) || (mapNum < 1)){
                Console.println("Invalid map.");
                print("Insert the map number you've chosen (1-4): ");
                mapNum = Console.nextInt();
            }
            menuController.chooseMap(mapNum);
            //TODO: ADD TIMER (IN SERVER), AFTER IT TIMERS CHOOSE RANDOMLY
        }
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
            Console.println("The match will start in 5 seconds when " + (3-players.size()) + " more player(s) will connect");
        }
    }

    private void printMapNum(int num) {
        println("╔═══════════════════════════════════════╗");
        println("║             ADRENALINE MAP            ║");
        println("╠═════════╦═════════╦═════════╦═════════╣");
        switch (num) {
            case 0:
                print("║         ║ "); printColor("CELL 12", COLOR.BLU); print("   "); printColor("CELL 22", COLOR.BLU);  print("   "); printColor("CELL 32", COLOR.BLU);  println(" ║ ");
                println("╠═════════╬═══   ═══╬═════════╬═══   ═══╬");
                print("║ "); printColor("CELL 01", COLOR.YELLOW); print("   "); printColor("CELL 11", COLOR.PURPLE); print(" ║ "); printColor("CELL 21", COLOR.RED);  print(" ║ "); printColor("CELL 22", COLOR.RED);  println(" ║ ");
                println("╠═════════╬═════════╬═══   ═══╬═════════╣");
                print("║ "); printColor("CELL 00", COLOR.YELLOW); print("   "); printColor("CELL 10", COLOR.WHITE); print(" ║ "); printColor("CELL 20", COLOR.WHITE);  print(" ║ "); println("        ║ ");
                println("╚═════════╩═════════╩═════════╩═════════╝");
                break;
            case 1:
                print("║ "); printColor("CELL 02", COLOR.BLU); print("   "); printColor("CELL 12", COLOR.BLU); print("   "); printColor("CELL 22", COLOR.BLU);  print("   "); printColor("CELL 32", COLOR.GREEN);  println(" ║ ");
                println("╠═══   ═══╬═════════╬═══   ═══╬═══   ═══╬");
                print("║ "); printColor("CELL 01", COLOR.RED); print("   "); printColor("CELL 11", COLOR.RED); print(" ║ "); printColor("CELL 21", COLOR.YELLOW);  print(" ║ "); printColor("CELL 31", COLOR.YELLOW);  println(" ║ ");
                println("╠═════════╬═══   ═══╬═══   ═══╬═══   ═══╣");
                print("║         ║ "); printColor("CELL 00", COLOR.WHITE); print("   "); printColor("CELL 10", COLOR.YELLOW);  print("   "); printColor("CELL 20", COLOR.YELLOW);  println(" ║ ");
                println("╚═════════╩═════════╩═════════╩═════════╝");
                break;
            case 2:
                print("║ "); printColor("CELL 02", COLOR.RED); print("   "); printColor("CELL 12", COLOR.BLU); print("   "); printColor("CELL 22", COLOR.BLU);  print("   "); printColor("CELL 32", COLOR.GREEN);  println(" ║ ");
                println("╠═══   ═══╬═══   ═══╬═══   ═══╬═══   ═══╬");
                print("║ "); printColor("CELL 01", COLOR.RED); print(" ║ "); printColor("CELL 11", COLOR.PURPLE); print(" ║ "); printColor("CELL 21", COLOR.YELLOW);  print("   "); printColor("CELL 31", COLOR.YELLOW);  println(" ║ ");
                println("╠═══   ═══╬═══   ═══╬═══   ═══╬═══   ═══╣");
                print("║ "); printColor("CELL 00", COLOR.WHITE); print("   "); printColor("CELL 10", COLOR.WHITE); print("   "); printColor("CELL 20", COLOR.YELLOW);  print("   "); printColor("CELL 30", COLOR.YELLOW);  println(" ║ ");
                println("╚═════════╩═════════╩═════════╩═════════╝");
                break;
            case 3:
                print("║         ║ "); printColor("CELL 12", COLOR.BLU); print("   "); printColor("CELL 22", COLOR.BLU);  print("   "); printColor("CELL 32", COLOR.RED);  println(" ║ ");
                println("╠═════════╬═══   ═══╬═══   ═══╬═══   ═══╬");
                print("║ "); printColor("CELL 01", COLOR.YELLOW); print("   "); printColor("CELL 11", COLOR.PURPLE); print("   "); printColor("CELL 21", COLOR.PURPLE);  print(" ║ "); printColor("CELL 31", COLOR.RED);  println(" ║ ");
                println("╠═══   ═══╬═════════╬═══   ═══╬═══   ═══╣");
                print("║ "); printColor("CELL 00", COLOR.YELLOW); print("   "); printColor("CELL 10", COLOR.WHITE); print("   "); printColor("CELL 20", COLOR.WHITE);  print("   "); printColor("CELL 30", COLOR.WHITE);  println(" ║ ");
                println("╚═════════╩═════════╩═════════╩═════════╝");
                break;
        }
    }
}
