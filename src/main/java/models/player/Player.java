package models.player;

import models.PowerUpCard;
import models.WeaponCard;
import models.turn.ActionGroup;
import network.TokenGenerator;

import java.io.Serializable;
import java.util.*;

public class Player implements Subscriber, Serializable {

    private boolean isFirstPlayer;
    private String name;
    private Ammo ammo;
    private List<WeaponCard> weaponList;
    private List<PowerUpCard> powerUpList;
    private Life life;
    private Mark marks;
    private int points;
    private int numberOfSkulls;
    private ActionGroup lifeState = ActionGroup.NORMAL;
    private String token;

    /** Creates a new player object
     * @param isFirstPlayer the boolean witch indicates whether this player should be the one who has the "First Player Mark"
     * @param name The name identifier for the player
     * */
    public Player(boolean isFirstPlayer, String name){
        this.isFirstPlayer = isFirstPlayer;
        this.name = name;
        ammo = new Ammo();
        weaponList = new ArrayList<>();
        powerUpList = new ArrayList<>();
        life = new Life(this);
        points = 0;
        numberOfSkulls = 0;
        marks = new Mark();
        //token generation
        this.token = TokenGenerator.nextToken();
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

    public void setFirstPlayer(boolean firstPlayer) {
        isFirstPlayer = firstPlayer;
    }

    public boolean isFirstPlayer() {
        return isFirstPlayer;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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

    public String getToken() {
        return this.token;
    }
}