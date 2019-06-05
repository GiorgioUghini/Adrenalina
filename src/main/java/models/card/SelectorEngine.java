package models.card;

import models.map.CardinalDirection;
import models.map.GameMap;
import models.map.Square;
import models.player.Player;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SelectorEngine {
    private GameMap gameMap;
    private Player me;
    private Select select;
    private Map<String, Player> selectedPlayers;
    private Map<String, Square> selectedSquares;

    SelectorEngine(GameMap gameMap, Player me, Select select, Map<String, Player> selectedPlayers, Map<String, Square> selectedSquares){
        this.gameMap = gameMap;
        this.me = me;
        this.select = select;
        this.selectedPlayers = selectedPlayers;
        this.selectedSquares = selectedSquares;
    }

    Set<Player> getSelectablePlayers(){
        Set<Player> out = new HashSet<>();
        if(select.rules.include!=null){
            for(String tag : select.rules.include){
                out.addAll(getAllPlayersWithTag(tag));
            }
            if(select.rules.exclude!=null){
                for(String tag : select.rules.exclude){
                    out.removeAll(getAllPlayersWithTag(tag));
                }
            }
            removeMe(out);
            return out;
        }

        Set<Square> radix = getAllSquaresInRadix(select.radix[0]);
        for(int i=1; i<select.radix.length; i++){
            radix.retainAll(getAllSquaresInRadix(select.radix[i]));
            if(radix.isEmpty()) return out;
        }

        if(select.min==0) out.add(new Player(null, null));
        removeMe(out);
        return out;
    }

    private Set<Square> getAllSquaresInRadix(Radix radix){
        Set<Square> out = new HashSet<>();
        Square ref = getSquareWithTag(radix.ref);
        if(radix.area != null){
            switch (radix.area){
                case "visible":
                    out.addAll(gameMap.getAllVisibleSquares(ref));
                    break;
                case "not_visible":
                    out.addAll(gameMap.getAllSquares());
                    out.removeAll(gameMap.getAllVisibleSquares(ref));
                    break;
                case "cardinal":
                    for(CardinalDirection direction : CardinalDirection.values()){
                        out.addAll(gameMap.getAllSquaresByCardinal(ref, direction, !radix.throughWalls));
                    }
                    break;
            }
        }
        return out;
    }

    private Set<Player> getAllPlayersWithTag(String tag){
        Set<Player> out = new HashSet<>();
        if(selectedPlayers.containsKey(tag)){
            out.add(selectedPlayers.get(tag));
        }else if(selectedSquares.containsKey(tag)){
            out.addAll(gameMap.getPlayersOnSquare(selectedSquares.get(tag)));
        }
        return out;
    }

    private Square getSquareWithTag(String tag){
        Square out = null;
        if(tag.equals("me")){
            out = gameMap.getPlayerPosition(me);
        }else if(selectedPlayers.containsKey(tag)){
            out = gameMap.getPlayerPosition(selectedPlayers.get(tag));
        }else if(selectedSquares.containsKey(tag)){
            out = selectedSquares.get(tag);
        }
        return out;
    }

    private void removeMe(Set<Player> players){
        players.remove(me);
    }
}
