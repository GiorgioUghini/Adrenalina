package models.player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class Mark {
    //In the english rules a player is allowed to distribute a total of 3 marks on each player and not 3 marks in general... And we are following there rules

    private Map<Player, Integer> otherPlayersMarksOnMyLife;

    Mark() {
        otherPlayersMarksOnMyLife = new HashMap<>();
    }

    void addMark(Player fromWho, int numberOfMarks) {
        Optional<Integer> marksFromPlayer = Optional.ofNullable(otherPlayersMarksOnMyLife.get(fromWho));
        otherPlayersMarksOnMyLife.put(fromWho, (marksFromPlayer.orElse(0) + numberOfMarks) > 3 ? 3 : marksFromPlayer.orElse(0) + numberOfMarks);  //Check no more than 3 marks from him
    }

    int getMarksFromPlayer(Player fromWho) {
        Optional<Integer> marksFromPlayer = Optional.ofNullable(otherPlayersMarksOnMyLife.get(fromWho));
        return marksFromPlayer.orElse(0);
    }

    void removeAllMarkFromPlayer(Player fromWho) {
        otherPlayersMarksOnMyLife.remove(fromWho);
    }
}
