package models.map;

import errors.*;
import models.player.Player;

import java.io.Serializable;
import java.util.*;

public class GameMap implements Serializable {
    private Set<Square> squares;
    private PlayerSquare positions;
    private Set<SpawnPoint> spawnPoints;
    private int squareId;
    private Set<RoomColor> rooms;
    private UUID id;

    /** Creates an empty GameMap */
    public GameMap(){
        squares = new HashSet<>();
        positions = new PlayerSquare();
        spawnPoints = new HashSet<>();
        rooms = new HashSet<>();
        squareId = 0;
        this.id = UUID.randomUUID();
    }

    public Set<RoomColor> getRooms(){
        return new HashSet<>(rooms);
    }

    /** creates room using a coordinate system with (0,0) in the top left corner of the room
     * @param width room width
     * @param height room height
     * @param color the color of the room, must be unique among the different rooms
     * @param spawnPoint the coordinates of the spawnPoint, (0,0) is the top left of the room. Can be null
     * @throws SquareNotInMapException if the spawnpoint is not in this room */
    void createRoom(int width, int height, RoomColor color, Coordinate spawnPoint){
        if(spawnPoint!=null && (spawnPoint.getX()>=width || spawnPoint.getY()>=height)) throw new SquareNotInMapException();
        addNewRoomIfNotExists(color);
        Square[][] squareMatrix = new Square[width][height];
        for(int y = 0;y<height;y++){
            for(int x=0;x<width;x++){
                Square s;
                if(spawnPoint!=null && spawnPoint.getX()==x && spawnPoint.getY() == y){
                    s= new SpawnPoint(color, squareId, getId());
                    spawnPoints.add((SpawnPoint) s);
                } else{
                    s = new AmmoPoint(color, squareId, getId());
                }
                squares.add(s);
                squareMatrix[x][y] = s;
                if(x>0){
                    s.connectToSquare(squareMatrix[x-1][y], CardinalDirection.LEFT);
                    squareMatrix[x-1][y].connectToSquare(s, CardinalDirection.RIGHT);
                }
                if(y>0){
                    s.connectToSquare(squareMatrix[x][y-1], CardinalDirection.TOP);
                    squareMatrix[x][y-1].connectToSquare(s, CardinalDirection.BOTTOM);
                }
                squareId++;
            }
        }
    }
    /** Connects 2 room given the topmost or leftmost communicating square
     * @param s1 the topmost square in the left room or the leftmost square in the top room
     * @param s2 the topmost square in the right room or the leftmost square in the bottom room
     * @param leftToRight if true connects rooms from left to right, s1 is the top room and s2 is the bottom room
     * @param doorOffsets a set of integers containing the offsets from which to put the doors.
     * @throws NullPointerException if one of the given squares is null
     * @throws SquareNotInMapException if one of the given squares is not in map
     * @throws NotWallException if the door is added on a link that is not a wall */
    void connectRooms(Square s1, Square s2, boolean leftToRight, Set<Integer> doorOffsets) throws NotWallException {
        checkSquareIsInMap(s1);
        checkSquareIsInMap(s2);

        Square tmp1 = s1;
        Square tmp2 = s2;

        CardinalDirection next = leftToRight ? CardinalDirection.RIGHT : CardinalDirection.BOTTOM;
        CardinalDirection d1 = leftToRight ? CardinalDirection.BOTTOM : CardinalDirection.RIGHT;
        CardinalDirection d2 = leftToRight ? CardinalDirection.TOP : CardinalDirection.LEFT;

        int i = 0;

        do{
            tmp1.connectToSquare(tmp2, d1);
            tmp2.connectToSquare(tmp1, d2);
            if(doorOffsets!=null && doorOffsets.contains(i)){
                tmp1.addDoor(d1);
                tmp2.addDoor(d2);
            }
            i++;
            tmp1 = tmp1.getNextSquare(next);
            tmp2 = tmp2.getNextSquare(next);
            if(tmp2==null) break;
        }while (tmp1!=null);
    }
    /** Connects 2 rooms through a single door, given the topmost or leftmost communicating square
     * @param s1 the topmost square in the left room or the leftmost square in the top room
     * @param s2 the topmost square in the right room or the leftmost square in the bottom room
     * @param leftToRight if true connects rooms from left to right, s1 is the top room and s2 is the bottom room
     * @param doorOffset the offset of the door, 0 if it is on the connected squares
     * @throws NullPointerException if one of the given squares is null
     * @throws SquareNotInMapException if one of the given squares is not in map
     * @throws NotWallException if the door is added on a link that is not a wall */
    void connectRooms(Square s1, Square s2, boolean leftToRight, int doorOffset){
        connectRooms(s1,s2,leftToRight,new HashSet<>(Arrays.asList(doorOffset)));
    }

    /** Connects 2 room given the topmost or leftmost communicating square ids
     * @param s1 the id of the topmost square in the left room or the leftmost square in the top room
     * @param s2 the id of the topmost square in the right room or the leftmost square in the bottom room
     * @param leftToRight if true connects rooms from left to right, s1 is the top room and s2 is the bottom room
     * @param doorOffsets a set of integers containing the offsets from which to put the doors.
     * @throws NullPointerException if one of the given squares is null
     * @throws SquareNotInMapException if one of the given squares is not in map
     * @throws NotWallException if the door is added on a link that is not a wall */
    void connectRooms(int s1, int s2, boolean leftToRight, Set<Integer> doorOffsets){
        if(s1<0 || s2<0) throw new NegativeException();
        connectRooms(
                getSquareById(s1),
                getSquareById(s2),
                leftToRight,
                doorOffsets
        );
    }
    /** Connects 2 room given the topmost or leftmost communicating square ids
     * @param s1 the id of the topmost square in the left room or the leftmost square in the top room
     * @param s2 the id of the topmost square in the right room or the leftmost square in the bottom room
     * @param leftToRight if true connects rooms from left to right, s1 is the top room and s2 is the bottom room
     * @param doorOffset the offset of the door, 0 if it is on the connected squares
     * @throws NullPointerException if one of the given squares is null
     * @throws SquareNotInMapException if one of the given squares is not in map
     * @throws NotWallException if the door is added on a link that is not a wall */
    void connectRooms(int s1, int s2, boolean leftToRight, int doorOffset){
        connectRooms(s1,s2,leftToRight, new HashSet<>(Arrays.asList(doorOffset)));
    }
    /** Adds new room to the rooms' list
     * @param color
     * @throws RoomAlreadyInMapException if there is already a room with that color in this map*/
    private void addNewRoomIfNotExists(RoomColor color) {
        if(rooms.contains(color)) throw new RoomAlreadyInMapException();
        rooms.add(color);
    }
    /** Gets square by id if exists, otherwise throw exception
     * @param id
     * @return the square with the given id. It is unique and never null
     * @throws SquareNotInMapException if there is not a square in this map with the given Id */
    Square getSquareById(int id){
        Iterator<Square> iterator = getAllSquares().iterator();
        while (iterator.hasNext()){
            Square s = iterator.next();
            if(s.getId()==id) return s;
        }
        throw new SquareNotInMapException();
    }
    /** finds the square using a coordinate system inside the room with (0,0) being in the top left corner. It does not check in near rooms.
     * @param coordinate the (x, y) coordinates of the square that will return
     * @param color the color of the room in which you are searching
     * @return the square in the room with the given color that has the given coordinates. Cannot be null
     * @throws RoomNotInMapException if there is not a room with the given color in the map
     * @throws SquareNotInMapException if the x coordinate is bigger than room's width or the y coordinate is bigger than the room's height */
    Square getSquareByPositionInRoom(Coordinate coordinate, RoomColor color){
        Square topLeft = null;
        Iterator<Square> iterator = getAllSquares().iterator();
        while (iterator.hasNext()){
            Square s = iterator.next();
            if(s.getColor().equals(color) && (topLeft==null || s.getId()<topLeft.getId())){
                topLeft = s;
            }
        }
        if(topLeft==null) throw new RoomNotInMapException();

        for(int x=0;x<coordinate.getX();x++){
            if(topLeft.hasNext(CardinalDirection.RIGHT, true)){
                topLeft = topLeft.getNextSquare(CardinalDirection.RIGHT);
            }else{
                throw new SquareNotInMapException();
            }
        }
        for(int y=0;y<coordinate.getY();y++){
            if(topLeft.hasNext(CardinalDirection.BOTTOM, true)){
                topLeft = topLeft.getNextSquare(CardinalDirection.BOTTOM);
            }else{
                throw new SquareNotInMapException();
            }
        }
        return topLeft;

    }
    /** Get a set of all the spawnpoints on the map
     * @return a set containing all the spawnpoints of the map */
    public Set<SpawnPoint> getSpawnPoints(){
        return spawnPoints;
    }
    /** Spawns player on a spawnpoint if it is not already on the map.
     * this also calls player.setMap(this) so it modifies the player
     * @param player the player to spawn
     * @param spawnPoint the spawnpoint on which the player will spawn
     * @throws NullPointerException if player is null or spawnPoint is null
     * @throws SquareNotInMapException if square is not in this map
     * @throws PlayerAlreadyOnMapException if this map already has this player */
    public void spawnPlayer(Player player, SpawnPoint spawnPoint){
        player.hasSpawnFirstTime();
        checkSquareIsInMap(spawnPoint);
        if(positions.hasPlayer(player)) throw new PlayerAlreadyOnMapException();
        positions.addPlayer(player, spawnPoint);
        player.setGameMap(this);
    }
    /** Move player to a square in the map
     * @param player
     * @param square
     * @throws NullPointerException if one of the params is null
     * @throws PlayerNotOnMapException if player is not on map
     * @throws SquareNotInMapException if map does not have this square
     * */
    public void movePlayer(Player player, Square square){
        checkPlayerInMap(player);
        checkSquareIsInMap(square);
        positions.movePlayer(player, square);
    }
    /** Removes player from map
     * @param player
     * @throws NullPointerException if player is null
     * @throws PlayerNotOnMapException if player is not on this map */
    public void removePlayer(Player player){
        checkPlayerInMap(player);
        positions.removePlayer(player);
    }
    /** Check if player is in map
     * @throws NullPointerException if player is null
     * @throws PlayerNotOnMapException if player is not on this map */
    public void checkPlayerInMap(Player player){
        if(player==null) throw new NullPointerException();
        if(!hasPlayer(player)) throw new PlayerNotOnMapException();
    }
    /** Get all squares at distance strictly equal to "distance", not less nor more
     * @param from the square from which to start calculating distances
     * @param distance the exact distance
     * @return a set containing all the squares at that distance
     * @throws NullPointerException if 'from' is null
     * @throws SquareNotInMapException if square is not in map
     * @throws NegativeException if distance is < 0*/
    Set<Square> getAllSquaresAtDistance(Square from, int distance){
        checkSquareIsInMap(from);
        if(distance<0)throw new NegativeException();
        Set<Square> out = new HashSet<>();
        Set<Square> visited = new HashSet<>();
        Map<Square, Integer> path = new LinkedHashMap<>();
        Map<Square, Integer> toBeAdded = new LinkedHashMap<>();
        Square toRemove = null;

        visited.add(from);
        path.put(from, distance);

        while(!path.isEmpty()){
            for(Map.Entry<Square, Integer> p : path.entrySet()) {
                Square s = p.getKey();
                int d = p.getValue();
                if(d > 0){
                    for(CardinalDirection c : CardinalDirection.values()){
                        if(s.hasNextWalkable(c) && !visited.contains(s.getNextSquare(c))){
                            visited.add(s.getNextSquare(c));
                            toBeAdded.put(s.getNextSquare(c), d-1);
                        }
                    }
                }else{
                    out.add(s);
                }
                toRemove = s;
            }
            if(toRemove!=null){
                path.remove(toRemove);
            }
            path.putAll(toBeAdded);
            toBeAdded.clear();

        }
        return out;
    }
    /** Get all squares at distance strictly equal to "distance", not less nor more
     * @param from the square from which to start calculating distances
     * @param distance the exact distance
     * @return a set containing all the squares at that distance
     * @throws NullPointerException if 'from' is null
     * @throws SquareNotInMapException if square is not in map
     * @throws NegativeException if distance is < 0*/
    public Set<Square> getAllSquaresAtExactDistance(Square from, int distance){
        return getAllSquaresAtDistance(from, distance);
    }
    /** Get all squares at distance less than or equal to distance, including the starting square
     * @param from the square from which to start calculating distances
     * @param distance the maximum distance
     * @return a set containing all the squares at distance less than or equal to distance, including the starting square
     * @throws NullPointerException if 'from' is null
     * @throws SquareNotInMapException if square is not in map
     * @throws NegativeException if distance is < 0*/
    public Set<Square> getAllSquaresAtDistanceLessThanOrEquals(Square from, int distance){
        Set<Square> out = new HashSet<>();
        for(int i=0;i<=distance;i++){
            out.addAll(getAllSquaresAtDistance(from, i));
        }
        return out;
    }
    /** Gets all the squares in the same room of the given square, including the square given
     * @param color the color of the room
     * @throws RoomNotInMapException if there is not a square with this color in this map
     * @return a set containing all the sets of that room. It cannot be empty */
    public Set<Square> getAllSquaresInRoom(RoomColor color){
        if(!rooms.contains(color)) throw new RoomNotInMapException();
        Set<Square> out = new HashSet<>();
        for(Square square : squares){
            if(square.getColor().equals(color)){
                out.add(square);
            }
        }
        return out;
    }
    /** Gets all the squares visible from the given square according to the game rules: all the squares in the room and all the squares in the rooms that are connected through doors on the given square, if any
     * @param from the square from which you are getting the other squares
     * @throws NullPointerException if "from" is null
     * @throws SquareNotInMapException if "from" is not in map
     * @return a set with all the visible squares */
    public Set<Square> getAllVisibleSquares(Square from){
        checkSquareIsInMap(from);
        Set<Square> out = new HashSet<>();
        out.addAll(getAllSquaresInRoom(from.getColor()));
        if(from.hasDoors()){
            for(CardinalDirection c : CardinalDirection.values()){
                if(from.hasNext(c) && from.getLink(c).isDoor()){
                    out.addAll(getAllSquaresInRoom(from.getNextSquare(c).getColor()));
                }
            }
        }
        return out;
    }
    /** Get all the players on a given square
     * @param square the square on which the players are
     * @throws NullPointerException if square is null
     * @throws SquareNotInMapException if map does not have this square
     * @return a set of players that can be empty if there are no players on the square */
    public Set<Player> getPlayersOnSquare(Square square){
        checkSquareIsInMap(square);
        return positions.getPlayers(square);
    }
    /** Get the square containing that player or null if the player is not on the map
     * @throws PlayerNotOnMapException if the player is not on the map
     * @return the square on which that player lays */
    public Square getPlayerPosition(Player player){
        if(!hasPlayer(player)) throw new PlayerNotOnMapException();
        return positions.getSquare(player);
    }
    /** get all players on map
     * @return a set containing all players on map */
    public Set<Player> getAllPlayers(){
        return positions.getAllPlayers();
    }
    /** checks if map has a player
     * @param player the player being checked
     * @return true if the player is actually on the map */
    public boolean hasPlayer(Player player){
        return positions.hasPlayer(player);
    }
    /** return the number of players on the map
     * @return an integer representing the number of players on the map */
    public int getPlayersNumber(){
        return getAllPlayers().size();
    }
    /** A set of all the squares of the map, without any order
     * @return A set of all the squares of the map, without any order */
    public Set<Square> getAllSquares() {
        Set<Square> out = new HashSet<>();
        out.addAll(squares);
        return out;
    }

    /** Returns in a list all squares in a given cardinal direction until the end of the map, stop on walls if throughWalls=false
     * @param from the starting square, will return as the first element of the list
     * @param direction the cardinal direction
     * @throws NullPointerException if one of the params is null
     * @throws SquareNotInMapException if map does not have that square
     * @return a List containing the squares in the order in which they are walked in the path */
    public List<Square> getAllSquaresByCardinal(Square from, CardinalDirection direction, boolean stopOnWalls){
        checkSquareIsInMap(from);
        if(direction==null) throw new NullPointerException();
        List<Square> out = new ArrayList<>();
        Square tmp = from;
        while(tmp!=null){
            out.add(tmp);
            if(stopOnWalls && !tmp.hasNextWalkable(direction)) break;
            tmp = tmp.getNextSquare(direction);
        }
        return out;
    }

    /** Returns in a list all squares in a given cardinal direction until the end of the map. Does not stop on walls
     * @param from the starting square, will return as the first element of the list
     * @param direction the cardinal direction
     * @throws NullPointerException if one of the params is null
     * @throws SquareNotInMapException if map does not have that square
     * @return a List containing the squares in the order in which they are walked in the path */
    public List<Square> getAllSquaresByCardinal(Square from, CardinalDirection direction){
        return getAllSquaresByCardinal(from, direction, false);
    }
    /** Gets all players in a cardinal direction, passing through walls. Does not return them in order
     * @param from the square from which the research start
     * @param direction
     * @return a set of the players in the direction, including the players on the "from" square, if any. The set could be empty. If order is needed, use getAllSquaresByCardinal method
     * @throws SquareNotInMapException if "from" square is not in map
     * @throws NullPointerException if one of the params is null */
    public Set<Player> getPlayersInDirection(Square from, CardinalDirection direction){
        List<Square> squares = getAllSquaresByCardinal(from, direction);
        Set<Player> out = new HashSet<>();
        for(Square square : squares){
            out.addAll(getPlayersOnSquare(square));
        }
        return out;
    }
    /** Get all visible players from a given square
     * @param from Square
     * @return All visible players from the given square
     * @throws NullPointerException if "from" is null
     * @throws SquareNotInMapException if the map does not have this square */
    public Set<Player> getVisiblePlayers(Square from){
        Set<Square> visibleSquares = getAllVisibleSquares(from);
        Set<Player> out = new HashSet<>();
        for(Square square : visibleSquares){
            out.addAll(getPlayersOnSquare(square));
        }
        return out;
    }

    /** Given 2 aligned squares, calculates the direction that you have to take to go from the square "from"
     * to the square "to". Works through walls too
     * @param from
     * @param to
     * @throws NoDirectionException if from=to or from and to are not aligned */
    public CardinalDirection getDirection(Square from, Square to){
        if(from.equals(to)) throw new NoDirectionException("From and to cannot be the same square");
        for(CardinalDirection direction : CardinalDirection.values()){
            if(getAllSquaresByCardinal(from, direction).contains(to)){
                return direction;
            }
        }
        throw new NoDirectionException();
    }

    /** Given as hypothesis that a top left square exists and has id 0, returns the square with the given coordinates
     * having the point (0, 0) in the top left square
     * @param x the x coordinate (left to right)
     * @param y the y coordinate (top to bottom)
     * @return a square if the coordinates are inside the map, null otherwise */
    public Square getSquareByCoordinate(int x, int y){
        Square square = getSquareById(0);
        int x0 = 0;
        int y0 = 0;
        while(x0<x || y0<y){
            boolean stuck = true;
            for(int i=x0; i<x; i++){
                if(!square.hasNext(CardinalDirection.RIGHT))break;
                square = square.getNextSquare(CardinalDirection.RIGHT);
                stuck = false;
                x0++;
            }
            for(int j=y0; j<y;j++){
                if(!square.hasNext(CardinalDirection.BOTTOM))break;
                square = square.getNextSquare(CardinalDirection.BOTTOM);
                stuck = false;
                y0++;
            }
            if(stuck) return null;
        }
        return square;
    }

    public Coordinate getSquareCoordinates(Square square){
        checkSquareIsInMap(square);
        int x = 0;
        int y = 0;
        Square tmp = square;
        boolean stuck = false;
        while(!stuck){
            stuck = true;
            while(tmp.hasNext(CardinalDirection.LEFT)){
                tmp = tmp.getNextSquare(CardinalDirection.LEFT);
                x++;
                stuck = false;
            }
            while(tmp.hasNext(CardinalDirection.TOP)){
                tmp = tmp.getNextSquare(CardinalDirection.TOP);
                y++;
                stuck = false;
            }
        }
        return new Coordinate(x, y);
    }

    public Coordinate getPlayerCoordinates(Player player){
        checkPlayerInMap(player);
        Square square = getPlayerPosition(player);
        return getSquareCoordinates(square);
    }

    /** Check that a square is in the map
     * @param square the square being checked, cannot be null
     * @throws NullPointerException if square is null */
    private void checkSquareIsInMap(Square square){
        if(square==null)throw new NullPointerException("Square cannot be null");
        if(!squares.contains(square)) throw new SquareNotInMapException();
    }

    public UUID getId(){return this.id;}
}


