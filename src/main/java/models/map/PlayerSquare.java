package models.map;

import models.Player;

import java.util.*;

public class PlayerSquare {
    private Map<Player, Square> hashMap;

    public PlayerSquare(){
        hashMap = new HashMap<>();
    }

    public boolean addPlayer(Player player, Square square){
        if(hasPlayer(player)) return false;
        hashMap.put(player, square);
        return true;
    }

    
    public Square movePlayer(Player player, Square newSquare){
        return hashMap.put(player, newSquare);
    }

    public boolean hasPlayer(Player player){
        return hashMap.containsKey(player);
    }

    public Square getSquare(Player player){
        return hashMap.get(player);
    }
    public Set<Player> getPlayers(Square square){
        Set<Player> players = new HashSet<>();
        for(Map.Entry entry : hashMap.entrySet()){
            if(entry.getValue().equals(square)){
                players.add((Player) entry.getKey());
            }
        }
        return players;
    }
}
