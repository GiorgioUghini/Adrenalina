package models.card;

import errors.WeaponCardException;
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
        }else{
            Set<Square> radix = gameMap.getAllSquares();
            for(int i=0; i<select.radix.length; i++){
                radix.retainAll(getAllSquaresInRadix(select.radix[i]));
                if(radix.isEmpty()) return out;
            }
        }

        if(select.rules.exclude!=null){
            for(String tag : select.rules.exclude){
                out.removeAll(getAllPlayersWithTag(tag));
            }
        }
        if(select.min==0) out.add(new Player(null, null));
        removeMe(out);
        return out;
    }

    private Set<Square> getAllSquaresInRadix(Radix radix){
        Set<Square> out = gameMap.getAllSquares();
        Square ref = getSquareWithTag(radix.ref);
        if(radix.area != null){
            switch (radix.area){
                case "visible":
                    out.retainAll(gameMap.getAllVisibleSquares(ref));
                    break;
                case "not_visible":
                    out.removeAll(gameMap.getAllVisibleSquares(ref));
                    break;
                case "cardinal":
                    for(CardinalDirection direction : CardinalDirection.values()){
                        out.retainAll(gameMap.getAllSquaresByCardinal(ref, direction, !radix.throughWalls));
                    }
                    break;
            }
        }
        if(radix.min != 0 || radix.max != -1){
            if(radix.min>0) {
                out.removeAll(gameMap.getAllSquaresAtDistanceLessThanOrEquals(ref, radix.min-1));
            }
            if(radix.max != -1){
                out.retainAll(gameMap.getAllSquaresAtDistanceLessThanOrEquals(ref, radix.max));
            }
        }
        if(radix.straight!=null){
            Square to = getSquareWithTag(radix.straight);
            if(to==null)throw new WeaponCardException("The tag of a 'straight' radix must exist");
            CardinalDirection direction = gameMap.getDirection(ref, to);
            out.retainAll(gameMap.getAllSquaresByCardinal(ref, direction, !radix.throughWalls));
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
