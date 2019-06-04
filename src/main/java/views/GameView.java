package views;

public interface GameView extends View {
    void chooseSpawnPoint();

    void setBtnDrawPowerUpVisibility(boolean isVisible);
    void setBtnGrabWeapon(boolean isVisible);
    void setBtnSpawn(boolean isVisible);
    void setBtnRun(boolean isVisible);
    void setBtnGrabAmmo(boolean isVisible);
    void setBtnShoot(boolean isVisible);
    void setBtnReload(boolean isVisible);
    void setBtnUsePowerUp(boolean isVisible);

}
