package models.card;

import errors.InvalidAmmoException;
import org.junit.Test;
import static org.junit.Assert.*;

public class AmmoCardTest {
    @Test
    public void testCreation(){
        AmmoCard a = new AmmoCard(1,2,3, false);
        assertEquals(1, a.getRed());
        assertEquals(2, a.getYellow());
        assertEquals(3, a.getBlue());
        assertFalse(a.hasPowerup());
    }
    @Test
    public void testCreationWithErrors(){
        try{
            new AmmoCard(4,3,3, true);
            assert false;
        }catch(InvalidAmmoException e){
            assert  true;
        }
    }
}
