package models.player;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DeathManagerTest {
    @Test
    public void testAddPoints(){
        DeathManager deathManager = new DeathManager();
        Player a = new Player("a", "");
        Player b = new Player("b", "");

        Map<Player, Integer> pointsCount = new HashMap<>();
        pointsCount.put(b, 11);

        deathManager.addPartialPointsCount(a, pointsCount);
        deathManager.addPartialPointsCount(a, pointsCount);

        assertEquals(0, deathManager.getTotalPoints(a));
        assertEquals(22, deathManager.getTotalPoints(b));

        List<Player> players = new ArrayList<>();
        players.add(a);
        assertEquals(2, deathManager.getSkullCount(players));
        players.clear();
        players.add(b);
        assertEquals(0, deathManager.getSkullCount(players));

        players.add(a);
        Map<Player, Integer> totPoints = deathManager.getTotalPoints(players);
        assertEquals(2, totPoints.size());
    }
}
