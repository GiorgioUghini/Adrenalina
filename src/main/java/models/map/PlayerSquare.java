package models.map;

import models.Player;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class PlayerSquare {
    Hashtable<Player, Square> hashtable;

    public PlayerSquare(){
        hashtable = new Hashtable<Player, Square>();
    }

    public boolean addPlayer(Player player, Square square){
        if(hasPlayer(player)) return false;
        hashtable.put(player, square);
        return true;
    }

    
    public Square movePlayer(Player player, Square newSquare){
        return hashtable.put(player, newSquare);
    }

    public boolean hasPlayer(Player player){
        return hashtable.containsKey(player);
    }

    public Square getSquare(Player player){
        return hashtable.get(player);
    }
    public Set<Player> getPlayers(Square square){
        Set<Player> players = new HashSet<Player>();
        for(Map.Entry entry : hashtable.entrySet()){
            if(entry.getValue().equals(square)){
                players.add((Player) entry.getKey());
            }
        }
        return players;
    }
}
