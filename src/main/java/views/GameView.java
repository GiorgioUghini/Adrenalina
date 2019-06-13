package views;

import models.card.PowerUpCard;
import models.card.WeaponCard;
import models.map.GameMap;
import models.turn.ActionType;

public interface GameView extends View {
    void chooseSpawnPoint();

    void setBtnDrawPowerUpVisibility(boolean isVisible);
    void setBtnGrabWeaponVisibility(boolean isVisible);
    void setBtnSpawnVisibility(boolean isVisible);
    void setBtnRunVisibility(boolean isVisible);
    void setBtnGrabAmmoVisibility(boolean isVisible);
    void setBtnShootVisibility(boolean isVisible);
    void setBtnReloadVisibility(boolean isVisible);
    void setBtnUsePowerUpVisibility(boolean isVisible);

    void addPowerUpToHand(PowerUpCard card);
    void addWeaponToHand(WeaponCard card);

    void getValidActions();
    void startTurn(String name);

    void updateMapView(GameMap map);

    void setTextAndEnableBtnActionGroup(ActionType actionType, int btnNum);

}
