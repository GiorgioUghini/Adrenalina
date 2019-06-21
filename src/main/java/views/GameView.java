package views;

import models.map.GameMap;
import models.player.Player;
import models.turn.ActionType;
import models.turn.TurnEvent;

import java.util.List;
import java.util.Map;

public interface GameView extends View {
    void chooseSpawnPoint();

    void getValidActions();
    void reconnect();
    void startTurn(String name);

    void updateMapView(GameMap map);
    void updatePlayerView(Player newPlayer);
    void updateActions(Map<ActionType, List<TurnEvent>> actions);

    void setTextAndEnableBtnActionGroup(ActionType actionType, int btnNum);

}
