package models.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeathManager implements Serializable {
    private List<Map<Player, Integer>> pointsMapList;
    private Map<Player, Integer> deathMap;


    public DeathManager() {
        pointsMapList = new ArrayList<>();
        deathMap = new HashMap<>();
    }

    public void addPartialPointsCount(Player player, Map<Player, Integer> pointsCount) {
        pointsMapList.add(pointsCount);
        Integer deathCount = deathMap.get(player);
        if (deathCount != null) {
            deathMap.put(player, deathCount + 1);
        } else {
            deathMap.put(player, 1);
        }
    }

    public int getTotalPoints(Player player) {
        int points = 0;
        for (Map<Player, Integer> map : pointsMapList) {
            points = points + (map.get(player) != null ? map.get(player) : 0);
        }
        return points;
    }

    public int getDeathCount(Player player){
        return deathMap.get(player) != null ? deathMap.get(player) : 0;
    }

    public Map<Player, Integer> getTotalPoints(List<Player> players) {
        Map<Player, Integer> points = new HashMap<>();
        for (Player player : players) {
            points.put(player, getTotalPoints(player));
        }
        return points;
    }
}