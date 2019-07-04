package errors;

public class NoDirectionException extends RuntimeException {
    public NoDirectionException() {
        super("Cannot find a direction");
    }

    public NoDirectionException(String message) {
        super(message);
    }
}
