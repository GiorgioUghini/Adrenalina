package models.card;

import errors.WeaponCardException;
import models.map.GameMap;
import models.map.RoomColor;
import models.map.Square;
import models.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class DamageEngine {
    private Mark markOrDamage;
    private Map<String, Player> selectedPlayers;
    private Map<String, Square> selectedSquares;
    private Map<String, RoomColor> selectedRooms;
    private GameMap gameMap;
    private Player me;

    DamageEngine(Mark markOrDamage, Map<String, Player> selectedPlayers, Map<String, Square> selectedSquares, Map<String, RoomColor> selectedRooms, GameMap gameMap, Player me) {
        this.markOrDamage = markOrDamage;
        this.selectedPlayers = selectedPlayers;
        this.selectedSquares = selectedSquares;
        this.selectedRooms = selectedRooms;
        this.gameMap = gameMap;
        this.me = me;
    }

    /**
     * calculates the damages that each player should take
     *
     * @return a map containing for each player its damages. can be empty
     */
    Map<Player, Integer> getDamages() {
        Map<Player, Integer> out = new HashMap<>();
        switch (markOrDamage.type) {
            case PLAYER:
                Player player = selectedPlayers.get(markOrDamage.target);
                if (player == null) return out;
                out.put(selectedPlayers.get(markOrDamage.target), markOrDamage.value);
                break;
            case SQUARE:
                Square target = getSquareByTag(markOrDamage.target);
                if (target == null) return out;
                Set<Player> playersOnSquare = gameMap.getPlayersOnSquare(target);
                for (Player p : playersOnSquare) {
                    out.put(p, markOrDamage.value);
                }
                out.remove(me);
                break;
            case ROOM:
                Set<Square> squaresInRoom = gameMap.getAllSquaresInRoom(selectedRooms.get(markOrDamage.target));
                for (Square s : squaresInRoom) {
                    for (Player p : gameMap.getPlayersOnSquare(s)) {
                        out.put(p, markOrDamage.value);
                    }
                }
                break;
            default:
                throw new WeaponCardException("The damage type is not valid: " + markOrDamage.type);
        }

        if (markOrDamage instanceof Damage) {
            Damage damage = (Damage) markOrDamage;
            if (!damage.except.isEmpty()) {
                for (String tag : damage.except) {
                    out.remove(selectedPlayers.get(tag));
                }
            }
        }

        return out;
    }

    /**
     * Given a tag, returns a square
     *
     * @param tag a string representing the tag
     * @return a square that can be null
     */
    private Square getSquareByTag(String tag) {
        if (tag.equals("me")) {
            return gameMap.getPlayerPosition(me);
        } else if (selectedSquares.containsKey(tag)) {
            return selectedSquares.get(tag);
        } else if (selectedPlayers.containsKey(tag)) {
            return gameMap.getPlayerPosition(selectedPlayers.get(tag));
        }
        return null;
    }
}
