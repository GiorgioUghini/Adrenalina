package network;

import models.card.Effect;
import models.card.Taggable;
import models.map.RoomColor;

import java.util.List;

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
    void spawnPlayer(RoomColor color);
    void tagElement(Taggable taggable);
    void playEffect(Effect effect);
    void finishCard();
    void receiveResponse(Response response);

}