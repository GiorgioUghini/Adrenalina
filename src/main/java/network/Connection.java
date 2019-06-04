package network;

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
    void receiveResponse(Response response);
}
