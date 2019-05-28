package network;

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
    void receiveResponse(Response response);
}
