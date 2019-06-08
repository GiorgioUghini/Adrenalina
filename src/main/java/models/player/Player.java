package models.player;

import errors.WeaponCardException;
import models.card.*;
import models.map.GameMap;
import models.map.Square;
import models.turn.ActionGroup;
import network.Server;

import java.io.Serializable;
import java.util.*;

public class Player implements Subscriber, Serializable, Taggable {

    private String name;
    private String password;
    private Ammo ammo;
    private List<WeaponCard> weaponList;
    private WeaponCard activeWeapon;
    private List<PowerUpCard> powerUpList;
    private Life life;
    private Mark marks;
    private int points;
    private int numberOfSkulls;
    private ActionGroup lifeState = ActionGroup.NORMAL;
    private boolean online;
    private GameMap gameMap;
    private boolean hasJustStarted;


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

    public void drawPowerUp(PowerUpCard drawn) {
        powerUpList.add(drawn);
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

    public void setPowerUpList(List<PowerUpCard> powerUpList) {
        this.powerUpList = powerUpList;
    }

    public List<PowerUpCard> getPowerUpList() {
        return powerUpList;
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

    public void addObserver(Player subscriber) {
        this.life.addObserver(subscriber);
    }

    public void removeObserver(Player subscriber) {
        this.life.removeObserver(subscriber);
    }

    public boolean isDead() {
        return this.life.isDead();
    }

    /** Damage this player
     * @param damage the number of damage to give. Could be any number, without needs to check limitation of it.
     * @param attacker the player witch is attacking
     * */
    public void getDamage(int damage, Player attacker) { this.life.damage(damage, attacker); }

    /** Counts the points to assign to each player
     * @return a map between Player and the points to assign him
     * */
    public Map<Player, Integer> countPoints() { return this.life.countPoints(); }

    /** Clears all the damage map of this player. */
    public void clearDamages() { this.life.clearDamages(); }

    /** Damage this player
     * @param numberOfMarks the number of marks to give. Could be any number, without needs to check limitation of it.
     * @param fromWho the player witch is giveing marks */
    public void giveMark(int numberOfMarks, Player fromWho) { this.marks.addMark(fromWho, numberOfMarks); }

    int getMarksFromPlayer(Player fromWho) { return this.marks.getMarksFromPlayer(fromWho); }

    void removeAllMarkFromPlayer(Player fromWho) { this.marks.removeAllMarkFromPlayer(fromWho); }

    /** Returns all the damages of this player. */
    public int getTotalDamage() { return this.life.getTotalDamage(); }

    void setLifeState(ActionGroup lifeState) {
        this.lifeState = lifeState;
    }

    public ActionGroup getLifeState() {
        return this.lifeState;
    }

    public void reconnect(){
        online = true;
    }

    public void disconnect() {
        online = false;
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

    /** Activates the weapon card given as param
     * @throws WeaponCardException if you do not have this weapon
     * @throws WeaponCardException if the weapon is not loaded
     * */
    public void playWeapon(WeaponCard weaponCard){
        checkHasWeapon(weaponCard);
        weaponCard.activate(this);
        activeWeapon = weaponCard;
    }

    public boolean canReloadWeapon(WeaponCard weaponCard){
        return weaponCard.canReload(ammo);
    }

    /**
     * @param weaponCard the card you want to reload
     * @throws WeaponCardException if you do not have that weapon
     * @throws WeaponCardException if the weapon is already loaded
     * @throws WeaponCardException if you do not have enough ammo to reload */
    public void loadWeapon(WeaponCard weaponCard){
        checkHasWeapon(weaponCard);
        if(weaponCard.isLoaded()) throw new WeaponCardException("The weapon is already loaded");
        weaponCard.load(ammo);
    }

    /**
     * @return an object containing all the card effects with a TRUE flag on those that can be played.
     * all the flags are set to FALSE if the weapon has not been activated */
    public LegitEffects getWeaponEffects(){
        LegitEffects out = activeWeapon.getEffects(ammo);
        if(out.getLegitEffects().isEmpty()){
            this.resetWeapon();
        }
        return out;
    }

    /** Activates the effect and gives access to playNextWeaponAction() method
     * @throws WeaponCardException if you do not have enough ammo to activate this effect */
    public void playWeaponEffect(Effect e){
        activeWeapon.playEffect(e, ammo);
    }

    /** if the action is a SELECT, you need to check the select TYPE and call one of the getSelectable[item] methods
     * of the weapon (obtainable with getActiveWeapon() method), if the select is of type "auto" just pick the first
     * selectable returned, otherwise ask the user to choose the item to select. Then, call one of the select[item]
     * methods of the weapon to apply the select. Then you can call playNextWeaponAction() again.
     * If the action is either MARK, DAMAGE or MOVE the weapon will do it by itself and you can just call playNextWeaponAction()
     * again.
     * @return the action being played. if null it means the effect is completed and you can play another effect
     *  */
    public Action playNextWeaponAction(){
        Action action = activeWeapon.playNextAction();
        if(action==null) return null;
        switch (action.type){
            case MARK:
                Map<Player, Integer> playersToMark = activeWeapon.getPlayersToMark();
                for(Player p : playersToMark.keySet()){
                    int marks = playersToMark.get(p);
                    p.giveMark(marks, this);
                }
                break;
            case DAMAGE:
                Map<Player, Integer> playersToDamage = activeWeapon.getPlayersToDamage();
                for(Player p : playersToDamage.keySet()){
                    int damage = playersToDamage.get(p);
                    p.getDamage(damage, this);
                }
                break;
            case MOVE:
                Map<Player, Square> moves = activeWeapon.getPlayersMoves();
                GameMap gameMap = Server.getInstance().getLobby().getMatch(this).getMap();
                for(Map.Entry<Player, Square> entry : moves.entrySet()){
                    gameMap.movePlayer(entry.getKey(), entry.getValue());
                }
                break;
            case SELECT:
                if(!action.select.auto)break;
                Selectable selectable = activeWeapon.getSelectable();
                Set<Taggable> selectableSet = selectable.get();
                if(selectableSet.isEmpty()) break;
                activeWeapon.select((Taggable) selectableSet.toArray()[0]);
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
    public WeaponCard getActiveWeapon(){
        return activeWeapon;
    }

    private void checkHasWeapon(WeaponCard weaponCard){
        if(!weaponList.contains(weaponCard)) throw new WeaponCardException("This user does not have this weapon:" + weaponCard.name);
    }

}