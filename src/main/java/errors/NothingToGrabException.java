package errors;

public class NothingToGrabException extends RuntimeException {
    public NothingToGrabException() {
        super("Nothing to grab here");
    }
}
