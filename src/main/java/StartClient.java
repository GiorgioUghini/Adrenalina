import errors.NotImplementedException;
import views.LobbyView;
import views.LobbyViewCLI;
import views.ViewType;

import java.util.Scanner;

public class StartClient {
    public static void main(String[] args) {
        LobbyView lobbyView;
        System.out.print("Which view?\n1) CLI\n2) GUI\nYour choice: ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt() - 1;
        ViewType viewType = ViewType.values()[choice];
        if(viewType==ViewType.CLI){
            lobbyView = new LobbyViewCLI();
        }else{
            throw new NotImplementedException();
        }
        lobbyView.createConnection();
    }
}
