package models.map;

import errors.*;
import models.Player;

import java.util.*;

public class GameMap {
    private Set<Square> squares;
    private PlayerSquare positions;
    private Set<Player> players;
    private Set<SpawnPoint> spawnPoints;
    private int squareId;
    private Set<RoomColor> rooms;

    public GameMap(){
        squares = new HashSet<>();
        players = new HashSet<>();
        positions = new PlayerSquare();
        spawnPoints = new HashSet<>();
        rooms = new HashSet<>();
        squareId = 0;
    }

    //creates room using a coordinate system with (0,0) in the top left of the room
    public void createRoom(int width, int height, RoomColor color, Coordinate spawnPoint){
        if(spawnPoint!=null && (spawnPoint.getX()>=width || spawnPoint.getY()>=height)) throw new RuntimeException("SpawnPoint is not in room");
        addNewRoom(color);
        Square[][] squareMatrix = new Square[width][height];
        for(int y = 0;y<height;y++){
            for(int x=0;x<width;x++){
                Square s;
                if(spawnPoint!=null && spawnPoint.getX()==x && spawnPoint.getY() == y){
                    s= new SpawnPoint(color, squareId);
                    spawnPoints.add((SpawnPoint) s);
                } else{
                    s = new AmmoPoint(color, squareId);
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
    //Connects 2 room given the topmost or leftmost communicating square
    public void connectRooms(Square s1, Square s2, boolean leftToRight, Set<Integer> doorOffsets) throws NotWallException {
        if(!(squares.contains(s1) && squares.contains(s2))) throw new NullPointerException();

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
    private void addNewRoom(RoomColor color) {
        if(rooms.contains(color)) throw new RuntimeException("Room already exists");
        rooms.add(color);
    }
    public Square getSquareById(int id){
        Iterator<Square> iterator = getAllSquares().iterator();
        while (iterator.hasNext()){
            Square s = iterator.next();
            if(s.getId()==id) return s;
        }
        return null;
    }
    public Square getSquareByPositionInRoom(Coordinate coordinate, RoomColor color){
        Square topLeft = null;
        Iterator<Square> iterator = getAllSquares().iterator();
        while (iterator.hasNext()){
            Square s = iterator.next();
            if(s.getColor().equals(color) && (topLeft==null || s.getId()<topLeft.getId())){
                topLeft = s;
            }
        }

        for(int x=0;x<coordinate.getX();x++){
            if(topLeft.hasNext(CardinalDirection.RIGHT, true)){
                topLeft = topLeft.getNextSquare(CardinalDirection.RIGHT);
            }else{
                return null;
            }
        }
        for(int y=0;y<coordinate.getY();y++){
            if(topLeft.hasNext(CardinalDirection.BOTTOM, true)){
                topLeft = topLeft.getNextSquare(CardinalDirection.BOTTOM);
            }else{
                return null;
            }
        }
        return topLeft;

    }
    public Set<SpawnPoint> getSpawnPoints(){
        return spawnPoints;
    }
    public void addPlayer(Player player) {
        if(players.size() == 5) throw new RuntimeException("Too many players on this map");
        if(players.contains(player)) throw new RuntimeException("Player already on this map");
        if(player==null) throw new NullPointerException();
        players.add(player);
    }
    public void spawnPlayer(Player player, SpawnPoint spawnPoint){
        if(player==null || spawnPoint==null) throw new NullPointerException();
        if(!spawnPoints.contains(spawnPoint)) throw new RuntimeException("Map does not contain this spawnPoint");
        if(!players.contains(player)) throw new RuntimeException("This player is not in this game");
        if(positions.getSquare(player)!=null) throw new RuntimeException("Player has already spawned");
        positions.addPlayer(player, spawnPoint);
    }
    public synchronized Set<Square> getAllSquaresAtDistance(Square from, int distance){
        if(distance<0)throw new RuntimeException("Distance cannot be negative");
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
            if(toRemove!=null && path.containsKey(toRemove)){
                path.remove(toRemove);
            }
            path.putAll(toBeAdded);
            toBeAdded.clear();

        }
        return out;
    }
    public Set<Player> getPlayersOnSquare(Square square){
        return positions.getPlayers(square);
    }
    public Square getPlayerPosition(Player player){
        return positions.getSquare(player);
    }
    public Set<Player> getAllPlayers(){
        return players;
    }
    public int getPlayersNumber(){
        return players.size();
    }
    public Set<Square> getAllSquares() { return squares; }
}


