package views;

import models.card.LegitEffects;
import models.card.PowerUpCard;
import models.card.Selectable;
import models.card.WeaponCard;
import models.map.GameMap;
import models.player.Player;
import models.turn.ActionType;
import models.turn.TurnEvent;
import network.Client;
import utils.Console;

import java.util.List;
import java.util.Map;

import static utils.Console.*;
import static utils.Console.println;

public class GameViewCLI implements GameView {

    public GameViewCLI() {
        printMapNum(Client.getInstance().getMapNum());
    }

    @Override
    public void chooseSpawnPoint() {
        //Forse deve stare vuoto
    }

    @Override
    public void getValidActions() {

    }

    @Override
    public void reconnect() {

    }

    @Override
    public void startTurn(String name) {

    }

    @Override
    public void updateMapView(GameMap map) {

    }

    @Override
    public void updatePlayerView(Player newPlayer) {

    }

    @Override
    public void updateDamagedAndMarkedPlayer(Player newPlayer) {

    }

    @Override
    public void effectChoosingDialog(LegitEffects legitEffects) {

    }

    @Override
    public void updateActions(Map<ActionType, List<TurnEvent>> actions) {

    }

    @Override
    public void setTextAndEnableBtnActionGroup(ActionType actionType, int btnNum) {

    }

    @Override
    public void selectTag(Selectable selectable) {

    }

    @Override
    public void continueWeapon() {

    }

    @Override
    public void onEndWeapon() {

    }

    @Override
    public void printError(String error) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void onNewPlayer(String playerName) {

    }

    @Override
    public void onPlayerDisconnected(String name) {

    }

    private void printMapNum(int num) {
        println("╔═══════════════════════════════════════╗");
        println("║             ADRENALINE MAP            ║");
        println("╠═════════╦═════════╦═════════╦═════════╣");
        switch (num) {
            case 0:     //TODO: I think all this map is not correct
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
