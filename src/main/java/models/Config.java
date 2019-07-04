package models;

public class Config {
    private int MATCH_CONNECTION_TIMEOUT;
    private int TURN_TIMEOUT;

    Config() {

    }

    Config(int matchConnectionTimeout, int turnTimeout) {
        MATCH_CONNECTION_TIMEOUT = matchConnectionTimeout;
        TURN_TIMEOUT = turnTimeout;
    }

    public int getMatchConnectionTimeout() {
        return MATCH_CONNECTION_TIMEOUT;
    }

    public int getTurnTimeout() {
        return TURN_TIMEOUT;
    }
}
