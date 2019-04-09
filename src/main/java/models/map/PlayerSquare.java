package models.map;

import models.player.Player;

import java.util.*;

public class PlayerSquare {
    private Map<Player, Square> hashMap;

    public PlayerSquare(){
        hashMap = new HashMap<>();
    }

    /** Adds player to map
     * @param player the player you are going to add
     * @param square the square on which the player will end up
     * @return true if player was added to the map, false if it was already on the map
     * */
    public boolean addPlayer(Player player, Square square){
        if(hasPlayer(player)) return false;
        hashMap.put(player, square);
        return true;
    }
    public int getPlayersNumber(){
        return hashMap.size();
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
    /** get all players on map
     * @return a set containing all players on map */
    public Set<Player> getAllPlayers(){
        return hashMap.keySet();
    }
    /** Removes player from hashMap if exists */
    public void removePlayer(Player player){
        hashMap.remove(player);
    }
}
