package models.card;

import models.map.GameMap;
import models.map.Square;
import models.player.Player;

import java.util.HashMap;
import java.util.Map;

class MoveEngine {
    private Move move;
    private Map<String, Player> selectedPlayers;
    private Map<String, Square> selectedSquares;
    private GameMap gameMap;
    private Player me;

    MoveEngine(Move move, Map<String, Player> selectedPlayers, Map<String, Square> selectedSquares, GameMap gameMap, Player me) {
        this.move = move;
        this.selectedPlayers = selectedPlayers;
        this.selectedSquares = selectedSquares;
        this.gameMap = gameMap;
        this.me = me;
    }

    /**
     * @return a Map containing the player and the position it will go to
     */
    Map<Player, Square> getNewPositions() {
        Map<Player, Square> out = new HashMap<>();
        Player playerToMove = getPlayerByTag(move.target);
        Square destinationSquare = getSquareByTag(move.dest);
        if (playerToMove == null || destinationSquare == null) return out;
        out.put(playerToMove, destinationSquare);
        return out;
    }

    /**
     * @return a square if the tag exists, null otherwise
     */
    private Square getSquareByTag(String tag) {
        if (tag.equals("me")) {
            return gameMap.getPlayerPosition(me);
        }
        if (selectedPlayers.containsKey(tag)) {
            Player player = selectedPlayers.get(tag);
            return gameMap.getPlayerPosition(player);
        } else if (selectedSquares.containsKey(tag)) {
            return selectedSquares.get(tag);
        }
        return null;
    }

    /**
     * @return a Player if the tag exists, null otherwise
     */
    private Player getPlayerByTag(String tag) {
        if (tag.equals("me")) {
            return me;
        } else if (selectedPlayers.containsKey(tag)) {
            return selectedPlayers.get(tag);
        }
        return null;
    }
}
