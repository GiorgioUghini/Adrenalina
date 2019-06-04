package models.card;

import controllers.CardController;
import models.player.Ammo;
import org.junit.Test;
import static org.junit.Assert.*;

public class EffectCardTest {
    private WeaponDeck weaponDeck;

    @Test
    public void testActivation(){
        weaponDeck = new CardController().getWeaponDeck();
        Ammo ammo = new Ammo();
        ammo.blue = 3;
        ammo.yellow = 3;
        ammo.red = 3;
        int deckSize = weaponDeck.size();
        for(int i = 0; i<deckSize; i++){
            WeaponCard weaponCard = (WeaponCard) weaponDeck.draw();
            assertEquals(0, weaponCard.getEffects(ammo).getLegitEffects().size());
            Ammo price = weaponCard.getPrice();
            weaponCard.activate();
            LegitEffects effects = weaponCard.getEffects(ammo);
            if(weaponCard.exclusive){
                assertEquals(effects.getAllEffects().size(), effects.getLegitEffects().size());
                weaponCard.playEffect(effects.getLegitEffects().get(0), ammo);
                effects = weaponCard.getEffects(ammo);
                assertEquals(0, effects.getLegitEffects().size());
            }else{
                for(Effect e : effects.getLegitEffects()){
                    assertTrue(e.orderId==0 || e.orderId==-1);
                }
                weaponCard.playEffect(effects.getLegitEffects().get(0), ammo);
                for(Effect e : effects.getLegitEffects()){
                    assertTrue(e.orderId==0 || e.orderId==1 || e.orderId == -1);
                }
            }
            assertEquals(ammo.blue, 3 - price.blue);
            assertEquals(ammo.red, 3 - price.red);
            assertEquals(ammo.yellow, 3 - price.yellow);
            ammo.blue = 3;
            ammo.yellow = 3;
            ammo.red = 3;
        }
    }
}
