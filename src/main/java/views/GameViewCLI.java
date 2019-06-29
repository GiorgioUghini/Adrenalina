package views;

import controllers.GameController;
import errors.PlayerNotOnMapException;
import models.card.*;
import models.map.*;
import models.player.Ammo;
import models.player.Player;
import models.turn.ActionType;
import models.turn.TurnEvent;
import network.Client;
import utils.Console;

import java.util.*;

import static utils.Console.*;
import static utils.Console.println;

public class GameViewCLI implements GameView {

    private int maxRunDistance;
    private HashMap<Integer, ActionType> buttonActionTypeMap = new HashMap<>();
    private boolean isShooting;

    private Map<ViewAction, Boolean> canDoActionMap = new EnumMap<>(ViewAction.class);

    public GameController gameController;

    public GameViewCLI() {
        printMapNum(Client.getInstance().getMapNum());
        this.gameController = new GameController();
        canDoActionMap.put(ViewAction.USEPOWERUP, false);
        canDoActionMap.put(ViewAction.CLICKPOWERUPSPAWN, false);
        canDoActionMap.put(ViewAction.CHOOSESPAWNPOINTWEAPON, false);
        canDoActionMap.put(ViewAction.SHOOT, false);
        canDoActionMap.put(ViewAction.SELECTPLAYER, false);
        canDoActionMap.put(ViewAction.SELECTROOM, false);
        canDoActionMap.put(ViewAction.SELECTSQUARE, false);
        canDoActionMap.put(ViewAction.RUN, false);
    }

    @Override
    public void getValidActions() {
        gameController.getValidActions();
    }

    @Override
    public void reconnect() {

    }

    @Override
    public void startTurn(String name) {
        if (Client.getInstance().getPlayer().getName().equals(name)) {
            gameController.getValidActions();
        }
    }

    @Override
    public void updateMapView(GameMap map) {
        Console.println("");
        Console.println("");
        printMapNum(Client.getInstance().getMapNum());
        Client client = Client.getInstance();
        //UPDATE PLAYERS POSITIONS
        for (Player p : client.getPlayers()) {
            try {
                Coordinate c = map.getPlayerCoordinates(p);
                Console.println(String.format("Player %s is on CELL%d%d", p.getStringColor(), c.getX(), c.getX()));
            }
            catch (PlayerNotOnMapException e) {
                //Nothing to do, just don't draw it.
            }
        }

        //ADD NEW WEAPONS
        for (SpawnPoint sp : map.getSpawnPoints()) {
            Console.println(String.format("Spawn point %s contains the following weapons:", sp.getColor()));
            for (WeaponCard card : sp.showCards()) {
                Console.print(card + ", ");
            }
            Console.println("");
        }

        //UPDATE AMMOs
        for (int x = 0; x<4; x++) {
            for (int y=0; y<3; y++) {
                Square square = map.getSquareByCoordinate(x, y);
                if (square == null) continue;
                Console.println(String.format("CELL%d%d contains the following ammos: ", x, y));
                AmmoPoint ammoPoint = (AmmoPoint) square;
                AmmoCard ammoCard = ammoPoint.showCard();
                if (ammoCard==null) continue;
                Console.print(String.format("Red: %d, Blue: %d, Yellow: %d", ammoCard.getRed(), ammoCard.getBlue(), ammoCard.getYellow()));
            }
        }
    }

    @Override
    public void updatePlayerView(Player newPlayer) {
        //UPDATE AMMO
        Ammo myAmmo = newPlayer.getAmmo();
        Console.println("AMMO: You have ");
        Console.print(String.format("%d Red, %d Blue and %d Yellow", myAmmo.red, myAmmo.blue, myAmmo.yellow));
        //ADD NEW POWERUP
        Console.println("POWER UP: You have ");
        for (PowerUpCard powerUpCard : newPlayer.getPowerUpList()) {
            Console.print(powerUpCard.name + " ");
        }
        Console.println("WEAPONS: You have ");
        //ADD NEW WEAPONS
        for (WeaponCard weaponCard : newPlayer.getWeaponList()) {
            Console.print(weaponCard.name + " ");
        }
    }

    private void addSkullsOnMainPane(Player newPlayer) {
        int skulls = 0;
        if (newPlayer.getTotalDamage() > 10) {
            skulls += 1;
        }
        for (Player p : Client.getInstance().getPlayers()) {
            skulls += p.getDeathCount();
        }
        Console.println(String.format("Skulls already taken in this match from all players: %d", skulls));
    }

    @Override
    public void effectChoosingDialog(LegitEffects legitEffects) {
        Console.println("Which effect do you want to use now?");

        int i = 0;
        for (; i<legitEffects.getLegitEffects().size(); i++) {
            Effect effect = legitEffects.getLegitEffects().get(i);
            Console.println(String.format("%d) %s", i, effect.name));
        }
        Console.println(i + ") I don't want to use any effect. ");
        Console.println("Choose one.");
        int choose = Console.nextInt();
        while ((choose > legitEffects.getLegitEffects().size()) || choose<0) {
            Console.print("Number not valid. Choose one.");
            choose = Console.nextInt();
        }

        if (choose == legitEffects.getLegitEffects().size()) {
            gameController.finishCard();
        } else {
            //Would you like to pay with power up?
            PowerUpCard toPay = choosePowerUpDialog();
            gameController.playEffect(legitEffects.getLegitEffects().get(choose), toPay);
        }
    }

    private PowerUpCard choosePowerUpDialog() {
        if(Client.getInstance().getPlayer().getPowerUpList().isEmpty()) return null;
        Console.println("Do you want to use a power up to pay for this action?");

        Player me = Client.getInstance().getPlayer();
        List<PowerUpCard> powerUpCards = me.getPowerUpList();

        int i = 0;
        for (; i<powerUpCards.size(); i++) {
            PowerUpCard powerUpCard = powerUpCards.get(i);
            Console.println(String.format("%d) %s", i, powerUpCard.getFullName()));
        }
        Console.println(i + ") I don't want to pay with power up. ");
        Console.println("Choose one.");
        int choose = Console.nextInt();
        while ((choose > powerUpCards.size()+1) || choose<0) {
            Console.print("Number not valid. Choose one.");
            choose = Console.nextInt();
        }

        if(choose == powerUpCards.size()) return null;

        return powerUpCards.get(choose);
    }

    private void setBtnEnabled(String string, boolean isVisible){
        if (isVisible) {
            Console.println("You can " + string);
        }
    }

    private void setActionGroupButtons(Set<ActionType> groupActions){
        if(groupActions.size() > 1 && Client.getInstance().getCurrentActionType()==null){
            for(ActionType groupAction : groupActions){
                Console.println("Action group: " + groupAction.name());
            }
        }
    }

    private void setTurnEventButtons(List<TurnEvent> turnEvents){
        for (TurnEvent turnEvent : turnEvents) {
            String buttonToShow = null;
            switch (turnEvent) {
                case DRAW:
                    buttonToShow = "DRAW";
                    break;
                case RUN_1:
                case RUN_2:
                case RUN_3:
                case RUN_4:
                    this.maxRunDistance = Character.getNumericValue(turnEvent.toString().charAt(4));
                    buttonToShow = "RUN " + maxRunDistance + " spaces";
                    break;
                case SHOOT:
                    buttonToShow = "SHOOT";
                    break;
                case RELOAD:
                    buttonToShow = "RELOAD";
                    break;
                case SPAWN:
                    if(turnEvents.size()==1){
                        buttonToShow = "SPAWN";
                    }
                    break;
                case GRAB:
                    buttonToShow = "GRAB";
                    break;
            }
            setBtnEnabled(buttonToShow, true);
        }
    }

    @Override
    public void updateActions(Map<ActionType, List<TurnEvent>> actions) {
        Client client = Client.getInstance();
        ActionType currentActionType = client.getCurrentActionType();

        boolean turnIsEnding = actions.isEmpty() && client.isMyTurn();

        setBtnEnabled("End Turn", turnIsEnding);
        //this must be called before setTurnEventButtons or it breaks the frenzy reload
        setBtnEnabled("Reload", turnIsEnding);

        if(currentActionType==null){
            setActionGroupButtons(actions.keySet());
        } else {
            setTurnEventButtons(actions.get(currentActionType));
        }
    }

    @Override
    public void onDamage(Player damagedPlayer) {
        //ADD DAMAGE
        Console.println(String.format("Player %swas hurt by ", damagedPlayer.getStringColor()));
        for (Player from : damagedPlayer.getDamagedBy()) {
            Console.print(String.format("Player %s ", from.getStringColor()));
        }
        //UPDATE SKULLS IN MAIN PANE
        addSkullsOnMainPane(damagedPlayer);
    }

    @Override
    public void onMark(Player markedPlayer) {
        //ADD MARKS
        Console.println(String.format("Player %swas marked by ", markedPlayer.getStringColor()));
        for (Player from : Client.getInstance().getPlayers()) {
            for (int k = 0; k < markedPlayer.getMarksFromPlayer(from); k++) {
                Console.print("Player " + from.getStringColor() + " ");
            }
        }
        //UPDATE SKULLS IN MAIN PANE
        addSkullsOnMainPane(markedPlayer);
    }

    @Override
    public void updatePoints(Map<Player, Integer> map) {
        for (Player p : Client.getInstance().getPlayers()) {
            int points = map.get(p);
            Console.println(String.format("Player %s has %d points.", p.getStringColor(), points));
        }
    }

    @Override
    public void setTextAndEnableBtnActionGroup(ActionType actionType, int btnNum) {
        switch (btnNum) {
            case 1:
                setBtnEnabled(actionType.name(), true);
                buttonActionTypeMap.put(1, actionType);
                break;
            case 2:
                setBtnEnabled(actionType.name(), true);
                buttonActionTypeMap.put(2, actionType);
                break;
            case 3:
                setBtnEnabled(actionType.name(), true);
                buttonActionTypeMap.put(3, actionType);
                break;
        }
    }

    @Override
    public void selectTag(Selectable selectable) {
        switch (selectable.getType()) {
            case ROOM:
                Console.println("Please select a ROOM: ");
                int i = 0;
                ArrayList<Taggable> selectables1 = new ArrayList<>(selectable.get());
                for (; i<selectables1.size(); i++) {
                    Taggable t = selectables1.get(i);
                    Console.println(String.format("%d) Room %s", i, ((RoomColor) t).name()));
                }
                Console.println(i + ") I don't want to select nothing. ");

                int choose1 = Console.nextInt();
                if (choose1 == i) {
                    gameController.tagElement(null, isShooting);
                } else {
                    RoomColor rc = (RoomColor) selectables1.get(choose1);
                    //Implement from scratch
                    gameController.tagElement(rc, isShooting);
                }
                break;

            case PLAYER:
                showMessage("Please select a PLAYER: ");
                int j = 0;
                ArrayList<Taggable> selectables2 = new ArrayList<>(selectable.get());
                for (; j<selectables2.size(); j++) {
                    Taggable t = selectables2.get(j);
                    Console.println(String.format("%d) Player %s", j, ((Player) t).getStringColor()));
                }
                Console.println(j + ") I don't want to select nothing. ");
                int choose2 = Console.nextInt();
                if (choose2 == j) {
                    gameController.tagElement(null, isShooting);
                } else {
                    Player p = (Player) selectables2.get(choose2);
                    gameController.tagElement(p, isShooting);
                }
                break;


            case SQUARE:
                showMessage("Please click on a SQUARE: ");
                int k = 0;
                ArrayList<Taggable> selectables3 = new ArrayList<>(selectable.get());
                for (; k<selectables3.size(); k++) {
                    Taggable t = selectables3.get(k);
                    Square s = (Square) t;
                    Coordinate c = Client.getInstance().getMap().getSquareCoordinates(s);
                    Console.println(String.format("%d) CELL%d%d", k, c.getX(), c.getY()));
                }
                Console.println(k + ") Don't want to select nothing. ");
                int choose3 = Console.nextInt();
                if (choose3 == k) {
                    gameController.tagElement(null, isShooting);
                } else {
                    Square s = (Square) selectables3.get(choose3);
                    gameController.tagElement(s, isShooting);
                }
                break;
        }
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

    //NELLA SHOOT() mettere isShooting a true, nella USEPOWERUP metterlo a false!!

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
