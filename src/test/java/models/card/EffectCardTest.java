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
        for(int i = 0; i<weaponDeck.size(); i++){
            WeaponCard weaponCard = (WeaponCard) weaponDeck.draw();
            assertEquals(0, weaponCard.getEffects(ammo).getLegitEffects().size());
            weaponCard.activate();
            LegitEffects effects = weaponCard.getEffects(ammo);
            if(weaponCard.exclusive){
                assertEquals(effects.getAllEffects().size(), effects.getLegitEffects().size());
                weaponCard.playEffect(effects.getLegitEffects().get(0));
                effects = weaponCard.getEffects(ammo);
                assertEquals(0, effects.getLegitEffects().size());
            }else{
                for(Effect e : effects.getLegitEffects()){
                    assertTrue(e.orderId==0 || e.orderId==-1);
                }
                weaponCard.playEffect(effects.getLegitEffects().get(0));
                for(Effect e : effects.getLegitEffects()){
                    assertTrue(e.orderId==0 || e.orderId==1 || e.orderId == -1);
                }
            }
        }
    }
}
