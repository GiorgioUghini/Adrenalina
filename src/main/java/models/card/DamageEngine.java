package models.card;

import errors.WeaponCardException;
import models.map.GameMap;
import models.map.RoomColor;
import models.map.Square;
import models.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DamageEngine {
    private Mark markOrDamage;
    private ActionType actionType;
    private Map<String, Player> selectedPlayers;
    private Map<String, Square> selectedSquares;
    private Map<String, RoomColor> selectedRooms;
    private GameMap gameMap;
    private Player me;

    public DamageEngine(Mark markOrDamage, Map<String, Player> selectedPlayers, Map<String, Square> selectedSquares, Map<String, RoomColor> selectedRooms, GameMap gameMap, Player me){
        this.markOrDamage = markOrDamage;
        this.selectedPlayers = selectedPlayers;
        this.selectedSquares = selectedSquares;
        this.selectedRooms = selectedRooms;
        this.gameMap = gameMap;
        this.me = me;
    }

    public Map<Player, Integer> getDamages(){
        Map<Player, Integer> out = new HashMap<>();

        switch (markOrDamage.type){
            case PLAYER:
                out.put(selectedPlayers.get(markOrDamage.target), markOrDamage.value);
                break;
            case SQUARE:
                Set<Player> playersOnSquare = gameMap.getPlayersOnSquare(selectedSquares.get(markOrDamage.target));
                for(Player player : playersOnSquare){
                    out.put(player, markOrDamage.value);
                }
                out.remove(me);
                break;
            case ROOM:
                Set<Square> squaresInRoom = gameMap.getAllSquaresInRoom(selectedRooms.get(markOrDamage.target));
                for(Square s : squaresInRoom){
                    for(Player p : gameMap.getPlayersOnSquare(s)){
                        out.put(p, markOrDamage.value);
                    }
                }
            default:
                throw new WeaponCardException("The damage type is not valid: " + markOrDamage.type);
        }

        if(markOrDamage instanceof Damage){
            Damage damage = (Damage) markOrDamage;
            if(!damage.except.isEmpty()){
                for(String tag : damage.except){
                    out.remove(selectedPlayers.get(tag));
                }
            }
        }

        return out;
    }
}
