package models.player;

import models.PowerUpCard;
import models.WeaponCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Player implements Subscriber {

    private boolean isFirstPlayer;
    private String name;
    private Ammo ammo;
    private List<WeaponCard> weaponList;
    private List<PowerUpCard> powerUpList;
    private Life life;
    private Mark marks;
    private int points;
    private int numerOfSkulls;

    public Player(boolean isFirstPlayer, String name){
        this.isFirstPlayer = isFirstPlayer;
        this.name = name;
        ammo = new Ammo();
        weaponList = new ArrayList<>();
        powerUpList = new ArrayList<>();
        life = new Life(this);
        points = 0;
        numerOfSkulls = 0;
        marks = new Mark(this);
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

    public int getNumberOfSkulls() { return this.numerOfSkulls; }

    public void setNumerOfSkulls(int numerOfSkulls) { this.numerOfSkulls = numerOfSkulls; }

    public Map<Player, Integer> getDamages() {
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

    public void getDamaged(int damage, Player attacker) { this.life.damage(damage, attacker); }

    public Map<Player, Integer> countPoints() { return this.life.countPoints(); }

    public void clearDamages() { this.life.clearDamages(); }

    public int marksIDistribuited() { return this.marks.marksIDistribuited(); }

    public void addMark(Player fromWho, int numberOfMarks) { this.marks.addMark(fromWho, numberOfMarks); }

    public int getMarksFromPlayer(Player fromWho) { return this.marks.getMarksFromPlayer(fromWho); }

    public void removeAllMarkFromPlayer(Player fromWho) { this.marks.removeAllMarkFromPlayer(fromWho); }

    public void hasJustMarkedPlayer(Player who, int numberOfMarks) { this.marks.hasJustMarkedPlayer(who, numberOfMarks); }

 }