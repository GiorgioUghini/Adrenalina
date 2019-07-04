package utils;

public interface Observable {
    void register(Observer observer);

    void unRegister(Observer observer);
}
