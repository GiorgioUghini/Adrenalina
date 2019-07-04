package models.map;

import controllers.CardController;
import errors.CardAlreadyExistsException;
import errors.NothingToGrabException;
import models.card.AmmoCard;
import models.card.WeaponCard;
import models.card.WeaponDeck;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class SpawnPointTest {
    @Test
    public void testCreation() {
        SpawnPoint spawnPoint = new SpawnPoint(RoomColor.YELLOW, 1, UUID.randomUUID());
        try {
            spawnPoint.addCard(null);
            assert false;
        } catch (NullPointerException e) {
            assert true;
        }
        AmmoCard errorCard = new AmmoCard(2, 2, 2, true);
        try {
            spawnPoint.addCard(errorCard);
            assert false;
        } catch (ClassCastException e) {
            assert true;
        }

        List<WeaponCard> cards = getWeaponCards(4);

        WeaponCard card1 = cards.get(0);
        WeaponCard card2 = cards.get(1);
        WeaponCard card3 = cards.get(2);
        WeaponCard card4 = cards.get(3);
        spawnPoint.addCard(card1);
        assertEquals(card1, spawnPoint.showCards().get(0));
        spawnPoint.addCard(card2);
        spawnPoint.addCard(card3);
        try {
            spawnPoint.addCard(card4);
            assert true;
        } catch (CardAlreadyExistsException e) {
            assert true;
        }
    }

    @Test
    public void testDrawCard() {
        SpawnPoint spawnPoint = new SpawnPoint(RoomColor.YELLOW, 1, UUID.randomUUID());
        List<WeaponCard> cards = getWeaponCards(2);
        WeaponCard card1 = cards.get(0);
        WeaponCard card2 = cards.get(1);
        spawnPoint.addCard(card1);
        spawnPoint.addCard(card2);
        assertEquals(2, spawnPoint.showCards().size());
        assertEquals(card1, spawnPoint.drawCard(card1));
        assertEquals(card2, spawnPoint.drawCard(card2));
        assertEquals(0, spawnPoint.showCards().size());
        try {
            spawnPoint.drawCard(null);
            assert false;
        } catch (NothingToGrabException e) {
            assert true;
        }
        try {
            spawnPoint.drawCard(card1);
            assert false;
        } catch (NothingToGrabException e) {
            assert true;
        }
    }

    private List<WeaponCard> getWeaponCards(int howMany) {
        WeaponDeck deck = new CardController().getWeaponDeck();
        List<WeaponCard> out = new ArrayList<>();
        for (int i = 0; i < howMany; i++) {
            out.add((WeaponCard) deck.draw());
        }
        return out;
    }
}
