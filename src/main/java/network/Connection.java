package network;

import models.card.Effect;
import models.card.Taggable;
import models.card.WeaponCard;
import models.map.RoomColor;
import models.map.Square;
import models.turn.ActionType;
import models.turn.TurnEvent;

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
    void endTurn();
    void grab();
    void run(TurnEvent turnEvent, Square square);
    void reload(List<WeaponCard> weapons);
    void action(ActionType actionType);
    void receiveResponse(Response response);
}