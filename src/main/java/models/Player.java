package models;

import java.util.ArrayList;
import java.util.List;

public class Player {

    public enum color {
        YELLOW,
        GREY,
        BLUE,
        PURPLE,
        GREEN,

    }

    private boolean isFirstPlayer;
    //private Miniatura miniatura;
    private color color;
    private Ammo ammo;
    private List<WeaponCard> weaponList = new ArrayList<>();
    private List<PowerUpCard> powerUpList = new ArrayList<>();

    public void setFirstPlayer(boolean firstPlayer) {
        isFirstPlayer = firstPlayer;
    }

    public boolean isFirstPlayer() {
        return isFirstPlayer;
    }

    public void setColor(Player.color color) {
        this.color = color;
    }

    public color getColor() {
        return color;
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
}