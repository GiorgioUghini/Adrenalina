package errors;

public class CardAlreadyExistsException extends RuntimeException {
    public CardAlreadyExistsException(){
        super("There is already a card on this square");
    }
}
