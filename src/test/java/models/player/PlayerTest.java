package models.player;

import models.card.PowerUpCard;
import models.card.WeaponCard;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PlayerTest {

    @Test
    public void equals(){
        Player pl1 = new Player(false, "Cosimo");
        Player pl2 = new Player(false, "Giorgio");
        Player pl3 = pl1;
        assertTrue(! pl1.equals(pl2));
        assertEquals(pl1, pl3);
    }

    @Test
    public void isFirstPlayer(){
        Player pl1 = new Player(false, "Giorgio");
        pl1.setFirstPlayer(true);
        assertTrue(pl1.isFirstPlayer());
    }

    @Test
    public void setName(){
        Player pl1 = new Player(false, "Giorgio");
        assertEquals(pl1.getName(), "Giorgio");
    }

    @Test
    public void getAmmo(){
        Player pl1 = new Player(false, "Giorgio");
        Ammo ammo = new Ammo();
        pl1.setAmmo(ammo);
        assertEquals(pl1.getAmmo(), ammo);
    }

    @Test
    public void getWeaponList(){
        Player pl1 = new Player(false, "Giorgio");
        List<WeaponCard> deck = new ArrayList<>();
        pl1.setWeaponList(deck);
        assertEquals(pl1.getWeaponList(), deck);
    }

    @Test
    public void getPowerUpList(){
        Player pl1 = new Player(false, "Giorgio");
        List<PowerUpCard> deck = new ArrayList<>();
        pl1.setPowerUpList(deck);
        assertEquals(pl1.getPowerUpList(), deck);
    }

}
