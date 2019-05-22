package models.map;
import errors.CardAlreadyExistsException;
import models.card.AmmoCard;
import models.card.WeaponCard;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class SpawnPointTest {
    @Test
    public void testCreation(){
        SpawnPoint spawnPoint = new SpawnPoint(RoomColor.YELLOW, 1, UUID.randomUUID());
        try{
            spawnPoint.addCard(null);
            assert false;
        }catch (NullPointerException e){assert true;}
        AmmoCard errorCard = new AmmoCard(2,2,2,true);
        try{
            spawnPoint.addCard(errorCard);
            assert false;
        }catch(ClassCastException e){ assert true; }

        WeaponCard card1 = new WeaponCard();
        WeaponCard card2 = new WeaponCard();
        WeaponCard card3 = new WeaponCard();
        WeaponCard card4 = new WeaponCard();
        spawnPoint.addCard(card1);
        assertEquals(card1, spawnPoint.showCards().toArray()[0]);
        spawnPoint.addCard(card2);
        spawnPoint.addCard(card3);
        try{
            spawnPoint.addCard(card4);
            assert true;
        }catch (CardAlreadyExistsException e){assert true;}
    }

    @Test
    public void testDrawCard(){
        SpawnPoint spawnPoint = new SpawnPoint(RoomColor.YELLOW, 1, UUID.randomUUID());
        WeaponCard card1 = new WeaponCard();
        WeaponCard card2 = new WeaponCard();
        spawnPoint.addCard(card1);
        spawnPoint.addCard(card2);
        assertEquals(2, spawnPoint.showCards().size());
        assertEquals(card1, spawnPoint.drawCard(card1));
        assertEquals(card2, spawnPoint.drawCard(card2));
        assertEquals(0, spawnPoint.showCards().size());
        try{
            spawnPoint.drawCard(null);
            assert false;
        }catch(NullPointerException e){ assert true; }
        try{
            spawnPoint.drawCard(card1);
            assert false;
        }catch(NullPointerException e){ assert true; }
    }
}
