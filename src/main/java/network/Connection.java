package network;

public interface Connection {
    void init();
    String getToken();
    void setToken(String token);
    void registerPlayer(String username);
    void validActions();
    void receiveResponse(Response response);
}
