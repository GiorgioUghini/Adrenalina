package views;

import models.card.LegitEffects;
import models.card.Selectable;
import models.map.GameMap;
import models.player.Player;
import models.turn.ActionType;
import models.turn.TurnEvent;

import java.util.List;
import java.util.Map;

public interface GameView extends View {

    void getValidActions();
    void reconnect();
    void startTurn(String name);

    void updateMapView(GameMap map);
    void updatePlayerView(Player newPlayer);
    void updateDamagedAndMarkedPlayer(Player newPlayer);
    void updateActions(Map<ActionType, List<TurnEvent>> actions);

    void updatePoints(Map<Player, Integer> map);
    void effectChoosingDialog(LegitEffects legitEffects);

    void setTextAndEnableBtnActionGroup(ActionType actionType, int btnNum);

    void selectTag(Selectable selectable);

    void continueWeapon();
    void onEndWeapon();

}
