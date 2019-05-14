package models.map;

import errors.MapNotExistsException;
import models.AmmoCard;
import models.Deck;

import java.util.Arrays;
import java.util.HashSet;

public class MapGenerator {
    /** given an integer from 0 to 3, generates a map based on the order on which they appear on the instructions. Map 3 is the map that is not shown in the instructions
     * @param mapNumber from 0 to 3
     * @throws MapNotExistsException if mapNumber is not in {0,1,2,3} */
    public static GameMap generate(int mapNumber) throws MapNotExistsException{
        GameMap gameMap = new GameMap();
        switch (mapNumber){
            case 0:
                gameMap.createRoom(3,1,RoomColor.BLUE, new Coordinate(2,0));
                gameMap.createRoom(3,1, RoomColor.RED, new Coordinate(0,0));
                gameMap.createRoom(1,2, RoomColor.YELLOW, new Coordinate(0,1));
                gameMap.createRoom(2,1, RoomColor.WHITE, null);
                //connect blue and red room
                gameMap.connectRooms(0,3,true, new HashSet<>(Arrays.asList(0,2)));
                //red and yellow
                gameMap.connectRooms(5,6,false,0);
                //red and white
                gameMap.connectRooms(4,8,true,0);
                //white and yellow
                gameMap.connectRooms(9,7,false,0);
                break;
            case 1:
                gameMap.createRoom(3,1, RoomColor.BLUE, new Coordinate(2,0));
                gameMap.createRoom(1,1, RoomColor.GREEN, null);
                gameMap.createRoom(2,1, RoomColor.RED, new Coordinate(0,0));
                gameMap.createRoom(2,2, RoomColor.YELLOW, new Coordinate(1,1));
                gameMap.createRoom(1,1, RoomColor.WHITE, null);
                //blue and red
                gameMap.connectRooms(0,4,true,0);
                //blue and green
                gameMap.connectRooms(2,3,false,0);
                //blue and yellow
                gameMap.connectRooms(2,6,true,0);
                //red and yellow
                gameMap.connectRooms(5,6,false,null );
                //red and white
                gameMap.connectRooms(5,10,true,0);
                //green and yellow
                gameMap.connectRooms(3,7,true,0);
                //white and yellow
                gameMap.connectRooms(10,8,false,0);
                break;
            case 2:
                gameMap.createRoom(1,2,RoomColor.RED, new Coordinate(0,1));
                gameMap.createRoom(2,1, RoomColor.BLUE,new Coordinate(1,0));
                gameMap.createRoom(1,1,RoomColor.GREEN, null);
                gameMap.createRoom(1,1,RoomColor.PURPLE, null);
                gameMap.createRoom(2,2,RoomColor.YELLOW, new Coordinate(1,1));
                gameMap.createRoom(2,1,RoomColor.WHITE, null);
                //red blue
                gameMap.connectRooms(0,2,false,0);
                //red white
                gameMap.connectRooms(1,10,true,0);
                //red purple
                gameMap.connectRooms(1,5,false,null);
                //blue purple
                gameMap.connectRooms(2,5,true, 0);
                //blue yellow
                gameMap.connectRooms(3,6,true, 0);
                //blue green
                gameMap.connectRooms(3,4,false,0);
                //purple yellow
                gameMap.connectRooms(5,6,false,null);
                //white yellow
                gameMap.connectRooms(11,8,false,0);
                break;
            case 3:
                gameMap.createRoom(1,2,RoomColor.RED, new Coordinate(0,1));
                gameMap.createRoom(2,1, RoomColor.BLUE, new Coordinate(1,0));
                gameMap.createRoom(2,1,RoomColor.PURPLE, null);
                gameMap.createRoom(3,1,RoomColor.WHITE, null);
                gameMap.createRoom(1,2,RoomColor.YELLOW, new Coordinate(0,1));
                //red blue
                gameMap.connectRooms(0,2,false,0);
                //red purple
                gameMap.connectRooms(1,4,false,null);
                //red white
                gameMap.connectRooms(1,6,true, 0);
                //blue purple
                gameMap.connectRooms(2,4,true,new HashSet<>(Arrays.asList(0,1)));
                //purple yellow
                gameMap.connectRooms(5,9,false,0);
                //white yellow
                gameMap.connectRooms(8,10, false, 0);
                break;
            default:
                throw new MapNotExistsException();
        }
        return gameMap;
    }

    /**
     * Fill map with cards, draws 3 cards from the weapon deck on spawn points and one card from ammo deck from ammo deck
     * @param ammoDeck the deck with ammos
     * @param weaponDeck the deck with weapons
     * @param gameMap the map of the game
     * */
    public static void initCards(Deck ammoDeck, Deck weaponDeck, GameMap gameMap){
        gameMap.getAllSquares()
                .stream()
                .forEach(s -> {
                    if(s.isSpawnPoint()){
                        for(int i=0; i<3;i++){
                            s.addCard(weaponDeck.draw());
                        }
                    }else{
                        s.addCard(ammoDeck.draw());
                    }
                });
    }
}
