package models.player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class Mark {
    //All commented lines refers to the implementation where a player can distribute at most 3 marks in general...
    //But in the english rules it's admitted to distribute a total of 3 marks on each player... And we are following there rules

    //private Map<Player, Integer> myMarks;
    private Map<Player, Integer> otherPlayersMarksOnMyLife;
    private Player me;

    Mark(Player me) {
        this.me = me;
        //myMarks = new HashMap<>();
        otherPlayersMarksOnMyLife = new HashMap<>();
    }

    void addMark(Player fromWho, int numberOfMarks) {
        Optional<Integer> marksFromPlayer = Optional.ofNullable(otherPlayersMarksOnMyLife.get(fromWho));
        //int maxMarksToAssign = Math.min(3 - fromWho.marksDistributed(), numberOfMarks);
        //otherPlayersMarksOnMyLife.put(fromWho, (maxMarksToAssign + marksFromPlayer.orElse(0)) > 3 ? 3 : maxMarksToAssign + marksFromPlayer.orElse(0));  //Check no more than 3 marks from him
        otherPlayersMarksOnMyLife.put(fromWho, (marksFromPlayer.orElse(0) + numberOfMarks) > 3 ? 3 : marksFromPlayer.orElse(0) + numberOfMarks);  //Check no more than 3 marks from him
        //fromWho.hasJustMarkedPlayer(me, maxMarksToAssign);
    }

    /* int marksDistributed() {
        return myMarks.values().stream().mapToInt(Integer::intValue).sum();
    } */

    int getMarksFromPlayer(Player fromWho) {
        Optional<Integer> marksFromPlayer = Optional.ofNullable(otherPlayersMarksOnMyLife.get(fromWho));
        return marksFromPlayer.orElse(0);
    }

    void removeAllMarkFromPlayer(Player fromWho) {
        otherPlayersMarksOnMyLife.remove(fromWho);
    }

    /* void hasJustMarkedPlayer(Player who, int numberOfMarks) {
        Optional<Integer> marksOnPlayer = Optional.ofNullable(myMarks.get(who));
        myMarks.put(who, (numberOfMarks + marksOnPlayer.orElse(0)));
    } */
}
