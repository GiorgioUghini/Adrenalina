package errors;

public class CheatException extends RuntimeException {
    public CheatException() {
        super("Busted!");
    }

    public CheatException(String message) {
        super(message);
    }
}
