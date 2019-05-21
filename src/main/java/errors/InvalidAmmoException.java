package errors;

public class InvalidAmmoException extends RuntimeException {
    public InvalidAmmoException(){
        super("The number of ammo per color must be between 0 and 3");
    }
}
