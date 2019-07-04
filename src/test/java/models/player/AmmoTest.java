package models.player;

import controllers.CardController;
import models.card.PowerUpCard;
import models.card.PowerUpDeck;
import models.map.RoomColor;
import org.junit.Test;

import static org.junit.Assert.*;

public class AmmoTest {
    @Test
    public void createWithPowerUp() {
        PowerUpCard blue = getByName("Teletrasporto", RoomColor.BLUE);
        PowerUpCard red = getByName("Teletrasporto", RoomColor.RED);
        PowerUpCard yellow = getByName("Teletrasporto", RoomColor.YELLOW);
        assertNotNull(blue);
        assertNotNull(red);
        assertNotNull(yellow);
        Ammo ammo = new Ammo(blue);
        assertEquals(ammo, new Ammo(0, 1, 0));
        ammo = new Ammo(yellow);
        assertEquals(ammo, new Ammo(0, 0, 1));
        ammo = new Ammo(red);
        assertEquals(ammo, new Ammo(1, 0, 0));
    }

    @Test
    public void cannotGoAbove3() {
        Ammo ammo = new Ammo(0, 0, 0);
        ammo.add(new Ammo(12, 12, 12));
        assertEquals(new Ammo(3, 3, 3), ammo);
    }

    @Test
    public void cannotGoBelow0() {
        Ammo ammo = new Ammo(2, 2, 2);
        ammo.remove(new Ammo(12, 12, 12));
        assertEquals(new Ammo(0, 0, 0), ammo);
    }

    @Test
    public void testGetSum() {
        Ammo a = new Ammo(0, 0, 1);
        Ammo b = new Ammo(0, 1, 0);
        Ammo c = a.getSum(b);
        assertEquals(new Ammo(0, 0, 1), a);
        assertEquals(new Ammo(0, 1, 1), c);
    }

    @Test
    public void testIsEmpty() {
        assertFalse(new Ammo(1, 1, 1).isEmpty());
        assertTrue(new Ammo(0, 0, 0).isEmpty());
    }

    @Test
    public void testEquals() {
        Ammo a = new Ammo(1, 2, 1);
        Ammo b = new Ammo(1, 1, 1);
        Ammo c = new Ammo(1, 2, 1);
        assertEquals(a, a);
        assertEquals(a, c);
        assertNotEquals(a, b);
        assertNotEquals(a, new Object());
        assertNotEquals(a, null);
        assertEquals(a.hashCode(), c.hashCode());
    }

    private PowerUpCard getByName(String name, RoomColor color) {
        CardController controller = new CardController();
        PowerUpDeck deck = controller.getPowerUpDeck();
        int size = deck.size();
        for (int i = 0; i < size; i++) {
            PowerUpCard powerUpCard = (PowerUpCard) deck.draw();
            if (powerUpCard.name.equals(name)) {
                if (color == null || color == powerUpCard.color) return powerUpCard;
            }
        }
        return null;
    }
}
