package models.card;

import errors.WeaponCardException;
import models.map.CardinalDirection;
import models.map.GameMap;
import models.map.RoomColor;
import models.map.Square;
import models.player.Player;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

class SelectorEngine {
    private GameMap gameMap;
    private Player me;
    private Select select;
    private Map<String, Player> selectedPlayers;
    private Map<String, Square> selectedSquares;

    SelectorEngine(GameMap gameMap, Player me, Select select, Map<String, Player> selectedPlayers, Map<String, Square> selectedSquares) {
        this.gameMap = gameMap;
        this.me = me;
        this.select = select;
        this.selectedPlayers = selectedPlayers;
        this.selectedSquares = selectedSquares;
    }

    /**
     * @return a set of Taggable that the user can tag
     */
    Set<Taggable> getSelectable() {
        Set<Taggable> out = new HashSet<>();
        switch (select.type) {
            case PLAYER:
                out.addAll(getSelectablePlayers());
                break;
            case SQUARE:
                out.addAll(getSelectableSquares());
                break;
            case ROOM:
                out.addAll(getSelectableRooms());
                break;
        }
        return out;
    }

    /**
     * @return A set of player the user can tag
     */
    private Set<Player> getSelectablePlayers() {
        Set<Player> out = new HashSet<>();
        boolean hasRules = select.rules != null;
        if (hasRules && select.rules.include != null) {
            for (String tag : select.rules.include) {
                out.addAll(getAllPlayersWithTag(tag));
            }
        } else {
            Set<Square> radixSquares = getAllSquaresInRadix(select.radix);
            for (Square s : radixSquares) {
                out.addAll(gameMap.getPlayersOnSquare(s));
            }
        }

        if (hasRules && select.rules.exclude != null) {
            for (String tag : select.rules.exclude) {
                out.removeAll(getAllPlayersWithTag(tag));
            }
        }
        removeMe(out);
        return out;
    }

    /**
     * @return A set of squares the user can tag
     */
    private Set<Square> getSelectableSquares() {
        Set<Square> out = new HashSet<>();
        boolean hasRules = select.rules != null;
        if (hasRules && select.rules.include != null) {
            for (String tag : select.rules.include) {
                out.add(getSquareWithTag(tag));
            }
        } else {
            Set<Square> radixSquares = getAllSquaresInRadix(select.radix);
            out.addAll(radixSquares);
        }

        if (hasRules && select.rules.exclude != null) {
            for (String tag : select.rules.exclude) {
                out.remove(getSquareWithTag(tag));
            }
        }
        return out;
    }

    /**
     * @return a set of rooms the user can tag
     */
    private Set<RoomColor> getSelectableRooms() {
        Set<RoomColor> out = new HashSet<>();
        Set<Square> squares = getAllSquaresInRadix(select.radix);
        for (Square square : squares) {
            out.add(square.getColor());
        }
        return out;
    }

    /**
     * @return A set of squares included in a given radix array, which means all the radix objects in this select
     */
    private Set<Square> getAllSquaresInRadix(Radix[] radixArray) {
        Set<Square> radix = gameMap.getAllSquares();
        if (radixArray == null) return radix;
        for (int i = 0; i < radixArray.length; i++) {
            radix.retainAll(getAllSquaresInRadix(select.radix[i]));
            if (radix.isEmpty()) return radix;
        }
        return radix;
    }

    /**
     * @return a set of squares given a single radix object
     */
    private Set<Square> getAllSquaresInRadix(Radix radix) {
        Set<Square> out = gameMap.getAllSquares();
        Square ref = getSquareWithTag(radix.ref);
        if (ref == null) return out;
        if (radix.area != null) {
            switch (radix.area) {
                case "visible":
                    out.retainAll(gameMap.getAllVisibleSquares(ref));
                    break;
                case "not_visible":
                    out.removeAll(gameMap.getAllVisibleSquares(ref));
                    break;
                case "cardinal":
                    Set<Square> toRetain = new HashSet<>();
                    for (CardinalDirection direction : CardinalDirection.values()) {
                        toRetain.addAll(gameMap.getAllSquaresByCardinal(ref, direction, !radix.throughWalls));
                    }
                    out.retainAll(toRetain);
                    break;
                case "other_rooms":
                    RoomColor myColor = ref.getColor();
                    out.removeAll(gameMap.getAllSquaresInRoom(myColor));
                    break;
                default:
                    throw new WeaponCardException("Unknown value in radix.area: " + radix.area);
            }
        }
        if (radix.min != 0 || radix.max != -1) {
            if (radix.min > 0) {
                out.removeAll(gameMap.getAllSquaresAtDistanceLessThanOrEquals(ref, radix.min - 1));
            }
            if (radix.max != -1) {
                out.retainAll(gameMap.getAllSquaresAtDistanceLessThanOrEquals(ref, radix.max));
            }
        }
        if (radix.straight != null) {
            Square to = getSquareWithTag(radix.straight);
            if (to != null) {
                CardinalDirection direction = gameMap.getDirection(ref, to);
                out.retainAll(gameMap.getAllSquaresByCardinal(ref, direction, !radix.throughWalls));
            } else {
                Logger.getAnonymousLogger().info("The tag of a 'straight' radix must exist!");
                out.clear();
            }
        }
        return out;
    }

    /**
     * @return all players with the given tag, or all players on squares with the given tag. Could be an empty set
     */
    private Set<Player> getAllPlayersWithTag(String tag) {
        Set<Player> out = new HashSet<>();
        if (tag.equals("last_damager")) {
            out.add(me.getLastDamager());
        } else if (tag.equals("damaged_from_me")) {
            out.addAll(me.getPlayersDamagedByMeThisTurn());
        } else if (selectedPlayers.containsKey(tag)) {
            out.add(selectedPlayers.get(tag));
        } else if (selectedSquares.containsKey(tag)) {
            out.addAll(gameMap.getPlayersOnSquare(selectedSquares.get(tag)));
        }
        return out;
    }

    /**
     * @return all squares with the given tag
     */
    private Square getSquareWithTag(String tag) {
        Square out = null;
        if (tag.equals("me")) {
            out = gameMap.getPlayerPosition(me);
        } else if (selectedPlayers.containsKey(tag)) {
            out = gameMap.getPlayerPosition(selectedPlayers.get(tag));
        } else if (selectedSquares.containsKey(tag)) {
            out = selectedSquares.get(tag);
        }
        return out;
    }

    /**
     * Since you cannot shoot yourself this method removes your player from the tag
     */
    private void removeMe(Set<Player> players) {
        players.remove(me);
    }
}
