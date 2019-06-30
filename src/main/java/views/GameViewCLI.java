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
import java.util.logging.Logger;

import static utils.Console.*;
import static utils.Console.println;

public class GameViewCLI implements GameView {

    private int maxRunDistance;
    private HashMap<Integer, ActionType> buttonActionTypeMap = new HashMap<>();
    private boolean isShooting;
    private boolean firstTurn = true;

    private List<BaseActions> baseActions = new ArrayList<>();
    private Map<BaseActions, ActionType> actionTypeMap;

    public GameController gameController;

    public GameViewCLI() {
        printMapNum(Client.getInstance().getMapNum());
        this.gameController = new GameController();
        actionTypeMap = new EnumMap<>(BaseActions.class);
        getValidActions();
    }

    @Override
    public void getValidActions() {
        gameController.getValidActions();
    }

    @Override
    public void reconnect() {
        gameController.reconnect();
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
                Console.println(String.format("Player %s is on CELL%d%d", p.getStringColor(), c.getX(), c.getY()));
            }
            catch (PlayerNotOnMapException e) {
                //Nothing to do, just don't draw it.
            }
        }

        //ADD NEW WEAPONS
        for (SpawnPoint sp : map.getSpawnPoints()) {
            Console.println(String.format("Spawn point %s contains the following weapons:", sp.getColor()));
            for (WeaponCard card : sp.showCards()) {
                Console.print(card.name + ", ");
            }
            Console.println("");
        }

        //UPDATE AMMOs
        for (int x = 0; x<4; x++) {
            for (int y=0; y<3; y++) {
                Square square = map.getSquareByCoordinate(x, y);
                if (square == null || square.isSpawnPoint()) continue;
                Console.print(String.format("CELL%d%d contains the following ammos: ", x, y));
                AmmoPoint ammoPoint = (AmmoPoint) square;
                AmmoCard ammoCard = ammoPoint.showCard();
                if (ammoCard==null) continue;
                Console.println(String.format("Red: %d, Blue: %d, Yellow: %d", ammoCard.getRed(), ammoCard.getBlue(), ammoCard.getYellow()));
            }
        }
    }

    @Override
    public void updatePlayerView(Player newPlayer) {
        //UPDATE AMMO
        Ammo myAmmo = newPlayer.getAmmo();
        Console.print("\nAMMO: You have ");
        Console.print(String.format("%d Red, %d Blue and %d Yellow", myAmmo.red, myAmmo.blue, myAmmo.yellow));
        //ADD NEW POWERUP
        Console.print("\nPOWER UP: You have ");
        for (PowerUpCard powerUpCard : newPlayer.getPowerUpList()) {
            if (newPlayer.getPowerUpList().indexOf(powerUpCard) == newPlayer.getPowerUpList().size()-1) {
                Console.print(String.format("%s (%s)", powerUpCard.name, powerUpCard.color));
            } else {
                Console.print(String.format("%s (%s) - ", powerUpCard.name, powerUpCard.color));
            }
        }
        Console.print("\nWEAPONS: You have: ");
        //ADD NEW WEAPONS
        for (WeaponCard weaponCard : newPlayer.getWeaponList()) {
            if (newPlayer.getWeaponList().indexOf(weaponCard) == newPlayer.getWeaponList().size()-1) {
                Console.println(weaponCard.name);
            } else {
                Console.print(weaponCard.name + " - ");
            }
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
        if (string != null && isVisible) {
            Console.println("You can " + string);
        }
    }

    private void setActionGroupButtons(Set<ActionType> groupActions){
        Console.println("--> "+groupActions.toString());
        if(groupActions.size() > 1 && Client.getInstance().getCurrentActionType()==null){
            for(ActionType groupAction : groupActions){
                switch (groupAction){
                    case SHOOT_VERY_LOW_LIFE:
                    case SHOOT_FRENZY_2:
                    case SHOOT_FRENZY_1:
                    case SHOOT_NORMAL:
                        baseActions.add(BaseActions.SHOOT);
                        actionTypeMap.put(BaseActions.SHOOT, groupAction);
                        break;
                    case GRAB_LOW_LIFE:
                    case GRAB_FRENZY_2:
                    case GRAB_FRENZY_1:
                    case GRAB_NORMAL:
                        baseActions.add(BaseActions.GRAB);
                        actionTypeMap.put(BaseActions.GRAB, groupAction);
                        break;
                    case RUN_FRENZY_1:
                    case RUN_NORMAL:
                        baseActions.add(BaseActions.RUN);
                        actionTypeMap.put(BaseActions.RUN, groupAction);
                        break;
                }
            }
        }
    }

    private void setTurnEventButtons(List<TurnEvent> turnEvents){
        for (TurnEvent turnEvent : turnEvents) {
            BaseActions buttonToShow = null;
            switch (turnEvent) {
                case DRAW:
                    buttonToShow = BaseActions.DRAW;
                    break;
                case RUN_1:
                case RUN_2:
                case RUN_3:
                case RUN_4:
                    this.maxRunDistance = Character.getNumericValue(turnEvent.toString().charAt(4));
                    buttonToShow = BaseActions.RUN;
                    break;
                case SHOOT:
                    buttonToShow = BaseActions.SHOOT;
                    break;
                case RELOAD:
                    buttonToShow = BaseActions.RELOAD;
                    break;
                case SPAWN:
                    if(turnEvents.size()==1){
                        buttonToShow = BaseActions.SPAWN;
                    }
                    break;
                case GRAB:
                    buttonToShow = BaseActions.GRAB;
                    break;
                default:
            }
            if(buttonToShow!=null){
                baseActions.add(buttonToShow);
            }

        }
    }

    @Override
    public void updateActions(Map<ActionType, List<TurnEvent>> actions) {
        Client client = Client.getInstance();
        ActionType currentActionType = client.getCurrentActionType();

        Console.println("--> Current action type: " + currentActionType);

        boolean turnIsEnding = actions.isEmpty() && client.isMyTurn();

        if(turnIsEnding) baseActions.add(BaseActions.END_TURN);
        //this must be called before setTurnEventButtons or it breaks the frenzy reload
        if(turnIsEnding && !firstTurn) baseActions.add(BaseActions.RELOAD);

        if(currentActionType==null){
            setActionGroupButtons(actions.keySet());
        } else {
            setTurnEventButtons(actions.get(currentActionType));
        }

        int i = 1;
        for(BaseActions baseAction : baseActions){
            Console.println(i++ + " - " + baseAction.toString());
        }
        if(!baseActions.isEmpty()){
            chooseAction();
        }

    }

    void chooseAction() {
        Console.print("Choose one: ");
        int i = Console.nextInt();
        while ((i > baseActions.size()) || i<=0) {
            Console.print("Number not valid. Choose one.");
            i = Console.nextInt();
        }
        BaseActions chosenAction = baseActions.get(i-1);
        ActionType currentActionType = Client.getInstance().getCurrentActionType();

        switch (chosenAction) {
            case DRAW:
                gameController.drawPowerUp();
                gameController.getValidActions();
                break;
            case RUN:
                if(currentActionType == null){
                    ActionType actionType = actionTypeMap.get(chosenAction);
                    Client.getInstance().setCurrentActionType(actionType);
                    Client.getInstance().getConnection().action(actionType);
                    actionTypeMap.clear();
                }else{
                    run();
                }
                break;
            case SHOOT:
                if(currentActionType == null){
                    ActionType actionType = actionTypeMap.get(chosenAction);
                    Client.getInstance().setCurrentActionType(actionType);
                    Client.getInstance().getConnection().action(actionType);
                    actionTypeMap.clear();
                }else{
                    //shoot()
                }
                break;
            case RELOAD:
                break;
            case SPAWN:
                spawn();
                break;
            case GRAB:
                Console.println("--> Grab chosen");
                if(currentActionType == null){
                    Console.println("--> Current action type is null");
                    ActionType actionType = actionTypeMap.get(chosenAction);
                    Client.getInstance().setCurrentActionType(actionType);
                    Client.getInstance().getConnection().action(actionType);
                    actionTypeMap.clear();
                }else{
                    Console.println("--> Current action type is NOT null. its value: " + currentActionType.toString());
                    grab();
                }
                break;
            case END_TURN:
                gameController.endTurn();
                Console.println("Your turn ended!");
        }
        baseActions.clear();
    }

    private void spawn() {
        Console.print("Discard a powerup. Select a number, starting from 1: ");
        PowerUpCard powerUpCard = Client.getInstance().getPlayer().getPowerUpList().get(Console.nextInt()-1);
        gameController.spawn(powerUpCard);
    }

    @Override
    public void onDamage(Player damagedPlayer) {
        //ADD DAMAGE
        Console.println(String.format("Player %s was hurt by ", damagedPlayer.getStringColor()));
        for (Player from : damagedPlayer.getDamagedBy()) {
            Console.print(String.format("Player %s ", from.getStringColor()));
        }
        //UPDATE SKULLS IN MAIN PANE
        addSkullsOnMainPane(damagedPlayer);
    }

    @Override
    public void onMark(Player markedPlayer) {
        //ADD MARKS
        boolean hasMarks = false;
        for (Player from : Client.getInstance().getPlayers()) {
            for (int k = 0; k < markedPlayer.getMarksFromPlayer(from); k++) {
                hasMarks = true;
                break;
            }
        }
        if (!hasMarks) return;
        Console.println(String.format("Player %s is marked by ", markedPlayer.getStringColor()));
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

    private void grab(){
        Client client = Client.getInstance();
        GameMap gameMap = client.getMap();
        Player me = client.getPlayer();

        Square mySquare = gameMap.getPlayerPosition(me);
        if(mySquare.isSpawnPoint()){
            SpawnPoint spawnPoint = (SpawnPoint) mySquare;
            int i = 1;
            Console.println("Which weapon do you want to draw?");
            for(WeaponCard weaponCard : spawnPoint.showCards()){
                Console.println(i++ + " " + weaponCard.name + ", price: " + weaponCard.drawPrice);
            }
            int result = readConsole(spawnPoint.showCards().size());
            WeaponCard toDraw = spawnPoint.showCards().get(result);
            PowerUpCard powerUpToPay = null;
            WeaponCard toRelease = null;
            if(!toDraw.drawPrice.isEmpty()){
                powerUpToPay = choosePowerUpDialog();
            }
            if(me.getWeaponList().size() == 3){
                Console.println("You have to leave one of your weapons to draw this one");
                toRelease = chooseWeaponDialog(me.getWeaponList());
            }
            gameController.grab(toDraw, toRelease, powerUpToPay);
        }else{
            gameController.grab(null, null, null);
        }
    }

    private void run(){
        Client client = Client.getInstance();
        GameMap gameMap = client.getMap();
        Player me = client.getPlayer();
        Square myPosition = gameMap.getPlayerPosition(me);
        Set<Square> runnableSet = gameMap.getAllSquaresAtDistanceLessThanOrEquals(myPosition, maxRunDistance);
        List<Square> runnableList = new ArrayList<>(runnableSet);
        Console.println("Where do you want to go?");
        int i = 1;
        for(Square s : runnableList){
            Coordinate c = gameMap.getSquareCoordinates(s);
            Console.println(i++ + ") CELL " + c.getX() + c.getY());
        }
        int result = readConsole(runnableList.size());
        Square selected = runnableList.get(result);
        TurnEvent te = Client.getInstance().getActions().get(Client.getInstance().getCurrentActionType()).get(0);
        gameController.run(te, selected);
    }

    private WeaponCard chooseWeaponDialog(List<WeaponCard> weaponCards){
        Console.println("Choose a weapon:");
        int i = 1;
        for(WeaponCard weaponCard : weaponCards){
            Console.println(i++ + ") " + weaponCard.name);
        }
        int result = readConsole(weaponCards.size());
        return weaponCards.get(result);
    }

    private WeaponCard actualWC = null;
    public void setActualWC(WeaponCard wc) {
        this.actualWC = wc;
    }
    @Override
    public void continueWeapon() {
        if (actualWC!=null) {
            gameController.getEffects(actualWC);
        }
    }

    @Override
    public void onEndWeapon() {
        this.setActualWC(null);
    }

    @Override
    public void printError(String error) {
        Logger.getAnonymousLogger().info(error);    //NON CREDO FUNZIONI
    }

    @Override
    public void showMessage(String message) {
        Console.printColor(message + "\n", COLOR.PURPLE);
    }

    @Override
    public void onNewPlayer(String playerName) {
        showMessage("Player " + playerName + " connected!");
    }

    @Override
    public void onPlayerDisconnected(String playerName) {
        showMessage("Player " + playerName + " disconnected!");
    }

    //NELLA SHOOT() mettere isShooting a true, nella USEPOWERUP metterlo a false!!

    private void printMapNum(int num) {
        println("╔═══════════════════════════════════════╗");
        println("║             ADRENALINE MAP            ║");
        println("╠═════════╦═════════╦═════════╦═════════╣");

        GameMap gameMap = MapGenerator.generate(num);

        for(int j = 0; j<5; j++){
            for(int i = 0; i<4; i++){
                if(j%2 ==0){
                    printContent(gameMap, i, j/2);
                }else{
                    printBottom(gameMap, i, (j-1)/2);
                }
            }
        }
        println("╚═════════╩═════════╩═════════╩═════════╝");
    }

    private void printBottom(GameMap gameMap, int x, int y){
        Square s = gameMap.getSquareByCoordinate(x, y);
        boolean bottomWalk = s!=null && s.hasNextWalkable(CardinalDirection.BOTTOM);
        if(x==0){
            print("╠═══");
        }else{
            print("╬═══");
        }
        if(bottomWalk){
            print("   ═══");
        }else{
            print("══════");
        }
        if(x==3){
            print("╣\n");
        }
    }

    private void printContent(GameMap gameMap, int x, int y){
        Square s = gameMap.getSquareByCoordinate(x, y);

        if(s==null){
            print("║         ");
        }else{
            boolean leftWalk = s.hasNextWalkable(CardinalDirection.LEFT);
            if(leftWalk)
                print(" ");
            else
                print("║");

            String text = String.format(" CELL %d%d ", x, y);
            COLOR color = getColor(s.getColor());
            printColor(text, color);
        }
        if(x==3) print("║\n");
    }

    private COLOR getColor(RoomColor color){
        switch (color){
            case YELLOW: return COLOR.YELLOW;
            case BLUE: return COLOR.BLU;
            case RED: return COLOR.RED;
            case GREEN: return COLOR.GREEN;
            case WHITE: return COLOR.WHITE;
            case PURPLE: return COLOR.PURPLE;
            default: return null;
        }
    }

    private int readConsole(int max){
        int x = Console.nextInt();
        while(x<1 || x > max){
            Console.println("Invalid input. Try again:");
            x = Console.nextInt();
        }
        return x-1;
    }
}
