package errors;

public class NotImplementedException extends RuntimeException {
    public NotImplementedException(){
        super("Sorry mate, wrong path");
    }
}
