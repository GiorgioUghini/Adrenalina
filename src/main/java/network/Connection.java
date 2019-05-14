package network;

public interface Connection {
    void init();
    String getToken();
    void setToken(String token);
    void registerPlayer(String username);
    void sendRequest(Request request);
    void receiveResponse(Response response);
}
