package models.player;

import models.turn.ActionGroup;

import java.io.Serializable;
import java.util.*;

class Life implements Serializable {
    private static final int MAX_LIFEPOINTS = 12;
    private static final int FIRST_BLOOD_POINTS = 1;
    private static final int[] assignablePoints = {8, 6, 4, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

    private Map<Player, Integer> myDamages;
    private List<Player> damagedBy;
    private List<Player> observingPlayers;
    private Player me;
    private Player hurtMeLast;

    Life(Player me) {
        this.me = me;
        myDamages = new LinkedHashMap<>();
        observingPlayers = new ArrayList<>();
        damagedBy = new ArrayList<>();
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

    List<Player> getDamagedBy() {
        return damagedBy;
    }

    int getTotalDamage() {
        return myDamages.values().stream().mapToInt(Integer::intValue).sum();
    }

    Player getHurtMeLast() {
        return hurtMeLast;
    }

    void damage(int damage, Player attacker) {
        int additionalDamage = me.getMarksFromPlayer(attacker);
        me.removeAllMarkFromPlayer(attacker);
        damage = damage + additionalDamage;
        int totalDamage = myDamages.values().stream().mapToInt(Integer::intValue).sum();
        hurtMeLast = attacker;
        if (totalDamage+damage > MAX_LIFEPOINTS) {
            damage = MAX_LIFEPOINTS - totalDamage;
        }
        for (int i = 0; i<damage; i++) {
            damagedBy.add(attacker);
        }
        Optional<Integer> oldDamage = Optional.ofNullable(myDamages.get(attacker));
        myDamages.put(attacker, damage + oldDamage.orElse(0));
        totalDamage = damage + oldDamage.orElse(0);
        if (totalDamage == 12) {    //Give revenge mark
            attacker.giveMark(1, me);
        }

        switch (totalDamage) {
            case 3: case 4: case 5:
                me.setLifeState(ActionGroup.LOW_LIFE);
                break;
            case 6: case 7: case 8: case 9:
                me.setLifeState(ActionGroup.VERY_LOW_LIFE);
                break;
            default:
                break;
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

        Map<Player, Integer> myDamagesCloned = new HashMap<>(myDamages);

        if (myDamagesCloned.isEmpty()) {
            return result;
        } else {

            Player firstAttacker = myDamagesCloned.entrySet()
                    .stream()
                    .reduce((first, second) -> first)
                    .orElse(myDamagesCloned.entrySet().iterator().next())
                    .getKey();
            result.put(firstAttacker, FIRST_BLOOD_POINTS);

            int countingOrder = 0;
            while (!myDamagesCloned.isEmpty()) {
                Map.Entry<Player, Integer> maxEntry = myDamagesCloned.entrySet().iterator().next();

                for (Map.Entry<Player, Integer> entry : myDamagesCloned.entrySet()) {
                    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                        maxEntry = entry;
                    }
                }

                Optional<Integer> oldPoints = Optional.ofNullable(result.get(maxEntry.getKey()));
                result.put(maxEntry.getKey(), assignablePoints[me.getDeathCount() + countingOrder] + oldPoints.orElse(0));
                myDamagesCloned.remove(maxEntry.getKey());
                countingOrder++;
            }
        }
        return result;

    }

    void clearDamages() {
        myDamages.clear();
        damagedBy.clear();
        hurtMeLast = null;
        me.setLifeState(ActionGroup.NORMAL);
    }


}
