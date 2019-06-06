package models.card;

import models.map.GameMap;
import models.map.Square;
import models.player.Player;

import java.util.HashMap;
import java.util.Map;

public class MoveEngine {
    private Move move;
    private Map<String, Player> selectedPlayers;
    private Map<String, Square> selectedSquares;
    private GameMap gameMap;
    Player me;

    public MoveEngine(Move move, Map<String, Player> selectedPlayers, Map<String, Square> selectedSquares, GameMap gameMap, Player me){
        this.move = move;
        this.selectedPlayers = selectedPlayers;
        this.selectedSquares = selectedSquares;
        this.gameMap = gameMap;
        this.me = me;
    }

    public Map<Player, Square> getNewPositions(){
        Map<Player, Square> out = new HashMap<>();
        Player playerToMove = selectedPlayers.get(move.target);
        Square destinationSquare = getSquareByTag(move.dest);
        if(playerToMove==null || destinationSquare == null) return out;
        out.put(playerToMove, destinationSquare);
        return out;
    }

    private Square getSquareByTag(String tag){
        if(selectedPlayers.containsKey(tag)){
            Player player = selectedPlayers.get(tag);
            return gameMap.getPlayerPosition(player);
        }else if(selectedSquares.containsKey(tag)){
            return selectedSquares.get(tag);
        }
        return null;
    }
}
