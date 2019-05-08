import views.LobbyView;
import views.LobbyViewCLI;
import views.ViewType;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Scanner;

public class StartClient {
    public static void main(String[] args) throws IOException, NotBoundException, InterruptedException {
        LobbyView lobbyView;
        System.out.print("Which view?\n1) CLI\n2) GUI");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt() - 1;
        ViewType viewType = ViewType.values()[choice];
        if(viewType==ViewType.CLI){
            lobbyView = new LobbyViewCLI();
        }else{
            throw new RuntimeException("Sorry mate, wrong path");
        }
        lobbyView.createConnection();
    }
}
