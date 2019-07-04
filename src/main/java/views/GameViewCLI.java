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
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

import static utils.Console.*;

public class GameViewCLI implements GameView {

    public GameController gameController;
    private int maxRunDistance;
    private Semaphore semaphore;
    private boolean isShooting;
    private boolean firstTurn = true;
    private List<BaseActions> baseActions = new ArrayList<>();
    private Map<BaseActions, ActionType> actionTypeMap;
    private WeaponCard actualWC = null;

    public GameViewCLI() {
        printMapNum(Client.getInstance().getMapNum());
        this.gameController = new GameController();
        actionTypeMap = new EnumMap<>(BaseActions.class);
        this.semaphore = new Semaphore(1);
    }

    /**
     * Acquires the lock
     *
     * @param fromWho a string representing the method which is acquiring the log, only used in debug
     */
    private void acquireLock(String fromWho) {
        try {
            semaphore.acquire();
            if (Client.getInstance().isDebug())
                Console.println("--> acquired " + semaphore.availablePermits() + " from: " + fromWho);
        } catch (InterruptedException e) {
            Logger.getAnonymousLogger().severe(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Releases the lock on the resource
     */
    private void releaseLock() {
        semaphore.release();
        if (Client.getInstance().isDebug())
            Console.println("--> released " + semaphore.availablePermits());
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void getValidActions() {
        gameController.getValidActions();
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void reconnect() {
        gameController.reconnect();
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void startTurn(String name) {
        if (Client.getInstance().getPlayer().getName().equals(name)) {
            gameController.getValidActions();
        }
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void updateMapView(GameMap map) {
        Client.getInstance().setMap(map);
        acquireLock("updateMapView");
        Console.println("");
        Console.println("");
        printMapNum(Client.getInstance().getMapNum());
        Client client = Client.getInstance();
        //UPDATE PLAYERS POSITIONS
        for (Player p : client.getPlayers()) {
            try {
                Coordinate c = map.getPlayerCoordinates(p);
                if (p.equals(client.getPlayer())) {
                    Console.println(String.format("Player %s (you) is on CELL%d%d", p.getStringColor(), c.getX(), c.getY()));
                } else {
                    Console.println(String.format("Player %s is on CELL%d%d", p.getStringColor(), c.getX(), c.getY()));
                }

            } catch (PlayerNotOnMapException e) {
                //Nothing to do, just don't draw it.
            }
        }

        for (Player p : map.getAllPlayers()) {
            printPlayerDamages(p);
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
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 3; y++) {
                Square square = map.getSquareByCoordinate(x, y);
                if (square == null || square.isSpawnPoint()) continue;
                Console.print(String.format("CELL%d%d contains the following ammos: ", x, y));
                AmmoPoint ammoPoint = (AmmoPoint) square;
                AmmoCard ammoCard = ammoPoint.showCard();
                if (ammoCard != null) {
                    Console.print(String.format("Red: %d, Blue: %d, Yellow: %d", ammoCard.getRed(), ammoCard.getBlue(), ammoCard.getYellow()));
                    if (ammoCard.hasPowerup()) Console.print(" and a PowerUp");
                }
                Console.println("");
            }
        }
        releaseLock();
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void updatePlayerView(Player newPlayer) {
        Client.getInstance().setPlayer(newPlayer);
        acquireLock("updatePlayerView");
        //UPDATE AMMO
        Ammo myAmmo = newPlayer.getAmmo();
        Console.print("\nAMMO: You have ");
        Console.print(String.format("%d Red, %d Blue and %d Yellow", myAmmo.red, myAmmo.blue, myAmmo.yellow));
        //ADD NEW POWERUP
        Console.print("\nPOWER UP: You have ");
        for (PowerUpCard powerUpCard : newPlayer.getPowerUpList()) {
            if (newPlayer.getPowerUpList().indexOf(powerUpCard) == newPlayer.getPowerUpList().size() - 1) {
                Console.print(String.format("%s (%s)", powerUpCard.name, powerUpCard.color));
            } else {
                Console.print(String.format("%s (%s) - ", powerUpCard.name, powerUpCard.color));
            }
        }
        Console.print("\nWEAPONS: You have:" + (newPlayer.getWeaponList().size() == 0 ? "\n" : " "));
        //ADD NEW WEAPONS
        for (WeaponCard weaponCard : newPlayer.getWeaponList()) {
            if (newPlayer.getWeaponList().indexOf(weaponCard) == newPlayer.getWeaponList().size() - 1) {
                Console.println(weaponCard.name);
            } else {
                Console.print(weaponCard.name + " - ");
            }
        }
        releaseLock();
    }

    /**
     * Print the skulls for each player
     *
     * @param newPlayer the player whose skulls have just been updated
     */
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
    /**
     * {@inheritDoc}
     */
    public void effectChoosingDialog(LegitEffects legitEffects) {
        acquireLock("effectChoosingDialog");
        Console.println("Which effect do you want to use now?");

        int i = 1;
        int effectsNumber = legitEffects.getLegitEffects().size();
        for (Effect effect : legitEffects.getLegitEffects()) {
            Console.println(String.format("%d) %s", i++, effect.name));
        }
        Console.println(i + ") I don't want to use any effect. ");
        int choose = readConsole(i);
        releaseLock();

        if (choose == effectsNumber) {
            gameController.finishCard();
        } else {
            Effect chosenEffect = legitEffects.getLegitEffects().get(choose);
            PowerUpCard toPay = null;
            if (!chosenEffect.price.isEmpty()) {
                //Would you like to pay with power up?
                toPay = choosePowerUpDialog();
            }
            gameController.playEffect(chosenEffect, toPay);
        }
    }

    /**
     * Choose a powerup when you need to pay
     *
     * @return the powerUp chosen
     */
    private PowerUpCard choosePowerUpDialog() {
        return choosePowerUpDialog(null, null);
    }

    /**
     * Choose a powerup, good for any usage since text can be personalized
     *
     * @param title        The text of the question
     * @param powerUpCards the list of powerUps between the user can choose
     * @return the chosen powerup
     */
    private PowerUpCard choosePowerUpDialog(String title, List<PowerUpCard> powerUpCards) {
        if (Client.getInstance().getPlayer().getPowerUpList().isEmpty()) return null;
        if (title == null) title = "Do you want to use a power up to pay for this action?";
        Console.println(title);

        Player me = Client.getInstance().getPlayer();
        if (powerUpCards == null) powerUpCards = me.getPowerUpList();

        int i = 0;
        for (; i < powerUpCards.size(); i++) {
            PowerUpCard powerUpCard = powerUpCards.get(i);
            Console.println(String.format("%d) %s", i, powerUpCard.getFullName()));
        }
        Console.println(i + ") None ");
        Console.println("Choose one.");
        int choose = Console.nextInt();
        while ((choose > powerUpCards.size() + 1) || choose < 0) {
            Console.print("Number not valid. Choose one.");
            choose = Console.nextInt();
        }

        if (choose == powerUpCards.size()) return null;

        return powerUpCards.get(choose);
    }

    /**
     * Sets the main buttons, those that set the action
     *
     * @param groupActions a set containing all the possible actions
     */
    private void setActionGroupButtons(Set<ActionType> groupActions) {
        if (groupActions.size() > 1 && Client.getInstance().getCurrentActionType() == null) {
            for (ActionType groupAction : groupActions) {
                switch (groupAction) {
                    case SHOOT_VERY_LOW_LIFE:
                    case SHOOT_FRENZY_2:
                    case SHOOT_FRENZY_1:
                    case SHOOT_NORMAL:
                        if (canShoot()) {
                            baseActions.add(BaseActions.SHOOT);
                            actionTypeMap.put(BaseActions.SHOOT, groupAction);
                        }
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

    /**
     * Once chose the main action, choose the sub-action (eg run or grab for a GRAB_NORMAL)
     *
     * @param turnEvents the list of the possible events
     */
    private void setTurnEventButtons(List<TurnEvent> turnEvents) {
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
                    if (turnEvents.size() == 1) {
                        buttonToShow = BaseActions.SPAWN;
                    }
                    break;
                case GRAB:
                    buttonToShow = BaseActions.GRAB;
                    break;
                default:
            }
            if (buttonToShow != null) {
                baseActions.add(buttonToShow);
            }

        }
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public synchronized void updateActions(Map<ActionType, List<TurnEvent>> actions) {
        Client client = Client.getInstance();
        ActionType currentActionType = client.getCurrentActionType();

        boolean turnIsEnding = actions.isEmpty() && client.isMyTurn();

        if (turnIsEnding) baseActions.add(BaseActions.END_TURN);
        //this must be called before setTurnEventButtons or it breaks the frenzy reload
        if (turnIsEnding && !firstTurn) baseActions.add(BaseActions.RELOAD);

        if (currentActionType == null) {
            setActionGroupButtons(actions.keySet());
            if (actions.size() != 1 && Client.getInstance().isMyTurn() && !firstTurn) {
                baseActions.add(BaseActions.USE_POWERUP);
            }
        } else {
            setTurnEventButtons(actions.get(currentActionType));
        }

        int i = 1;
        try {
            Thread.sleep(350);  //Change here
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Logger.getAnonymousLogger().info(e.toString());
        }
        acquireLock("updateActions");
        for (BaseActions baseAction : baseActions) {
            Console.println(i++ + " - " + baseAction.toString());
        }
        if (!baseActions.isEmpty()) {
            chooseAction();
        }
        releaseLock();
    }

    /**
     * shows a dialog that lets the user choose what he wants to do, automatically chooses between actions and sub-actions
     */
    void chooseAction() {
        Console.print("\nChoose one: ");
        int choice = readConsole(baseActions.size());
        BaseActions chosenAction = baseActions.get(choice);
        ActionType currentActionType = Client.getInstance().getCurrentActionType();

        switch (chosenAction) {
            case DRAW:
                gameController.drawPowerUp();
                gameController.getValidActions();
                break;
            case RUN:
                if (currentActionType == null) {
                    ActionType actionType = actionTypeMap.get(chosenAction);
                    Client.getInstance().setCurrentActionType(actionType);
                    Client.getInstance().getConnection().action(actionType);
                    actionTypeMap.clear();
                } else {
                    run();
                }
                break;
            case SHOOT:
                if (currentActionType == null) {
                    ActionType actionType = actionTypeMap.get(chosenAction);
                    Client.getInstance().setCurrentActionType(actionType);
                    Client.getInstance().getConnection().action(actionType);
                    actionTypeMap.clear();
                } else {
                    shoot();
                }
                break;
            case RELOAD:
                reload();
                break;
            case SPAWN:
                spawn();
                break;
            case GRAB:
                if (currentActionType == null) {
                    ActionType actionType = actionTypeMap.get(chosenAction);
                    Client.getInstance().setCurrentActionType(actionType);
                    Client.getInstance().getConnection().action(actionType);
                    actionTypeMap.clear();
                } else {
                    grab();
                }
                break;
            case USE_POWERUP:
                playPowerUp();
                break;
            case END_TURN:
                firstTurn = false;
                gameController.endTurn();
                Console.println("Your turn ended!");
        }
        baseActions.clear();
    }

    /**
     * dialog that discards a powerup and calls the spawn method on server
     */
    private void spawn() {
        Console.println("Choose a powerup to discard and spawn: ");
        Player me = Client.getInstance().getPlayer();
        int i = 1;
        for (PowerUpCard powerUpCard : me.getPowerUpList()) {
            Console.println(i++ + ") " + powerUpCard.getFullName());
        }
        int choice = readConsole(me.getPowerUpList().size());
        PowerUpCard powerUpCard = me.getPowerUpList().get(choice);
        gameController.spawn(powerUpCard);
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void onDamage(Player damagedPlayer) {
        //ADD DAMAGE
        acquireLock("onDamage");
        printPlayerDamages(damagedPlayer);
        //UPDATE SKULLS IN MAIN PANE
        addSkullsOnMainPane(damagedPlayer);

        Player me = Client.getInstance().getPlayer();
        if (damagedPlayer.equals(me)) {
            List<PowerUpCard> playable = new ArrayList<>();
            for (PowerUpCard powerUpCard : me.getPowerUpList()) {
                if (powerUpCard.when.equals("on_damage_received")) {
                    playable.add(powerUpCard);
                }
            }
            if (playable.isEmpty() || me.getLastDamager() == null) {
                releaseLock();
                return;
            }
            PowerUpCard powerUpCard = choosePowerUpDialog("You have been damaged by " + me.getLastDamager().getName() + " would you like to mark him?", playable);
            if (powerUpCard != null) {
                Client.getInstance().getConnection().playPowerUp(powerUpCard.name, null, null);
            }
        }
        releaseLock();
    }

    private void printPlayerDamages(Player player) {
        Console.print(String.format("Player %s was hurt by: ", player.getStringColor()));
        for (Player from : player.getDamagedBy()) {
            Console.print(String.format("%s ", from.getStringColor()));
        }
        Console.print("\n");
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void onMark(Player markedPlayer) {
        //ADD MARKS
        acquireLock("onMark");
        if (!markedPlayer.hasMarks()) {
            releaseLock();
            return;
        }
        Console.print(String.format("Player %s is marked by: ", markedPlayer.getStringColor()));
        for (Player from : Client.getInstance().getPlayers()) {
            for (int k = 0; k < markedPlayer.getMarksFromPlayer(from); k++) {
                Console.print(from.getStringColor() + " ");
            }
        }
        Console.println("\n");
        //UPDATE SKULLS IN MAIN PANE
        addSkullsOnMainPane(markedPlayer);
        releaseLock();
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void updatePoints(Map<Player, Integer> map) {
        acquireLock("updatePoints");
        for (Player p : Client.getInstance().getPlayers()) {
            int points = map.get(p);
            Console.println(String.format("Player %s has %d points.", p.getStringColor(), points));
        }
        releaseLock();
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void selectTag(Selectable selectable) {
        acquireLock("selectTag");
        Console.println("Please select a " + selectable.getType());
        List<Taggable> taggables = new ArrayList<>(selectable.get());
        int i = 1;
        int max = taggables.size();
        for (Taggable taggable : taggables) {
            String tag = null;
            switch (selectable.getType()) {
                case ROOM:
                    tag = taggable.toString();
                    break;
                case SQUARE:
                    Coordinate c = Client.getInstance().getMap().getSquareCoordinates((Square) taggable);
                    tag = "CELL " + c.getX() + c.getY();
                    break;
                case PLAYER:
                    tag = ((Player) taggable).getStringColor();
            }
            Console.println(i++ + ") " + tag);
        }
        if (selectable.isOptional()) {
            Console.println(i + ") Nothing");
            max++;
        }
        int result = readConsole(max);
        releaseLock();
        if (result == taggables.size()) {
            gameController.tagElement(null, isShooting);
        } else {
            Taggable tagged = taggables.get(result);
            gameController.tagElement(tagged, isShooting);
        }
    }

    /**
     * grabs an ammo if on an ammo point, or shows a dialog asking the user which weapons he wants to draw (if any)
     */
    private void grab() {
        Client client = Client.getInstance();
        GameMap gameMap = client.getMap();
        Player me = client.getPlayer();

        Square mySquare = gameMap.getPlayerPosition(me);
        if (mySquare.isSpawnPoint()) {
            SpawnPoint spawnPoint = (SpawnPoint) mySquare;
            int i = 1;
            Console.println("Which weapon do you want to draw?");
            for (WeaponCard weaponCard : spawnPoint.showCards()) {
                Console.println(i++ + " " + weaponCard.name + ", price: " + weaponCard.drawPrice);
            }
            int result = readConsole(spawnPoint.showCards().size());
            WeaponCard toDraw = spawnPoint.showCards().get(result);
            PowerUpCard powerUpToPay = null;
            WeaponCard toRelease = null;
            if (!toDraw.drawPrice.isEmpty()) {
                powerUpToPay = choosePowerUpDialog();
            }
            if (me.getWeaponList().size() == 3) {
                Console.println("You have to leave one of your weapons to draw this one");
                toRelease = chooseWeaponDialog(me.getWeaponList());
            }
            gameController.grab(toDraw, toRelease, powerUpToPay);
        } else {
            gameController.grab(null, null, null);
        }
    }

    /**
     * shows the user all the possible squares he can run to and lets he choose one
     */
    private void run() {
        Client client = Client.getInstance();
        GameMap gameMap = client.getMap();
        Player me = client.getPlayer();
        Square myPosition = gameMap.getPlayerPosition(me);
        Set<Square> runnableSet = gameMap.getAllSquaresAtDistanceLessThanOrEquals(myPosition, maxRunDistance);
        List<Square> runnableList = new ArrayList<>(runnableSet);
        Console.println("Where do you want to go?");
        int i = 1;
        for (Square s : runnableList) {
            Coordinate c = gameMap.getSquareCoordinates(s);
            Console.println(i++ + ") CELL " + c.getX() + c.getY());
        }
        int result = readConsole(runnableList.size());
        Square selected = runnableList.get(result);
        TurnEvent te = Client.getInstance().getActions().get(Client.getInstance().getCurrentActionType()).get(0);
        gameController.run(te, selected);
    }

    /**
     * Shows the loaded weapon to the user and lets him choose one, then calls continueWeapon()
     */
    private void shoot() {
        //if you are here you surely have at least one loaded weapon.
        Client client = Client.getInstance();
        Player me = client.getPlayer();

        List<WeaponCard> loadedWeapons = getLoadedWeapons(me);
        if (!loadedWeapons.isEmpty()) {
            Console.println("Only the loaded weapons are shown.");
            WeaponCard chosen = chooseWeaponDialog(loadedWeapons);
            setActualWC(chosen);
            isShooting = true;
            continueWeapon();
        } else {
            Console.println("You cannot shoot");
            gameController.finishCard();
        }
    }

    /**
     * Asks the user which weapon he wants to reload and reloads it if possible
     */
    private void reload() {
        Player me = Client.getInstance().getPlayer();
        if (getLoadedWeapons(me).size() == me.getWeaponList().size()) {
            Console.println("Nothing to reload");
        } else {
            List<WeaponCard> reloadableWeapons;
            Map<WeaponCard, PowerUpCard> reloads = new HashMap<>();
            while (!(reloadableWeapons = getReloadableWeapons(me)).isEmpty()) {
                Console.println("Choose which weapon you want to reload: ");
                int i = 1;
                for (WeaponCard weaponCard : reloadableWeapons) {
                    Console.println(i++ + ") " + weaponCard.getName() + ", price: " + weaponCard.getReloadPrice());
                }
                Console.println(i + ") Nothing");
                int result = readConsole(i);
                if (result == reloadableWeapons.size()) {
                    break;
                }
                WeaponCard toReload = reloadableWeapons.get(result);
                PowerUpCard toPay = null;
                if (!me.getPowerUpList().isEmpty()) {
                    toPay = choosePowerUpDialog();
                }
                reloads.put(toReload, toPay);
                me.reloadWeapon(toReload, toPay);
            }
            if (reloadableWeapons.isEmpty()) {
                Console.println("You cannot reload anything else");
            }
            Client.getInstance().getConnection().reload(reloads);
        }
        gameController.getValidActions();
    }

    /**
     * Asks the user which powerUp he wants to play and makes him pay with a dialog if it has a price.
     */
    private void playPowerUp() {
        Player me = Client.getInstance().getPlayer();
        List<PowerUpCard> selectablePowerUps = getPlayablePowerUps(me);
        if (selectablePowerUps.isEmpty()) {
            Console.println("You cannot play any powerUp!");
        } else {
            PowerUpCard powerUpCard = choosePowerUpDialog("Which powerup do you want to use?", selectablePowerUps);
            Ammo ammoToPay = null;
            PowerUpCard powerUpToPay = null;
            if (powerUpCard != null) {
                if (powerUpCard.getHasPrice()) {
                    ammoToPay = chooseOneAmmoDialog(me);
                    if (ammoToPay == null) {
                        List<PowerUpCard> powerUpsToPay = me.getPowerUpList();
                        powerUpsToPay.remove(powerUpCard);
                        powerUpToPay = choosePowerUpDialog("Choose a powerUp to pay", powerUpsToPay);
                        if (powerUpToPay == null) {
                            Console.println("You must pay to use this powerUp");
                            gameController.getValidActions();
                            return;
                        }
                    }
                }
                isShooting = false;
                gameController.playPowerUp(powerUpCard, ammoToPay, powerUpToPay);
            }
        }
        gameController.getValidActions();
    }

    /**
     * Shows all ammo colors of which you have at least one cube and an option to pay with a powerup instead
     */
    private Ammo chooseOneAmmoDialog(Player player) {
        Ammo myAmmo = player.getAmmo();
        if (myAmmo.isEmpty()) return null;
        Console.println("Choose one ammo cube of any color:");
        List<String> options = new ArrayList<>();
        if (myAmmo.red > 0) options.add("Red");
        if (myAmmo.blue > 0) options.add("Blue");
        if (myAmmo.yellow > 0) options.add("Yellow");
        options.add("I want to pay with a powerUp");

        int i = 1;
        for (String s : options) {
            Console.println(i++ + ") " + s);
        }
        int choice = readConsole(options.size());
        String chosenOption = options.get(choice);
        switch (chosenOption) {
            case "Red":
                return new Ammo(1, 0, 0);
            case "Blue":
                return new Ammo(0, 1, 0);
            case "Yellow":
                return new Ammo(0, 0, 1);
            default:
                return null;
        }
    }

    /**
     * returns the powerup the user can actually play: those he can pay and that can be played in its turn
     *
     * @param player your player
     * @return a list of the powerup cards the user can play
     */
    private List<PowerUpCard> getPlayablePowerUps(Player player) {
        List<PowerUpCard> out = player.getPowerUpList();
        List<PowerUpCard> toRemove = new ArrayList<>();
        for (PowerUpCard powerUpCard : out) {
            if (powerUpCard.when.equals("on_damage_received") || (powerUpCard.getHasPrice() && player.getAmmo().isEmpty() && player.getPowerUpList().size() == 1)) {
                toRemove.add(powerUpCard);
            }
        }
        out.removeAll(toRemove);
        return out;
    }

    /**
     * get the weapons the user can reload: those that he can pay and that are actually unloaded
     *
     * @param player your player
     * @return a list of weapons that can be reloaded
     */
    private List<WeaponCard> getReloadableWeapons(Player player) {
        List<WeaponCard> reloadable = new ArrayList<>();
        for (WeaponCard weaponCard : player.getWeaponList()) {
            if (!weaponCard.isLoaded() && weaponCard.canReload(player.getAmmo(), player.getPowerUpList())) {
                reloadable.add(weaponCard);
            }
        }
        return reloadable;
    }

    /**
     * checks that the user has at least one loaded weapon
     *
     * @return true if you have something to shoot with
     */
    private boolean canShoot() {
        Player me = Client.getInstance().getPlayer();
        return !getLoadedWeapons(me).isEmpty();
    }

    /**
     * @param player your player
     * @return a list of loaded weapons
     */
    private List<WeaponCard> getLoadedWeapons(Player player) {
        List<WeaponCard> loadedWeapons = new ArrayList<>();
        for (WeaponCard weaponCard : player.getWeaponList()) {
            if (weaponCard.isLoaded()) loadedWeapons.add(weaponCard);
        }
        return loadedWeapons;
    }

    /**
     * asks the user for a weapon to use
     *
     * @param weaponCards a list containing all the weapons the user can choose among
     * @return the chosen weapon
     */
    private WeaponCard chooseWeaponDialog(List<WeaponCard> weaponCards) {
        Console.println("Choose a weapon:");
        int i = 1;
        for (WeaponCard weaponCard : weaponCards) {
            Console.println(i++ + ") " + weaponCard.name);
        }
        int result = readConsole(weaponCards.size());
        return weaponCards.get(result);
    }

    /**
     * Sets the weapon you are shooting with
     *
     * @param wc the weapon card you are using to shoot
     */
    public void setActualWC(WeaponCard wc) {
        this.actualWC = wc;
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void continueWeapon() {
        if (actualWC != null) {
            gameController.getEffects(actualWC);
        }
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void onEndWeapon() {
        this.setActualWC(null);
        this.isShooting = false;
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void onEndMatch(List<Player> winners, Map<Player, Integer> pointers) {
        acquireLock("onEndMatch");
        for (Map.Entry<Player, Integer> entry : pointers.entrySet()) {
            Console.println(String.format("%s scored %d points", entry.getKey().getName(), entry.getValue()));
        }
        Console.println("\n");
        for (Player player : winners) {
            Console.println(player.getName() + " WON!");
        }
        releaseLock();
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void printError(String error) {
        Logger.getAnonymousLogger().info(error);
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void showMessage(String message) {
        acquireLock("showMessage");
        Console.printColor(message + "\n", COLOR.PURPLE);
        releaseLock();
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void onNewPlayer(String playerName) {
        showMessage("Player " + playerName + " connected!");
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void onPlayerDisconnected(String playerName) {
        showMessage("Player " + playerName + " disconnected!");
    }

    //NELLA SHOOT() mettere isShooting a true, nella USEPOWERUP metterlo a false!!

    /**
     * Prints the map with the given number
     *
     * @param num an id between 0 and 3
     */
    private void printMapNum(int num) {
        println("╔═══════════════════════════════════════╗");
        println("║             ADRENALINE MAP            ║");
        println("╠═════════╦═════════╦═════════╦═════════╣");

        GameMap gameMap = MapGenerator.generate(num);

        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 4; i++) {
                if (j % 2 == 0) {
                    printContent(gameMap, i, j / 2);
                } else {
                    printBottom(gameMap, i, (j - 1) / 2);
                }
            }
        }
        println("╚═════════╩═════════╩═════════╩═════════╝");
    }

    /**
     * prints the bottom of the map line that is being printed
     *
     * @param gameMap the map to print
     * @param x       the row of the map, starting from 0 on the top
     * @param y       the column, 0 from left
     */
    private void printBottom(GameMap gameMap, int x, int y) {
        Square s = gameMap.getSquareByCoordinate(x, y);
        boolean bottomWalk = s != null && s.hasNextWalkable(CardinalDirection.BOTTOM);
        if (x == 0) {
            print("╠═══");
        } else {
            print("╬═══");
        }
        if (bottomWalk) {
            print("   ═══");
        } else {
            print("══════");
        }
        if (x == 3) {
            print("╣\n");
        }
    }

    /**
     * prints the content of the line: the cell number and a wall or a door
     *
     * @param gameMap the map being drawn
     * @param x       the row of the map, starting from 0 on the top
     * @param y       the column, 0 from left
     */
    private void printContent(GameMap gameMap, int x, int y) {
        Square s = gameMap.getSquareByCoordinate(x, y);

        if (s == null) {
            print("║         ");
        } else {
            boolean leftWalk = s.hasNextWalkable(CardinalDirection.LEFT);
            if (leftWalk)
                print(" ");
            else
                print("║");

            String text = String.format(" CELL %d%d ", x, y);
            COLOR color = getColor(s.getColor());
            printColor(text, color);
        }
        if (x == 3) print("║\n");
    }

    private COLOR getColor(RoomColor color) {
        switch (color) {
            case YELLOW:
                return COLOR.YELLOW;
            case BLUE:
                return COLOR.BLU;
            case RED:
                return COLOR.RED;
            case GREEN:
                return COLOR.GREEN;
            case WHITE:
                return COLOR.WHITE;
            case PURPLE:
                return COLOR.PURPLE;
            default:
                return null;
        }
    }

    /**
     * Reads the user input in the console, from 1 to max
     *
     * @param max the max number the user can provide
     * @return the read number - 1 (to be used as index)
     */
    private int readConsole(int max) {
        int x = Console.nextInt();
        while (x < 1 || x > max) {
            Console.println("Invalid input. Try again:");
            x = Console.nextInt();
        }
        return x - 1;
    }
}
