package errors;

public class NoActiveEffectException extends RuntimeException {
    public NoActiveEffectException() {
        super("Cannot play an action if you have not activated an effect");
    }
}
