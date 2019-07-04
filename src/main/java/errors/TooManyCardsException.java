package errors;

public class TooManyCardsException extends RuntimeException {
    public TooManyCardsException(String message) {
        super(message);
    }
}
