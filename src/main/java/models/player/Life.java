package models.player;

import java.util.*;

public class Life {
    private static final int MAX_LIFEPOINTS = 12;
    private static final int FIRST_BLOOD_POINTS = 1;
    private static final int[] assignablePoints = {8, 6, 4, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

    private Map<Player, Integer> myDamages;
    private List<Player> observingPlayers;
    private Map<Player, Integer> marks;
    private Player me;

    public Life(Player me) {
        this.me = me;
        myDamages = new LinkedHashMap<>();
        observingPlayers = new ArrayList<>();
        marks = new HashMap<>();
    }

    public Map<Player, Integer> getMyDamages() {
        return this.myDamages;
    }

    public void addObserver(Player subscriber) {
        this.observingPlayers.add(subscriber);
    }

    public void removeObserver(Player subscriber) {
        this.observingPlayers.remove(subscriber);
    }

    public boolean isDead() {
        return (myDamages.size() > 10);
    }

    public void damage(int damage, Player attacker) {
        Integer totalDamage = myDamages.values().stream().mapToInt(Integer::intValue).sum();

        if (totalDamage+damage > 12) {
            damage = 12 - totalDamage;
        }

        Optional<Integer> oldDamage = Optional.ofNullable(myDamages.get(attacker));
        myDamages.put(attacker, damage + oldDamage.orElse(0));

        //Check if the player's dead
        if (totalDamage > 10) {
            //OBSERVER PATTERN
            for (Player subscriber : this.observingPlayers) {
                subscriber.update(me);
            }
        }
    }

    public Map<Player, Integer> countPoints() {
        Map<Player, Integer> result = new HashMap<>();

        if (myDamages.isEmpty()) {
            return result;
        } else {

            Player firstAttacker = myDamages.entrySet().stream().reduce((first, second) -> second).orElse(null).getKey();
            result.put(firstAttacker, FIRST_BLOOD_POINTS);

            int countingOrder = 0;
            while (!myDamages.isEmpty()) {
                Map.Entry<Player, Integer> maxEntry = null;

                for (Map.Entry<Player, Integer> entry : myDamages.entrySet()) {
                    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                        maxEntry = entry;
                    }
                }

                myDamages.remove(maxEntry.getKey());
                Optional<Integer> oldDamage = Optional.ofNullable(myDamages.get(maxEntry.getKey()));
                myDamages.put(maxEntry.getKey(), assignablePoints[me.getNumerOfSkulls() + countingOrder] + oldDamage.orElse(0));
            }
        }
        return result;

    }

}
