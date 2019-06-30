package errors;

public class UnloadedWeaponException extends RuntimeException {
    public UnloadedWeaponException(){
        super("Weapon is not loaded");
    }
}
