package network;

import models.card.Effect;
import models.card.PowerUpCard;
import models.card.Taggable;
import models.card.WeaponCard;
import models.map.RoomColor;
import models.map.Square;
import models.player.Ammo;
import models.turn.ActionType;
import models.turn.TurnEvent;

import java.util.List;
import java.util.Map;

public interface Connection {
    void init();
    String getToken();
    void setToken(String token);
    void registerPlayer(String username, String password);
    void getWaitingPlayer();
    void validActions();
    void chooseMap(int map);
    void cardEffects(String cardName);
    void drawPowerUpCard();
    void spawnPlayer(PowerUpCard powerUpCard);
    void tagElement(Taggable taggable);
    void powerUpTagElement(Taggable taggable);
    void playEffect(Effect effect, Ammo ammo, PowerUpCard powerUpCard);
    void finishCard();
    void endTurn();
    void grab(WeaponCard drawn, WeaponCard toRelease, PowerUpCard powerUpCard);
    void run(TurnEvent turnEvent, Square square);
    void reload(Map<WeaponCard, PowerUpCard> weaponsMap);
    void action(ActionType actionType);
    void playPowerUp(String powerUpName, Ammo ammo, PowerUpCard powerUpAmmo);
    void reconnect();
    void receiveResponse(Response response);
}