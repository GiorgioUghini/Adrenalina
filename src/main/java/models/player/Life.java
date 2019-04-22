package models.player;

import java.util.*;

class Life {
    private static final int MAX_LIFEPOINTS = 12;
    private static final int FIRST_BLOOD_POINTS = 1;
    private static final int[] assignablePoints = {8, 6, 4, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

    private Map<Player, Integer> myDamages;
    private List<Player> observingPlayers;
    private Player me;

    Life(Player me) {
        this.me = me;
        myDamages = new LinkedHashMap<>();
        observingPlayers = new ArrayList<>();
    }

    Map<Player, Integer> getMyDamages() {
        return this.myDamages;
    }

    void addObserver(Player subscriber) {
        this.observingPlayers.add(subscriber);
    }

    void removeObserver(Player subscriber) {
        this.observingPlayers.remove(subscriber);
    }

    boolean isDead() {
        int totalDamage = myDamages.values().stream().mapToInt(Integer::intValue).sum();
        return (totalDamage > 10);
    }

    int getTotalDamage() {
        return myDamages.values().stream().mapToInt(Integer::intValue).sum();
    }

    void damage(int damage, Player attacker) {
        int additionalDamage = me.getMarksFromPlayer(attacker);
        me.removeAllMarkFromPlayer(attacker);
        damage = damage + additionalDamage;
        int totalDamage = myDamages.values().stream().mapToInt(Integer::intValue).sum();

        if (totalDamage+damage > MAX_LIFEPOINTS) {
            damage = MAX_LIFEPOINTS - totalDamage;
        }

        Optional<Integer> oldDamage = Optional.ofNullable(myDamages.get(attacker));
        myDamages.put(attacker, damage + oldDamage.orElse(0));
        totalDamage = damage + oldDamage.orElse(0);
        if (totalDamage == 12) {    //Give revenge mark
            attacker.giveMark(1, me);
        }

        //Check if the player's dead
        if (totalDamage > 10) {
            //OBSERVER PATTERN
            for (Player subscriber : this.observingPlayers) {
                subscriber.update(me);
            }
        }
    }

    Map<Player, Integer> countPoints() {
        Map<Player, Integer> result = new HashMap<>();

        if (myDamages.isEmpty()) {
            return result;
        } else {

            Player firstAttacker = myDamages.entrySet().stream().reduce((first, second) -> first).orElse(null).getKey();
            result.put(firstAttacker, FIRST_BLOOD_POINTS);

            int countingOrder = 0;
            while (!myDamages.isEmpty()) {
                Map.Entry<Player, Integer> maxEntry = null;

                for (Map.Entry<Player, Integer> entry : myDamages.entrySet()) {
                    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                        maxEntry = entry;
                    }
                }

                Optional<Integer> oldPoints = Optional.ofNullable(result.get(maxEntry.getKey()));
                result.put(maxEntry.getKey(), assignablePoints[me.getNumberOfSkulls() + countingOrder] + oldPoints.orElse(0));
                myDamages.remove(maxEntry.getKey());
                countingOrder++;
            }
        }
        return result;

    }

    void clearDamages() {
        myDamages.clear();
    }


}
