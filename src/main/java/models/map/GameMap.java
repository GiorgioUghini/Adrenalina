package models.map;

import errors.NotWallException;
import models.Player;

import java.util.HashSet;
import java.util.Set;

public class GameMap {
    private Set<Square> squares;
    private PlayerSquare positions;
    private Set<Player> players;
    private Set<SpawnPoint> spawnPoints;
    private int squareId;

    public GameMap(){
        squares = new HashSet<>();
        players = new HashSet<>();
        positions = new PlayerSquare();
        spawnPoints = new HashSet<>();
        squareId = 0;
    }

    //creates room using a coordinate system with (0,0) in the bottom left of the room
    public void createRoom(int width, int height, RoomColor color, Coordinate spawnPoint){
        Square[][] squareMatrix = new Square[width][height];
        for(int x = 0;x<width;x++){
            for(int y=0;y<height;y++){
                Square s;
                if(spawnPoint.getX()==x && spawnPoint.getY() == y){
                    s= new SpawnPoint(color, squareId);
                    spawnPoints.add((SpawnPoint) s);
                } else{
                    s = new AmmoPoint(color, squareId);
                }
                squares.add(s);
                squareMatrix[x][y] = s;
                if(x>0){
                    s.connectToSquare(squareMatrix[x-1][y], CardinalDirection.RIGHT);
                    squareMatrix[x-1][y].connectToSquare(s, CardinalDirection.LEFT);
                }
                if(y>0){
                    s.connectToSquare(squareMatrix[x][y-1], CardinalDirection.BOTTOM);
                    squareMatrix[x][y-1].connectToSquare(s, CardinalDirection.TOP);
                }
            }
        }
    }
    //Connects 2 room given the topmost or leftmost communicating square
    public void connectRooms(Square s1, Square s2, boolean leftToRight, Set<Integer> doorOffsets) throws NotWallException {
        Square tmp1 = s1;
        Square tmp2 = s2;

        CardinalDirection next = leftToRight ? CardinalDirection.RIGHT : CardinalDirection.BOTTOM;
        CardinalDirection d1 = leftToRight ? CardinalDirection.BOTTOM : CardinalDirection.RIGHT;
        CardinalDirection d2 = leftToRight ? CardinalDirection.TOP : CardinalDirection.LEFT;

        tmp1.connectToSquare(tmp2, d1);
        tmp2.connectToSquare(tmp2, d2);

        int i = 0;

        while (tmp1.hasNext(next)){
            if(!tmp2.hasNext(next)) break;
            tmp1 = tmp1.getNextSquare(next);
            tmp2 = tmp2.getNextSquare(next);
            tmp1.connectToSquare(tmp2, d1);
            tmp2.connectToSquare(tmp1, d1);
            if(doorOffsets.contains(i)){
                tmp1.addDoor(d1);
                tmp2.addDoor(d2);
            }
            i++;
        }
    }
    public Set<SpawnPoint> getSpawnPoints(){
        return spawnPoints;
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
}
