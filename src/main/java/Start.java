import controllers.CardController;

import java.io.IOException;

public class Start {
    public static void main(String[] args) throws IOException {
        CardController cc = new CardController();
        cc.getPowerUpDeck();
        cc.getWeaponDeck();
    }
}
