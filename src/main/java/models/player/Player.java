package models.player;

import errors.CheatException;
import errors.NotEnoughAmmoException;
import errors.TooManyCardsException;
import errors.WeaponCardException;
import models.Match;
import models.card.*;
import models.map.*;
import models.turn.ActionGroup;
import network.Response;
import network.Server;
import network.updates.DamageUpdate;
import network.updates.MarkUpdate;
import utils.Observable;
import utils.Observer;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Player implements Subscriber, Serializable, Taggable, Observable {

    private String name;
    private String password;
    private Ammo ammo;
    private List<WeaponCard> weaponList;
    private WeaponCard activeWeapon;
    public PowerUpCard activePowerUp;
    private List<PowerUpCard> powerUpList;
    private Life life;
    private Mark marks;
    private int points;
    private int numberOfSkulls;
    private ActionGroup lifeState = ActionGroup.NORMAL;
    private boolean online;
    private transient GameMap gameMap;
    private boolean hasJustStarted;
    private Set<Player> playersDamagedByMeThisTurn;
    private DeathManager deathManager;
    private transient Match match;
    private String circleColor = null;
    private transient List<Observer> observers;

    public void setPlayerColor(String playerColor) {
        this.circleColor = playerColor;
    }

    public String getStringColor() {
        return circleColor;
    }

    public List<Player> getDamagedBy() {
        return this.life.getDamagedBy();
    }

    /** Creates a new player object
     * @param name The name identifier for the player
     * */
    public Player(String name, String password){
        this.name = name;
        this.password = password;
        this.online = true;
        ammo = new Ammo();
        weaponList = new ArrayList<>();
        powerUpList = new ArrayList<>();
        life = new Life(this);
        points = 0;
        numberOfSkulls = 0;
        marks = new Mark();
        activeWeapon = null;
        gameMap = null;
        hasJustStarted = true;
        playersDamagedByMeThisTurn = new HashSet<>();
        this.observers = new ArrayList<>();
        //token generation
    }

    @Override
    public boolean equals(Object o) {
        // self check
        if (this == o)
            return true;
        // null check
        if (o == null)
            return false;
        // type check and cast
        if (getClass() != o.getClass())
            return false;
        Player player = (Player) o;
        // field comparison
        if(name==null) return false;
        return name.equals(player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public void update(Object playerLife) {
        //TODO: What to do when the states changes?
    }

    public AmmoCard drawAmmoCard(){
        AmmoPoint mySquare = (AmmoPoint) gameMap.getPlayerPosition(this);
        AmmoCard ammoCard = mySquare.drawCard();
        this.ammo.add(ammoCard.getAmmo());
        if(ammoCard.hasPowerup()){
            drawPowerUp();
        }
        return ammoCard;
    }

    public PowerUpCard drawPowerUp() {
        PowerUpCard powerUpCard = null;
        if(powerUpList.size() < 3 ){
            powerUpCard = (PowerUpCard) match.drawPowerUp();
            powerUpList.add(powerUpCard);
        }
        return powerUpCard;
    }

    public void throwPowerUp(PowerUpCard powerUpCard){
        if(match!=null){
            match.throwPowerUp(powerUpCard);
        }
        this.powerUpList.remove(powerUpCard);
    }

    /** Pays the draw price of the card and adds it to the weapons list
     * @param drawn the card to draw
     * @param toRelease the card to release to grab the new one. Can be null unless you already have 3 weapons
     * @throws TooManyCardsException if you have 3 cards in your hand and toRelease is null
     * @throws CheatException if you do not have the card toRelease */
    public void drawWeaponCard(WeaponCard drawn, PowerUpCard powerUpToPay, WeaponCard toRelease) {
        if(powerUpToPay!=null && !powerUpList.contains(powerUpToPay)) throw new WeaponCardException("You do not have this powerup: " + powerUpToPay.name);
        if(drawn==null) throw new NullPointerException("Drawn weapon cannot be null");
        if(!drawn.canDraw(ammo, powerUpToPay)) throw new NotEnoughAmmoException();
        SpawnPoint myPosition = (SpawnPoint) gameMap.getPlayerPosition(this);
        if(weaponList.size()==3){
            if(toRelease==null) throw new TooManyCardsException("You already have 3 weapons, select one to leave");
            if(!weaponList.contains(toRelease)) throw new CheatException("You do not have the card you are trying to release");
            myPosition.drawCard(drawn);
            myPosition.addCard(toRelease);
            weaponList.remove(toRelease);
        }else{
            myPosition.drawCard(drawn);
        }
        Ammo price = drawn.getDrawPrice().getCopy();
        if(powerUpToPay!=null){
            price.remove(new Ammo(powerUpToPay));
            match.throwPowerUp(powerUpToPay);
            powerUpList.remove(powerUpToPay);
        }
        ammo.remove(price);
        weaponList.add(drawn);
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasJustStarted() {
        return hasJustStarted;
    }

    public void hasSpawnFirstTime() {
        this.hasJustStarted = false;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setAmmo(Ammo ammo) {
        this.ammo = ammo;
    }

    public Ammo getAmmo() {
        return ammo;
    }

    public void setWeaponList(List<WeaponCard> weaponList) {
        this.weaponList = weaponList;
    }

    public List<WeaponCard> getWeaponList() {
        return weaponList;
    }

    public WeaponCard getWeaponByName(String name){
        return weaponList.stream().filter(w -> w.name.equals(name)).findFirst().orElse(null);
    }

    public PowerUpCard getPowerUpByName(String name, RoomColor color){
        for(PowerUpCard card : powerUpList){
            if(card.name.equals(name) && card.color.equals(color)){
                return card;
            }
        }
        throw new WeaponCardException("User does not have this powerUp: " + name + " color: " + color);
    }

    public void setPowerUpList(List<PowerUpCard> powerUpList) {
        this.powerUpList = powerUpList;
    }

    public List<PowerUpCard> getPowerUpList() {
        return powerUpList.stream().sorted(Comparator.comparing(c -> c.name)).collect(Collectors.toList());
    }

    public int getPoints() { return points; }

    public void setPoints(int points) { this.points = points; }

    public int getNumberOfSkulls() { return this.numberOfSkulls; }

    public void setNumberOfSkulls(int numberOfSkulls) { this.numberOfSkulls = numberOfSkulls; }

    /**
     * @return a map between Player and the damages it made to this player
     * */
    public Map<Player, Integer> getDamageMap() {
        return this.life.getMyDamages();
    }

    public boolean isDead() {
        return this.life.isDead();
    }

    /** Damage this player
     * @param damage the number of damage to give. Could be any number, without needs to check limitation of it.
     * @param attacker the player which is attacking
     * */
    public void getDamage(int damage, Player attacker) {
        this.life.damage(damage, attacker);
        attacker.addDamagedPlayerToList(this);

        updateObservers(new DamageUpdate(this));
        if(getTotalDamage()==12){
            updateObservers(new MarkUpdate(getLastDamager()));
        }
    }

    private void addDamagedPlayerToList(Player damagedPlayer){
        playersDamagedByMeThisTurn.add(damagedPlayer);
    }

    public void onTurnEnded(){
        this.playersDamagedByMeThisTurn = new HashSet<>();
    }

    public Set<Player> getPlayersDamagedByMeThisTurn(){
        return playersDamagedByMeThisTurn;
    }

    /** Counts the points to assign to each player
     * @return a map between Player and the points to assign him
     * */
    public Map<Player, Integer> countPoints() { return this.life.countPoints(); }

    /** Clears all the damage map of this player. */
    public void clearDamages() { this.life.clearDamages(); }

    /** Damage this player
     * @param numberOfMarks the number of marks to give. Could be any number, without needs to check limitation of it.
     * @param fromWho the player witch is giveing marks */
    public void giveMark(int numberOfMarks, Player fromWho) {
        this.marks.addMark(fromWho, numberOfMarks);
        updateObservers(new MarkUpdate(this));
    }

    public int getMarksFromPlayer(Player fromWho) { return this.marks.getMarksFromPlayer(fromWho); }

    void removeAllMarkFromPlayer(Player fromWho) { this.marks.removeAllMarkFromPlayer(fromWho); }

    /** Returns all the damages of this player. */
    public int getTotalDamage() { return this.life.getTotalDamage(); }

    public void setLifeState(ActionGroup lifeState) {
        if(this.lifeState != ActionGroup.FRENZY_TYPE_1 && this.lifeState != ActionGroup.FRENZY_TYPE_2){
            this.lifeState = lifeState;
        }
    }

    public ActionGroup getLifeState() {
        return this.lifeState;
    }

    public void reconnect(){
        online = true;
    }

    public void disconnect() {
        online = false;
        resetWeapon();
        resetPowerUp();
    }

    public boolean isOnline(){
        return online;
    }

    public boolean isWaiting(){
       return  Server.getInstance().getLobby().getWaitingPlayers().contains(this);
    }

    public void setGameMap(GameMap gameMap){
        this.gameMap = gameMap;
    }

    public GameMap getGameMap(){
        return gameMap;
    }

    public void setMatch(Match match){
        this.match = match;
    }

    public void setDeathManager(DeathManager deathManager){
        this.deathManager = deathManager;
    }

    public Match getMatch(){
        return match;
    }

    /** Activates the weapon card given as param
     * @throws WeaponCardException if the weapon is already active
     * @throws WeaponCardException if you do not have this weapon
     * @throws WeaponCardException if the weapon is not loaded
     * */
    public void playWeapon(WeaponCard weaponCard){
        checkHasWeapon(weaponCard);
        weaponCard.setPlayer(this);
        weaponCard.activate();
        activeWeapon = weaponCard;
    }

    /** Activated the powerup card given as param paying the ammo, if it costs
     * @param powerUpCard the card to activate
     * @param ammo the cube you are paying to activate it. could either be null
     * @param paymentPowerUpCard the powerup card you are using to pay for the activation of this card
     * if the card is free or one (and only one) of the colors should be set to 1
     * @throws WeaponCardException if you cannot activate it */
    public void playPowerUp(PowerUpCard powerUpCard, Ammo ammo, PowerUpCard paymentPowerUpCard){
        checkHasPowerUp(powerUpCard);
        if(paymentPowerUpCard!=null){
            checkHasPowerUp(paymentPowerUpCard);
            if(paymentPowerUpCard.equals(powerUpCard)) throw new WeaponCardException("You cannot use the power up you are playing to activate it");
        }
        if(activePowerUp!=null) throw new WeaponCardException("You are already playing a powerup");
        switch (powerUpCard.when){
            case "on_damage_dealt":
                if(playersDamagedByMeThisTurn.size()==0) throw new WeaponCardException("You can only play this card if you damaged someone in this turn");
                break;
            case "my_turn":
                if(!(match.getCurrentPlayer().equals(this) && !match.isDoingAction())) throw  new WeaponCardException("You can only play this powerup in your turn");
                break;
            case "on_damage_received":
                Player lastDamager = getLastDamager();
                if(lastDamager==null) throw new WeaponCardException("You did not receive any damages so you cannot activate this powerup");
                if(!lastDamager.equals(match.getCurrentPlayer())) throw new WeaponCardException("You did non receive any damages in this turn so you cannot activate this powerup");
                break;
            default:
                throw new WeaponCardException("Unhandled 'when' case in powerup " + powerUpCard.name + ": " + powerUpCard.when);
        }
        powerUpCard.setPlayer(this);
        powerUpCard.payPrice(this.ammo, ammo, paymentPowerUpCard);
        powerUpCard.activate();
        activePowerUp = powerUpCard;
    }

    public boolean canReloadWeapon(WeaponCard weaponCard){
        return weaponCard.canReload(ammo, powerUpList);
    }

    public boolean canReloadWeapon(WeaponCard weaponCard, PowerUpCard whichPowerUpCard){
        return weaponCard.canReload(ammo, whichPowerUpCard);
    }

    /**
     * @param weaponCard the card you want to reload
     * @throws WeaponCardException if you do not have that weapon
     * @throws WeaponCardException if the weapon is already loaded
     * @throws NotEnoughAmmoException if you do not have enough ammo to reload */
    public void reloadWeapon(WeaponCard weaponCard, PowerUpCard powerUpCard){
        checkHasWeapon(weaponCard);
        if(weaponCard.isLoaded()) throw new WeaponCardException("The weapon is already loaded");
        weaponCard.load(ammo, powerUpCard);
    }

    /**
     * @return an object containing all the card effects with a TRUE flag on those that can be played.
     * all the flags are set to FALSE if the weapon has not been activated */
    public LegitEffects getWeaponEffects(){
        LegitEffects out = activeWeapon.getEffects(ammo, powerUpList);
        if(out.getLegitEffects().isEmpty()){
            this.resetWeapon();
        }
        return out;
    }

    /** Activates the effect and gives access to playNextWeaponAction() method
     * @throws WeaponCardException if you do not have enough ammo to activate this effect */
    public void playWeaponEffect(Effect e, PowerUpCard powerUpCard){
        activeWeapon.playEffect(e, ammo, powerUpCard);
    }

    public Action playNextWeaponAction(){
        return playNextAction(activeWeapon);
    }

    /** Plays the next action of the powerup. if this method returns null, the active powerup has already been set to null */
    public Action playNextPowerUpAction(){
        Action nextAction = playNextAction(activePowerUp);
        if(nextAction==null){
            throwPowerUp(activePowerUp);
            activePowerUp = null;
        }
        return nextAction;
    }

    /** if the action is a SELECT, you need to check the select TYPE and call one of the getSelectable[item] methods
     * of the weapon (obtainable with getActiveWeapon() method), if the select is of type "auto" just pick the first
     * selectable returned, otherwise ask the user to choose the item to select. Then, call one of the select[item]
     * methods of the weapon to apply the select. Then you can call playNextWeaponAction() again.
     * If the action is either MARK, DAMAGE or MOVE the weapon will do it by itself and you can just call playNextWeaponAction()
     * again.
     * @return the action being played. if null it means the effect is completed and you can play another effect
     *  */
    private Action playNextAction(EffectCard activeCard){
        Action action = activeCard.playNextAction();
        if(action==null) return null;
        switch (action.type){
            case MARK:
                Map<Player, Integer> playersToMark = activeCard.getPlayersToMark();
                for(Player p : playersToMark.keySet()){
                    int marks = playersToMark.get(p);
                    p.giveMark(marks, this);
                }
                break;
            case DAMAGE:
                Map<Player, Integer> playersToDamage = activeCard.getPlayersToDamage();
                for(Player p : playersToDamage.keySet()){
                    int damage = playersToDamage.get(p);
                    p.getDamage(damage, this);
                }
                break;
            case MOVE:
                Map<Player, Square> moves = activeCard.getPlayersMoves();
                for(Map.Entry<Player, Square> entry : moves.entrySet()){
                    gameMap.movePlayer(entry.getKey(), entry.getValue());
                }
                break;
            case SELECT:
                Selectable selectable = activeCard.getSelectable();
                Set<Taggable> selectableSet = selectable.get();
                if(selectableSet.isEmpty()) break;
                if(!action.select.auto)break;
                //auto select
                activeCard.select((Taggable) selectableSet.toArray()[0]);
        }
        return action;
    }

    /**
     * Effect to activate when the user finishes to use a weapon.
     * if the last getWeaponEffects() returned no valid effects, this method has already been called.
     * if the user still had valid effects but chose not to use them, you need to call this manually */
    public void resetWeapon(){
        if(activeWeapon!=null){
            activeWeapon.reset();
            activeWeapon = null;
        }
    }
    public void resetPowerUp(){
        if(activePowerUp!=null){
            activePowerUp.reset();
            activePowerUp = null;
        }
    }
    public WeaponCard getActiveWeapon(){
        return activeWeapon;
    }

    public PowerUpCard getActivePowerUp(){
        return activePowerUp;
    }

    private void checkHasWeapon(WeaponCard weaponCard){
        if(!weaponList.contains(weaponCard)) throw new WeaponCardException("This user does not have this weapon:" + weaponCard.name);
    }
    private void checkHasPowerUp(PowerUpCard powerUpCard){
        if(!powerUpList.contains(powerUpCard)) throw new WeaponCardException("This user does not have this powerup:" + powerUpCard.name);
    }

    public Player getLastDamager() {
        return this.life.getHurtMeLast();
    }

    public void addPartialPointsCount(){
        deathManager.addPartialPointsCount(this, countPoints());
    }

    public void setDeathCount(int value){
        deathManager.setDeathCount(this, value);
    }

    public int getDeathCount(){
        return deathManager.getDeathCount(this);
    }

    public int getSkullCount(List<Player> players){
        return deathManager.getSkullCount(players);
    }

    @Override
    public void register(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void unRegister(Observer observer) {
        observers.remove(observer);
    }

    private void updateObservers(Response update){
        for(Observer observer : observers){
            observer.update(update);
        }
    }
}