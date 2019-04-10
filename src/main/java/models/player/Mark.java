package models.player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Mark {
    private Map<Player, Integer> myMarks;
    private Map<Player, Integer> otherPlayersMarksOnMyLife;
    private Player me;

    public Mark(Player me) {
        this.me = me;
        myMarks = new HashMap<>();
        otherPlayersMarksOnMyLife = new HashMap<>();
    }

    public void addMark(Player fromWho, int numberOfMarks) {
        Optional<Integer> marksFromPlayer = Optional.ofNullable(otherPlayersMarksOnMyLife.get(fromWho));
        int maxMarksToAssign = Math.min(3 - fromWho.marksIDistribuited(), numberOfMarks);
        otherPlayersMarksOnMyLife.put(fromWho, (maxMarksToAssign + marksFromPlayer.orElse(0)) > 3 ? 3 : maxMarksToAssign + marksFromPlayer.orElse(0));  //Check no more than 3 marks from him
        fromWho.hasJustMarkedPlayer(me, maxMarksToAssign);
    }

    public int marksIDistribuited() {
        return myMarks.values().stream().mapToInt(Integer::intValue).sum();
    }

    public int getMarksFromPlayer(Player fromWho) {
        Optional<Integer> marksFromPlayer = Optional.ofNullable(otherPlayersMarksOnMyLife.get(fromWho));
        return marksFromPlayer.orElse(0);
    }

    public void removeAllMarkFromPlayer(Player fromWho) {
        otherPlayersMarksOnMyLife.remove(fromWho);
    }

    public void hasJustMarkedPlayer(Player who, int numberOfMarks) {
        Optional<Integer> marksOnPlayer = Optional.ofNullable(myMarks.get(who));
        myMarks.put(who, (numberOfMarks + marksOnPlayer.orElse(0)));
    }
}
