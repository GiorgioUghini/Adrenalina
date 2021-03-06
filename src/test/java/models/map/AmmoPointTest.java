package models.map;

import errors.CardAlreadyExistsException;
import errors.NothingToGrabException;
import models.card.AmmoCard;
import models.card.Card;
import models.card.WeaponCard;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AmmoPointTest {
    @Test
    public void testAddCard() {
        AmmoPoint ammoPoint = new AmmoPoint(RoomColor.YELLOW, 1, UUID.randomUUID());
        assertNull(ammoPoint.showCard());
        Card card = new AmmoCard(1, 1, 1, true);
        ammoPoint.addCard(card);
        assertEquals(card, ammoPoint.showCard());
    }

    @Test
    public void testAddCardWithError() {
        AmmoPoint ammoPoint = new AmmoPoint(RoomColor.YELLOW, 1, UUID.randomUUID());
        Card card1 = new WeaponCard();
        Card card2 = new AmmoCard(1, 1, 1, false);
        Card card3 = new AmmoCard(2, 2, 2, true);
        try {
            ammoPoint.addCard(card1);
            assert false;
        } catch (ClassCastException e) {
            assert true;
        }
        try {
            ammoPoint.addCard(null);
            assert false;
        } catch (NullPointerException e) {
            assert true;
        }
        try {
            ammoPoint.addCard(card2);
            ammoPoint.addCard(card3);
            assert false;
        } catch (CardAlreadyExistsException e) {
            assert true;
        }
    }

    @Test
    public void testDrawCard() {
        AmmoPoint ammoPoint = new AmmoPoint(RoomColor.YELLOW, 1, UUID.randomUUID());
        Card card = new AmmoCard(1, 1, 1, true);
        ammoPoint.addCard(card);
        assertEquals(card, ammoPoint.drawCard());
        try {
            ammoPoint.drawCard();
            assert false;
        } catch (NothingToGrabException e) {
            assert true;
        }
    }
}
