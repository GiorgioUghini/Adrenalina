package models.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeathManager {
    private List<Map<Player, Integer>> mapList;

    public DeathManager(){
        mapList = new ArrayList<>();
    }

    void addPartialPointsCount(Map<Player, Integer> pointsCount){
        mapList.add(pointsCount);
    }

    int getTotalPoints(Player player){
        int points = 0;
        for(Map<Player, Integer> map : mapList){
            points = points + map.get(player);
        }
        return points;
    }

    Map<Player, Integer> getTotalPoints(List<Player> players){
        Map<Player, Integer> points = new HashMap<>();
        for(Player player : players){
            points.put(player, getTotalPoints(player));
        }
        return points;
    }
}